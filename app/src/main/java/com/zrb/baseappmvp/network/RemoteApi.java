package com.zrb.baseappmvp.network;



import com.zrb.baseappmvp.BuildConfig;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 * normal interface class of api to load String or post String ,replace {@link #end_point}
 * with your own base url
 * <p>
 * 常用API接口类用于加载和post 字符串操作，请在 {@link #end_point}中替换基地址
 */
public interface RemoteApi {
//        String end_point = "http://sandboxapi.jiaxiaoshu.cn";//not used
    String end_point = BuildConfig.baseUrl;

    @GET("{url}")
    Observable<ResponseBody> loadString(@Path(value = "url", encoded = true) String url);

//    @POST
//    @Multipart
//    Observable<ResponseBody> postString(@Url String url, @Part Map<String, String> params);
//
//    @POST
//    Observable<ResponseBody> postString(@Url String url,  @Body String params);

    @POST
    Observable<ResponseBody> postString(@Url String url, @Body RequestBody params);

//    @GET("getImage")
//    Observable<ResponseBody> getImageByUrl(@Part("url") String url);
}