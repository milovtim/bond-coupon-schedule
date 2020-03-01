package ru.milovtim.bonds;

import com.google.common.base.Preconditions;
import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.milovtim.bonds.module.TinkoffApiClientModule;
import ru.milovtim.bonds.module.MainModule;
import ru.milovtim.bonds.pojo.BondPaymentSchedule;
import ru.milovtim.bonds.service.BondScrappingService;

public class Test {

    public static void main(String[] args) {
        Preconditions.checkArgument(args.length > 0, "Token must be provided");
        boolean sandbox = (args.length > 1) && Boolean.parseBoolean(args[1]);

        Injector injector = Guice.createInjector(new TinkoffApiClientModule(args[0], sandbox), new MainModule());

        BondScrappingService scrappingService = injector.getInstance(BondScrappingService.class);
        BondPaymentSchedule schedule = scrappingService.getBondSchedule("RU000A0JWXQ7");
        System.out.println(schedule);
    }
}
