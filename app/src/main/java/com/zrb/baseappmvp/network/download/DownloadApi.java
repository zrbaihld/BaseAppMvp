package com.zrb.baseappmvp.network.download;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Anthony on 2016/6/12.
 * Class Note:
 * download API
 */
public interface DownloadApi {
//    String end_point =  BuildConfig.baseUrl;
    String end_point = "http://angle.so/";

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);
}
