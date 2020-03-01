package ru.milovtim.bonds.module;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import de.skuzzle.inject.conf.Resources;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.milovtim.bonds.module.config.ApiAddressesConfig;
import ru.milovtim.bonds.service.TinkoffInvestAPIClient;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static com.google.common.net.HttpHeaders.USER_AGENT;


public class TinkoffApiClientModule extends AbstractModule {
    private final String token;
    private final boolean sandbox;

    public TinkoffApiClientModule(String token, boolean sandbox) {
        this.token = token;
        this.sandbox = sandbox;
    }

    @Provides
    private TinkoffInvestAPIClient createTinkoffApiClient(ApiAddressesConfig config, ObjectMapper objectMapper) {
        OkHttpClient httpClient = ModuleHelpers.createOkHttpClientWithPermanentHeaders(ImmutableMap.of(
            AUTHORIZATION, token,
            USER_AGENT, ModuleHelpers.USER_AGENT_HEADER_MOZILLA));
        Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .client(httpClient)
            .baseUrl(sandbox ? config.getHostSandbox() : config.getHost())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
        return retrofit.create(TinkoffInvestAPIClient.class);
    }

    @Override
    protected void configure() {
        bind(ObjectMapper.class).toProvider(() -> new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false));
        Resources.bind()
            .classPathResource("config.properties")
            .encodedWith(StandardCharsets.UTF_8)
            .containingProperties()
            .to(Key.get(new TypeLiteral<Map<String, String>>() {}, Names.named("api-config")))
            .using(binder());
    }
}
