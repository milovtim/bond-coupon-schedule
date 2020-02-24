package ru.milovtim.bonds.service;

import javax.inject.Inject;
import java.util.AbstractMap;
import java.util.Map;

import io.reactivex.Observable;
import ru.milovtim.bonds.pojo.UserAccounts;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;

import static java.util.stream.Collectors.toMap;

public class TinkoffInvestService {

    private final TinkoffInvestAPIClient apiClient;

    @Inject
    public TinkoffInvestService(TinkoffInvestAPIClient apiClient) {
        this.apiClient = apiClient;
    }


    public Map<String, Portfolio> getPortfolioByAccount() {
        Observable<Map.Entry<String, Portfolio>> portfolioObs = apiClient.getUserAccounts()
            .map(accApiResp -> {
                UserAccounts payload = accApiResp.payload;
                return payload.getAccounts();
            })
            .flatMap(userAccounts -> userAccounts.stream()
                .map(userAccount -> apiClient
                    .getAccountPortfolio(userAccount.getBrokerAccountId())
                    .<Map.Entry<String, Portfolio>>map(portfolioApiResp -> new AbstractMap.SimpleImmutableEntry<>(
                        userAccount.getBrokerAccountType(),
                        portfolioApiResp.payload)))
                .reduce(Observable::mergeWith).orElse(Observable.empty()));

        return portfolioObs.toList()
            .map(e -> e.stream().collect(toMap(Map.Entry::getKey, Map.Entry::getValue)))
            .blockingGet();
    }
}
