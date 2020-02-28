package ru.milovtim.bonds.service;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.milovtim.bonds.module.ThirdPartyConfig;
import ru.milovtim.bonds.pojo.BondPaymentSchedule;
import ru.milovtim.bonds.pojo.adapter.BondsScheduleHtmlTableParserImpl;

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
        Collection<? extends BondPaymentSchedule.BondPayment> coupons =
            httpClient.getBondPage(isin)
                .map(ResponseBody::byteStream)
                .map(this::parseSmartlabResponse)
                .map(BondsScheduleHtmlTableParserImpl::new)
                .map(BondsScheduleHtmlTableParserImpl::getData)
                .blockingFirst();

        BondPaymentSchedule schedule = new BondPaymentSchedule();
        schedule.setPayments(new ArrayList<>(coupons));
        return schedule;
    }
}
