package ru.milovtim.bonds.service;

import java.util.AbstractMap;
import java.util.Map;

import io.reactivex.Observable;
import ru.milovtim.bonds.pojo.AccountPortfolio;
import ru.milovtim.bonds.pojo.UserAccounts;
import ru.tinkoff.invest.openapi.exceptions.OpenApiException;
import ru.tinkoff.invest.openapi.models.ResponseStatus;
import ru.tinkoff.invest.openapi.models.RestResponse;

import static java.util.stream.Collectors.toMap;

public class TinkofInvestService {

    private final TinkofInvestAPIClient apiClient;

    public TinkofInvestService(TinkofInvestAPIClient apiClient) {
        this.apiClient = apiClient;
    }


    public Map<String, AccountPortfolio> getPortfolioByAccount() {
        Observable<Map.Entry<String, AccountPortfolio>> portfolioObs = apiClient.getUserAccounts()
            .map(accApiResp -> {
                UserAccounts payload = accApiResp.payload;
                return payload.getAccounts();
            })
            .flatMap(userAccounts -> userAccounts.stream()
                .map(userAccount -> {
                    Observable<Map.Entry<String, AccountPortfolio>> portfObs = apiClient
                        .getAccountPortfolio(userAccount.getBrokerAccountId())
                        .map(portfolioApiResp -> new AbstractMap.SimpleImmutableEntry<>(
                            userAccount.getBrokerAccountType(),
                            portfolioApiResp.payload));
                    return portfObs;
                })
                .reduce((obs1, obs2) -> obs1.mergeWith(obs2)).orElse(Observable.empty()));

        return portfolioObs.toList()
            .map(e -> e.stream().collect(toMap(Map.Entry::getKey, Map.Entry::getValue)))
            .blockingGet();
    }

    private static void checkStatus(RestResponse<?> apiResponse) {
        if (apiResponse.status != ResponseStatus.OK) {
            if (apiResponse.payload instanceof OpenApiException) {
                OpenApiException oae = (OpenApiException) apiResponse.payload;
                throw new RuntimeException("Error during API call: " + oae.getCode(), oae);
            } else {
                throw new RuntimeException(apiResponse.payload.toString());
            }
        }
    }
}
