package ru.milovtim.bonds;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Test {
    private static String TINKOV_INVEST_BASE_URL = "https://api-invest.tinkoff.ru/openapi";
    private static HttpRequest.Builder TINKOV_INVEST_REQ_BUILDER = HttpRequest.newBuilder(
        URI.create(TINKOV_INVEST_BASE_URL));


    private static HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.of(4, ChronoUnit.SECONDS))
        .version(HttpClient.Version.HTTP_1_1)
        .build();

    public Test(String token) {
        TINKOV_INVEST_REQ_BUILDER
            .header("Authorization",
                "Bearer " + token);
    }


    public String tinkoffPortfolio(String account) {
        TINKOV_INVEST_REQ_BUILDER.uri()
        return null;
    }


    public String tinkoffAccounts() {
        TINKOV_INVEST_REQ_BUILDER.
    }

    public static void main(String[] args) throws IOException {
        Test test = new Test(args[0]);
        /*Document doc = Jsoup.connect("https://smart-lab.ru/q/bonds/RU000A0JWXQ7/").get();
        List<BondCoupon> coupons = doc.select(".simple-little-table.bond tbody tr").stream()
            .skip(1)//skip table header
            .map(element -> new BondCouponImpl(new BondCouponHtmlTableAdapter(element)))
            .collect(Collectors.toList());
        System.out.println(coupons);*/



    }
}
