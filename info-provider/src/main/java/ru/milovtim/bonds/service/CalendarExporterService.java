package ru.milovtim.bonds.service;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.*;
import lombok.val;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import ru.tinkoff.invest.openapi.models.portfolio.InstrumentType;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio.PortfolioPosition;

public class CalendarExporterService {

    private final TinkoffInvestService tinkoffInvestService;
    private final BondScrappingService bondScrappingService;

    @Inject
    public CalendarExporterService(TinkoffInvestService tinkoffInvestService, BondScrappingService bondScrappingService) {
        this.tinkoffInvestService = tinkoffInvestService;
        this.bondScrappingService = bondScrappingService;
    }

    private Calendar createICalendar() {
        val cal = new Calendar();
        cal.getProperties().add(new ProdId("-//Mixed Forest LTD//Bonds schedule exporter//RU"));
        cal.getProperties().add(Version.VERSION_2_0);
        return cal;
    }

    private void addEvent(Calendar cal, LocalDate localDate, List<PortfolioPosition> payments) {
        val zdtStart = ZonedDateTime.of(localDate, LocalTime.ofSecondOfDay(0), ZoneId.of("Europe/Moscow"));
        val evt = new VEvent();
        evt.getProperties().add(new DtStart(new Date(zdtStart.toInstant().toEpochMilli()), false));
        evt.getProperties().add(new Uid(UUID.randomUUID().toString()));
        evt.getProperties().add(new Summary("Bond payment in: " + calculateSummaryPayment(payments)));
        evt.getProperties().add(new Description("This is the test event description that may contain details about bond coupons payments"));
        cal.getComponents().add(evt);
    }

    private String calculateSummaryPayment(List<PortfolioPosition> payments) {

        return null;
    }

    @SuppressWarnings("UnstableApiUsage")
    public void exportBondScheduleToStream(OutputStream outputStream) {
        Map<String, Portfolio> portfolioByAccount = tinkoffInvestService.getPortfolioByAccount();
        ListMultimap<LocalDate, PortfolioPosition> result = MultimapBuilder.treeKeys().arrayListValues().build();
        ImmutableList.of(true, false).forEach(isISS -> {
            Optional<List<PortfolioPosition>> iisPositions = getAccBonds(portfolioByAccount, isISS);
            iisPositions.ifPresent(positions -> {
                val iisSchedule = calculatePaymentSchedule(positions);
                result.putAll(iisSchedule);
            });
        });

        Calendar iCalendar = createICalendar();
        result.keySet().stream().sorted().forEach(paymentDate -> {
            val payments = result.get(paymentDate);
            addEvent(iCalendar, paymentDate, payments);
        });
    }

    /**
     * todo implement this
     */
    @Nonnull
    private Multimap<LocalDate, PortfolioPosition> calculatePaymentSchedule(List<PortfolioPosition> iisPositions) {
        return Multimaps.newListMultimap(Collections.emptyMap(), ArrayList::new);
    }

    private Optional<List<PortfolioPosition>> getAccBonds(Map<String, Portfolio> portfolioByAccount, boolean iis) {
        Predicate<String> iisFilter = acc -> acc.toLowerCase().contains("iis");
        if (!iis) {
            iisFilter = iisFilter.negate();
        }
        String accName = portfolioByAccount.keySet().stream()
            .filter(iisFilter)
            .findFirst().orElse(null);
        Portfolio portfolio = portfolioByAccount.get(accName);
        if (hasBonds(portfolio)) {
            List<PortfolioPosition> res = portfolio.positions.stream()
                .filter(pos -> pos.instrumentType == InstrumentType.Bond)
                .collect(Collectors.toList());
            return Optional.of(res);
        }
        return Optional.empty();
    }

    private boolean hasBonds(Portfolio portfolio) {
        return portfolio != null && !portfolio.positions.isEmpty() &&
            portfolio.positions.stream().anyMatch(pos -> pos.instrumentType == InstrumentType.Bond);
    }
}
