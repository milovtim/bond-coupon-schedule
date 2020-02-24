package ru.milovtim.bonds.service;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.milovtim.bonds.pojo.BondPaymentSchedule;

public class BondScrappingServiceImpl implements BondScrappingService {

    private final SmartLabHttpClient httpClient;

    @Inject
    public BondScrappingServiceImpl(SmartLabHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void getBondData(String isin) {

    }

    @Override
    public BondPaymentSchedule getBondSchedule(String isin) {
        Observable<ResponseBody> bondPageObs = httpClient.getBondPage(isin);
        Observable<Document> docObs = bondPageObs.map(responseBody -> Jsoup.parse(responseBody.byteStream(), "UTF-8", null));
//        doc = Jsoup.connect("https://smart-lab.ru/q/bonds/RU000A0JWXQ7/").get();
        List<BondPaymentSchedule.BondPayment> coupons = docObs.map(doc ->
            doc.select(".simple-little-table.bond tbody tr").stream()
            .skip(1)//skip table header
            .map(element -> new BondPaymentSchedule.BondPayment())
            .collect(Collectors.toList())
        ).blockingFirst();

        BondPaymentSchedule schedule = new BondPaymentSchedule();
        schedule.setPayments(coupons);
        return schedule;
    }
}
