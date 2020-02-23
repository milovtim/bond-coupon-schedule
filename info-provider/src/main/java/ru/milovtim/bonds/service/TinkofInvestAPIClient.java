package ru.milovtim.bonds.service;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.milovtim.bonds.pojo.UserAccounts;
import ru.tinkoff.invest.openapi.models.RestResponse;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;

public interface TinkofInvestAPIClient {

    @GET("user/accounts")
    Observable<RestResponse<UserAccounts>> getUserAccounts();

    @GET("portfolio")
    Observable<RestResponse<Portfolio>> getAccountPortfolio(@Query("brokerAccountId") String brokerAccountId);

}
