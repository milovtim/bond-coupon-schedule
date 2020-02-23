package ru.milovtim.bonds;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.milovtim.bonds.module.MainModule;
import ru.milovtim.bonds.service.TinkofInvestAPIClient;
import ru.milovtim.bonds.service.TinkoffInvestService;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;

public class Test {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MainModule());
        TinkoffInvestService investService = injector.getInstance(TinkoffInvestService.class);
        Map<String, Portfolio> portByAcc = investService.getPortfolioByAccount();

        portByAcc.forEach((type, port) -> {
            System.out.println(type);
        });

        /*
        Document doc = Jsoup.connect("https://smart-lab.ru/q/bonds/RU000A0JWXQ7/").get();
        List<BondCoupon> coupons = doc.select(".simple-little-table.bond tbody tr").stream()
            .skip(1)//skip table header
            .map(element -> new BondCouponImpl(new BondCouponHtmlTableAdapter(element)))
            .collect(Collectors.toList());
        System.out.println(coupons);*/
    }
}
