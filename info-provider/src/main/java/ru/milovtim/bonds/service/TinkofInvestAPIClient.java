package ru.milovtim.bonds.service;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.milovtim.bonds.pojo.AccountPortfolio;
import ru.milovtim.bonds.pojo.UserAccounts;
import ru.tinkoff.invest.openapi.models.RestResponse;

public interface TinkofInvestAPIClient {

    @GET("user/accounts")
    Observable<RestResponse<UserAccounts>> getUserAccounts();

    @GET("portfolio")
    Observable<RestResponse<AccountPortfolio>> getAccountPortfolio(@Query("brokerAccountId") String brokerAccountId);

}
