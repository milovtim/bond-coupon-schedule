package ru.milovtim.bonds;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.milovtim.bonds.pojo.UserAccounts;
import ru.milovtim.bonds.service.TinkofInvestAPIClient;
import ru.tinkoff.invest.openapi.data.OpenApiResponse;

public class Test {

    private static String getApiUrl(boolean sandbox) {
        Properties urlsProps = new Properties();
        try (InputStream res = Test.class.getResourceAsStream("/config.properties")) {
            urlsProps.load(res);
        } catch (IOException ioe) {
            throw new IllegalStateException("Cannot load config with API urls", ioe);
        }
        return urlsProps.getProperty("openapi.host" + (sandbox? "-sandbox": "")) + "/";
    }


    public static void main(String[] args) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request authHeader = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + args[0])
                    .build();
                return chain.proceed(authHeader);
            }).build();


        Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create())
            .client(httpClient)
            .baseUrl(getApiUrl(args.length > 1 && Boolean.parseBoolean(args[1])))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

        TinkofInvestAPIClient apiClient = retrofit.create(TinkofInvestAPIClient.class);
        Call<OpenApiResponse<UserAccounts>> userAccountsCall = apiClient.getUserAccounts();
        try {
            Response<OpenApiResponse<UserAccounts>> resp = userAccountsCall.execute();
            OpenApiResponse<UserAccounts> respBody = resp.body();
            if (respBody != null && "Ok".equalsIgnoreCase(respBody.status)) {
                System.out.println(respBody.payload);
            } else {
                System.out.println(respBody.status);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*Document doc = Jsoup.connect("https://smart-lab.ru/q/bonds/RU000A0JWXQ7/").get();
        List<BondCoupon> coupons = doc.select(".simple-little-table.bond tbody tr").stream()
            .skip(1)//skip table header
            .map(element -> new BondCouponImpl(new BondCouponHtmlTableAdapter(element)))
            .collect(Collectors.toList());
        System.out.println(coupons);*/
    }
}
