package ru.milovtim.bonds.module;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

public class ModuleHelpers {
    public static final String USER_AGENT_HEADER_MOZILLA = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:73.0) " +
        "Gecko/20100101 Firefox/73.0";

    @NotNull
    public static OkHttpClient createOkHttpClientWithPermanentHeaders(Map<String, String> headers) {
        return new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request.Builder reqBuilder = chain.request().newBuilder();
                headers.forEach(reqBuilder::addHeader);
                return chain.proceed(reqBuilder.build());
            }).build();
    }
}
