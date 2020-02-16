package ru.milovtim.bonds.service;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.internal.operators.observable.ObservableAll;
import ru.milovtim.bonds.pojo.AccountPortfolio;
import ru.milovtim.bonds.pojo.UserAccounts;
import ru.tinkoff.invest.openapi.data.OpenApiResponse;
import ru.tinkoff.invest.openapi.exceptions.OpenApiException;

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

    private static void checkStatus(OpenApiResponse<?> apiResponse) {
        if (!"OK".equalsIgnoreCase(apiResponse.status)) {
            if (apiResponse.payload instanceof OpenApiException) {
                OpenApiException oae = (OpenApiException) apiResponse.payload;
                throw new RuntimeException("Error during API call: " + oae.getCode(), oae);
            } else {
                throw new RuntimeException(apiResponse.payload.toString());
            }
        }
    }
}
