package ru.milovtim.bonds.module;

import javax.inject.Named;

import java.nio.charset.StandardCharsets;
import java.util.Map;

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
import ru.milovtim.bonds.service.SmartLabHttpClient;
import ru.milovtim.bonds.service.TinkoffInvestAPIClient;


public class HttpClientModule extends AbstractModule {
    private final String token;
    private final boolean sandbox;
    private final ObjectMapper objectMapper;

    public HttpClientModule(String token, boolean sandbox) {
        this.token = token;
        this.sandbox = sandbox;
        objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Provides
    private TinkoffInvestAPIClient createTinkoffApiClient(ApiAddressesConfig config, ObjectMapper objectMapper) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request authHeader = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
                return chain.proceed(authHeader);
            }).build();

        Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .client(httpClient)
            .baseUrl(sandbox ? config.getHostSandbox() : config.getHost())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

        return retrofit.create(TinkoffInvestAPIClient.class);
    }

    @Provides
    private SmartLabHttpClient createSmartlabApiClient(ThirdPartyConfig config) {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl(config.getSmartlabHost())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

        return retrofit.create(SmartLabHttpClient.class);
    }

    @Override
    protected void configure() {
        bind(ObjectMapper.class).toInstance(objectMapper);
        Resources.bind()
            .classPathResource("config.properties")
            .encodedWith(StandardCharsets.UTF_8)
            .containingProperties()
            .to(Key.get(new TypeLiteral<Map<String, String>>() {}, Names.named("api-config")))
            .using(binder());

        Resources.bind()
            .classPathResource("thirdparty-service-addresses.properties")
            .encodedWith(StandardCharsets.UTF_8)
            .containingProperties()
            .to(ThirdPartyConfig.class)
            .using(binder());
    }
}
