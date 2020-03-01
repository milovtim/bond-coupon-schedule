package ru.milovtim.bonds.pojo.adapter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.javatuples.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.milovtim.bonds.pojo.BondPaymentSchedule;
import ru.tinkoff.invest.openapi.models.Currency;
import ru.tinkoff.invest.openapi.models.MoneyAmount;

public class BondsScheduleHtmlTableParserImpl
    extends JsoupHtmlTableParser<BondPaymentSchedule.BondPayment, Map<String, String>> {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu");

    public BondsScheduleHtmlTableParserImpl(Document htmlDocWithTable) {
        super(htmlDocWithTable);
    }

    @Override
    public Map<String, String> getInfo() {
        return this.getHtmlTableDoc()
            .select(".simple-little-table.bond tbody")
            .eq(0)//bond summary is the first table
            .select("tr").stream()
            .skip(1)//skip table header
            .map(tr -> Pair.fromIterable(tr.select("td"), 0))
            .filter(pair -> pair.getValue0() != null && pair.getValue1() != null)
            .collect(Collectors.toMap(pair -> pair.getValue0().text(), pair -> pair.getValue1().text()));
    }

    @Override
    public Collection<? extends BondPaymentSchedule.BondPayment> getPayments() {
        return this.getHtmlTableDoc()
            .select(".simple-little-table.bond tbody")
            .eq(1)//skip bond summary
            .select("tr").stream()
            .skip(1)//skip table header
            .map(this::createBondPaymentSchedule)
            .collect(Collectors.toList());
    }

    private BondPaymentSchedule.BondPayment createBondPaymentSchedule(Element trEl) {
        List<String> data = trEl.select("td").stream().skip(1).map(tdEl -> tdEl.text().trim()).collect(Collectors.toList());
        BondPaymentSchedule.BondPayment result = new BondPaymentSchedule.BondPayment();
        result.setPaymentDate(LocalDate.parse(data.get(0), FORMATTER));
        result.setCouponValue(new MoneyAmount(Currency.RUB, new BigDecimal(data.get(1))));
        result.setAnnualInterest(percentToBD(data.get(2)));
        result.setNominalPart(percentToBD(data.get(3)));
        result.setMarketPart(percentToBD(data.get(4)));
        return result;
    }

    private static BigDecimal percentToBD(String percentValue) {
        return new BigDecimal(percentValue.replaceAll("%", "").trim());
    }
}
