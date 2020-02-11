package ru.milovtim.bonds.service;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import ru.milovtim.bonds.pojo.AccountPortfolio;
import ru.milovtim.bonds.pojo.UserAccounts;
import ru.tinkoff.invest.openapi.data.OpenApiResponse;
import ru.tinkoff.invest.openapi.exceptions.OpenApiException;

public class TinkofInvestService {

    private final TinkofInvestAPIClient apiClient;

    public TinkofInvestService(TinkofInvestAPIClient apiClient) {
        this.apiClient = apiClient;
    }


    public Map<String, AccountPortfolio> getPortfolioByAccount() {
        OpenApiResponse<UserAccounts> userAccountsResp = null;
        try {
            userAccountsResp = apiClient.getUserAccounts()
                .flatMap(userAccountsApiResp -> {
                    checkStatus(userAccountsApiResp);
                    List<UserAccounts.UserAccount> accounts = userAccountsApiResp.payload.getAccounts();
//                    ListIterator<UserAccounts.UserAccount> accIter = accounts.listIterator();
                    Observable<Object> collect = accounts.stream().map(acc -> apiClient.getAccountPortfolio(acc.getBrokerAccountId()))
                        .map(apiResp -> apiResp)
                        .collect(Observable::empty, (accumulator, respObservable) -> accumulator.join(respObservable),
                            (observable, observable2) -> observable.join(observable2));
                })

            Map<String, AccountPortfolio> result = userAccountsResp.payload.getAccounts().stream().collect(Collectors.toMap(
                UserAccounts.UserAccount::getBrokerAccountType,
                userAccount -> {
                    OpenApiResponse<AccountPortfolio> portResp = null;
                    try {
                        portResp = apiClient.getAccountPortfolio(
                            userAccount.getBrokerAccountId()).execute().body();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if ("Ok".equalsIgnoreCase(portResp.status)) {
                        AccountPortfolio accountPortfolio = portResp.payload;
                        return accountPortfolio;
                    } else {
                        throw new IllegalStateException("Cannot get portfolio for account'" +
                            userAccount.getBrokerAccountId() + "'");
                    }
                }
            ));
            return result;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void checkStatus(OpenApiResponse<?> apiResponse) {
        if (!"OK".equalsIgnoreCase(apiResponse.status)) {
            if (apiResponse.payload instanceof OpenApiException) {
                OpenApiException oae = (OpenApiException) apiResponse.payload;
                throw new RuntimeException("Error during API call: " + oae.getCode(), oae);
            } else {
                throw new RuntimeException(((Object) apiResponse.payload).toString());
            }
        }
    }
}
