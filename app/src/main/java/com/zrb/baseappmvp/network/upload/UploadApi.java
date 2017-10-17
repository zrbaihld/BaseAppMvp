package com.zrb.baseappmvp.network.upload;




import com.zrb.baseappmvp.constant.Constants;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Url;


/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 *  using this to upload
 */
public interface UploadApi {
    String end_point = Constants.Remote_BASE_END_POINT;

    @Multipart
    @POST
    Observable<ResponseBody> uploadFile(@Url String url, @PartMap Map<String, RequestBody> params);
}
