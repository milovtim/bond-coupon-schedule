package ru.milovtim.bonds.service;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.milovtim.bonds.module.ThirdPartyConfig;
import ru.milovtim.bonds.pojo.BondPaymentSchedule;

public class BondScrappingServiceImpl implements BondScrappingService {

    private final SmartLabHttpClient httpClient;
    private final ThirdPartyConfig config;

    @Inject
    public BondScrappingServiceImpl(SmartLabHttpClient httpClient, ThirdPartyConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }

    @Override
    public void getBondData(String isin) {

    }


    private Document parseSmartlabResponse(InputStream inputStream) {
        try {
            return Jsoup.parse(inputStream, "UTF-8", config.getSmartlabHost());
        } catch (IOException e) {
            throw new RuntimeException("Error parsing http response body", e);
        }
    }

    //doc = Jsoup.connect("https://smart-lab.ru/q/bonds/RU000A0JWXQ7/").get();
    @Override
    public BondPaymentSchedule getBondSchedule(String isin) {
        List<BondPaymentSchedule.BondPayment> coupons =
            httpClient.getBondPage(isin)
                .map(ResponseBody::byteStream)
                .map(this::parseSmartlabResponse)
                .map(doc -> doc.select(".simple-little-table.bond tbody").eq(1).select("tr").stream()
                    .skip(1)//skip table header
                    //todo fill POJO with data from html table
                    .map(element -> new BondPaymentSchedule.BondPayment())
                    .collect(Collectors.toList())
                ).blockingFirst();

        BondPaymentSchedule schedule = new BondPaymentSchedule();
        schedule.setPayments(coupons);
        return schedule;
    }
}
