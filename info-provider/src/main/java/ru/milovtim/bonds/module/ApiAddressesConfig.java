package ru.milovtim.bonds.module;

import javax.inject.Inject;
import java.util.Map;

import com.google.inject.name.Named;

public class ApiAddressesConfig {

    private final Map<String, String> resourceConfig;

    @Inject
    public ApiAddressesConfig(@Named("api-config") Map<String, String> resourceConfig) {
        this.resourceConfig = resourceConfig;
    }

    public String getHost() {
        return resourceConfig.get("ru.tinkoff.invest.openapi.host");
    }

    public String getHostSandbox() {
        return resourceConfig.get("ru.tinkoff.invest.openapi.host-sandbox");
    }

    public String getStreaming() {
        return resourceConfig.get("ru.tinkoff.invest.openapi.streaming");
    }

    public Integer getStreamingParallelism() {
        return Integer.parseInt(resourceConfig.get("ru.tinkoff.invest.openapi.streaming-parallelism"));
    }
}
