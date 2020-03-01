package ru.milovtim.bonds.module;

import java.nio.charset.StandardCharsets;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import de.skuzzle.inject.conf.Resources;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import ru.milovtim.bonds.module.config.ThirdPartyConfig;
import ru.milovtim.bonds.service.SmartLabHttpClient;

import static com.google.common.net.HttpHeaders.USER_AGENT;

public class WebScrappingModule extends AbstractModule {

    @Provides
    private SmartLabHttpClient createSmartlabApiClient(ThirdPartyConfig config) {
        OkHttpClient httpClient = ModuleHelpers.createOkHttpClientWithPermanentHeaders(ImmutableMap.of(
            USER_AGENT, ModuleHelpers.USER_AGENT_HEADER_MOZILLA));

        Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl(config.getSmartlabHost())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

        return retrofit.create(SmartLabHttpClient.class);
    }

    @Override
    protected void configure() {
        Resources.bind()
            .classPathResource("thirdparty-service-addresses.properties")
            .encodedWith(StandardCharsets.UTF_8)
            .containingProperties()
            .to(ThirdPartyConfig.class)
            .using(binder());
    }
}
