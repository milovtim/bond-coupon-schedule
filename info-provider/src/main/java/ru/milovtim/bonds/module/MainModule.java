package ru.milovtim.bonds.module;

import com.google.inject.AbstractModule;
import ru.milovtim.bonds.module.config.ApiAddressesConfig;
import ru.milovtim.bonds.service.BondScrappingService;
import ru.milovtim.bonds.service.BondScrappingServiceImpl;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ApiAddressesConfig.class);
        bind(BondScrappingService.class).to(BondScrappingServiceImpl.class);
    }
}
