package ru.milovtim.bonds.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SmartLabHttpClient {

    @GET("q/bonds/{isin}/")
    Observable<ResponseBody> getBondPage(@Path("isin") String isin);
}
