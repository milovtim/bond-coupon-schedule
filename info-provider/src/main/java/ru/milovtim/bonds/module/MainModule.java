package ru.milovtim.bonds.module;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import de.skuzzle.inject.conf.Resources;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.milovtim.bonds.Test;
import ru.milovtim.bonds.service.BondScrappingService;
import ru.milovtim.bonds.service.BondScrappingServiceImpl;
import ru.milovtim.bonds.service.TinkoffInvestAPIClient;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ApiAddressesConfig.class);
        bind(BondScrappingService.class).to(BondScrappingServiceImpl.class);
    }
}
