package ru.milovtim.bonds.service;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import ru.milovtim.bonds.pojo.AccountPortfolio;
import ru.milovtim.bonds.pojo.UserAccounts;
import ru.tinkoff.invest.openapi.data.OpenApiResponse;

public class TinkofInvestService {

    private final TinkofInvestAPIClient apiClient;

    public TinkofInvestService(TinkofInvestAPIClient apiClient) {
        this.apiClient = apiClient;
    }


    public Map<String, AccountPortfolio> getPortfolioByAccount() {
        OpenApiResponse<UserAccounts> userAccountsResp = null;
        try {
            userAccountsResp = apiClient.getUserAccounts().execute().body();
            if ("Ok".equalsIgnoreCase(userAccountsResp.status)) {
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
            } else {
                throw new IllegalStateException("Cannot get user accounts");
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
