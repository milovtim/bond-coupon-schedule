package ru.milovtim.bonds.module;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.milovtim.bonds.Test;
import ru.milovtim.bonds.service.TinkofInvestAPIClient;

public class MainModule extends AbstractModule {

    private final TinkofInvestAPIClient apiClient;
    private ObjectMapper objectMapper;

    public MainModule() {
        this.objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.apiClient = createApiClient();
    }

    private static String getApiUrl(boolean sandbox) {
        Properties urlsProps = new Properties();
        try (InputStream res = Test.class.getResourceAsStream("/config.properties")) {
            if (res == null) {
                throw new IOException("Cannot find configuration file in classpath");
            }
            urlsProps.load(res);
        } catch (IOException ioe) {
            throw new IllegalStateException("Cannot load config with API urls", ioe);
        }
        return urlsProps.getProperty("ru.tinkoff.invest.openapi.host" + (sandbox ? "-sandbox" : "")) + "/";
    }

    private TinkofInvestAPIClient createApiClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request authHeader = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + args[0])
                    .build();
                return chain.proceed(authHeader);
            }).build();

        ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .client(httpClient)
            .baseUrl(getApiUrl(args.length > 1 && Boolean.parseBoolean(args[1])))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

        return retrofit.create(TinkofInvestAPIClient.class);
    }

    @Override
    protected void configure() {
        bind(ObjectMapper.class).toInstance(objectMapper);
        bind(TinkofInvestAPIClient.class).toInstance(apiClient);
    }
}
