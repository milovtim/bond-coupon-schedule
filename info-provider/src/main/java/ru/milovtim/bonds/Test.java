package ru.milovtim.bonds;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.cli.*;
import ru.milovtim.bonds.module.MainModule;
import ru.milovtim.bonds.module.TinkoffApiClientModule;
import ru.milovtim.bonds.pojo.BondPaymentSchedule;
import ru.milovtim.bonds.service.BondScrappingService;

public class Test {
    static final CommandLineParser CLI_PARSER;
    static final Options CLI_PARSER_OPTS;
    static {
        CLI_PARSER = new DefaultParser();
        CLI_PARSER_OPTS = new Options()
            .addOption(Option.builder("t")
                .longOpt("token")
                .hasArg(true)
                .longOpt("Tinkoff API token (generated in settings page)")
                .required(true).build())
            ;
    }


    public static void main(String[] args) {
        try {
            CLI_PARSER.parse(CLI_PARSER_OPTS, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean sandbox = (args.length > 1) && Boolean.parseBoolean(args[1]);

        Injector injector = Guice.createInjector(new TinkoffApiClientModule(args[0], sandbox), new MainModule());

        BondScrappingService scrappingService = injector.getInstance(BondScrappingService.class);
        BondPaymentSchedule schedule = scrappingService.getBondSchedule("RU000A0JWXQ7");
        System.out.println(schedule);
    }
}
