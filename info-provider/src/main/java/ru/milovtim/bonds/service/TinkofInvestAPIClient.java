package ru.milovtim.bonds.service;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.milovtim.bonds.pojo.AccountPortfolio;
import ru.milovtim.bonds.pojo.UserAccounts;
import ru.tinkoff.invest.openapi.data.OpenApiResponse;

public interface TinkofInvestAPIClient {

    @GET("user/accounts")
    Observable<OpenApiResponse<UserAccounts>> getUserAccounts();

    @GET("portfolio")
    Observable<OpenApiResponse<AccountPortfolio>> getAccountPortfolio(@Query("brokerAccountId") String brokerAccountId);

}
