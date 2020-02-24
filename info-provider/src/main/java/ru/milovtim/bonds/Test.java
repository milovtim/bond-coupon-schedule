package ru.milovtim.bonds;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.milovtim.bonds.module.HttpClientModule;
import ru.milovtim.bonds.module.MainModule;
import ru.milovtim.bonds.pojo.BondPaymentSchedule;
import ru.milovtim.bonds.service.BondScrappingService;
import ru.milovtim.bonds.service.TinkoffInvestService;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;

public class Test {

    public static void main(String[] args) {
        Preconditions.checkArgument(args.length > 0, "Token must be provided");
        boolean sandbox = (args.length > 1) && Boolean.parseBoolean(args[1]);

        Injector injector = Guice.createInjector(new HttpClientModule(args[0], sandbox), new MainModule());


        BondScrappingService scrappingService = injector.getInstance(BondScrappingService.class);
        BondPaymentSchedule schedule = scrappingService.getBondSchedule("RU000A0JWXQ7");
        System.out.println(schedule);

//        TinkoffInvestService investService = injector.getInstance(TinkoffInvestService.class);
//        Map<String, Portfolio> portByAcc = investService.getPortfolioByAccount();
//        portByAcc.forEach((type, port) -> {
//            System.out.println(type);
//        });
    }
}
