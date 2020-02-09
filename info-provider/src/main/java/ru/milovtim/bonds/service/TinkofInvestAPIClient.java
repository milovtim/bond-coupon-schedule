package ru.milovtim.bonds.service;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.milovtim.bonds.pojo.AccountPortfolio;
import ru.milovtim.bonds.pojo.UserAccounts;
import ru.tinkoff.invest.openapi.data.OpenApiResponse;

public interface TinkofInvestAPIClient {

    @GET("user/accounts")
    Call<OpenApiResponse<UserAccounts>> getUserAccounts();

    @GET("portfolio")
    Call<OpenApiResponse<AccountPortfolio>> getAccountPortfolio(@Query("brokerAccountId") String brokerAccountId);

}
