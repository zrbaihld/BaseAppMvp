package com.zrb.baseappmvp.network;

import android.content.Context;
import android.content.Intent;

import com.zrb.baseappmvp.tools.ExceptionUtil;
import com.zrb.baseappmvp.tools.ExceptionUtil.JXSAuthorityException;
import com.zrb.baseappmvp.tools.ExceptionUtil.LoginOuttimeException;
import com.zrb.baseappmvp.tools.ExceptionUtil.RemoteLoginException;
import com.zrb.baseappmvp.constant.Constants;
import com.zrb.baseappmvp.network.download.DownloadService;
import com.zrb.baseappmvp.network.upload.UploadParam;
import com.zrb.baseappmvp.network.upload.UploadService;
import com.zrb.baseappmvp.tools.FileUtil;
import com.zrb.baseappmvp.tools.LogUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Anthony on 2016/6/12.
 * Class Note:
 * data entrance of all kinds of data
 * using {@link HttpHelper},{@link PreferencesHelper},
 * and {@link RxBus} to access data
 * <p>
 * 所有数据的入口类
 */
public class BaseDataRepository {


    HttpHelper mHttpHelper;


    PreferencesHelper mPreferencesHelper;


    protected Context mContext;


    /**
     * using constructor class to generate other classes
     *
     * @param context
     */
    public BaseDataRepository(Context context) {
        this.mContext = context;
        mHttpHelper = new HttpHelper(mContext);
        mPreferencesHelper = new PreferencesHelper(mContext);
    }


    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public HttpHelper getHttpHelper() {
        return mHttpHelper;
    }


    /**
     * load String data ,support data from local and  online
     */
    public Observable<String> loadString(String url) {
        if (url.startsWith(Constants.LOCAL_FILE_BASE_END_POINT)) {
            try {
                String s = FileUtil.getString(mContext, url);
                return Observable.just(s);
            } catch (IOException e) {
                e.printStackTrace();
                throw Exceptions.propagate(e);
            }
        } else {
            String path = url.substring(Constants.Remote_BASE_END_POINT.length());
            return mHttpHelper.getApi(RemoteApi.class)
                    .loadString(path)
                    .flatMap(
                            new Function<ResponseBody, ObservableSource<String>>() {
                                @Override
                                public ObservableSource<String> apply(ResponseBody responseBody) throws Exception {
                                    try {
                                        String result = responseBody.string();
                                        return Observable.just(result);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        throw new RuntimeException("IOException when convert Response Body to String");
                                    }
                                }
                            }
                    );
        }
    }


    /**
     * post string to server
     */
    public Observable<String> postString(String url, RequestBody paramMap) {
        LogUtil.e("postString ");
        return mHttpHelper.getApi(RemoteApi.class)
                .postString(url, paramMap)
                .flatMap(new Function<ResponseBody, Observable<String>>() {
                    @Override
                    public Observable<String> apply(ResponseBody responseBody) throws Exception {
                        try {
                            String result = responseBody.string();
                            LogUtil.e("result " + result);
                            JSONObject jsonObject = new JSONObject(result);


                            if ("200".equals(jsonObject.getString("state"))) {
                                return Observable.just(jsonObject.getString("data").
                                        replaceAll("null", "\"\""));
                            } else if ("90005".equals(jsonObject.getString("state")) || "90006".equals(jsonObject.getString("state"))) {
                                RemoteLoginException login = new RemoteLoginException();
                                return Observable.error(login);
                            }else if ("110008".equals(jsonObject.getString("state"))) {
                                return Observable.error(new JXSAuthorityException());
                            } else {
                                LoginOuttimeException login = new LoginOuttimeException();
                                login.setMeg(jsonObject.getString("msg"));
                                return Observable.error(login);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            return Observable.error(new ExceptionUtil.HttpErrorException());
//                            throw new RuntimeException("IOException when convert Response Body to String");
                        }
                    }
                });
    }
//    /**
//     * post getImageByUrl to server
//     */
//    public Observable<String> getImageByUrl(String url) {
//        LogUtil.e("postString ");
//        return mHttpHelper.getApi(RemoteApi.class)
//                .getImageByUrl(url)
//                .flatMap(new Function<ResponseBody, Observable<String>>() {
//                    @Override
//                    public Observable<String> apply(ResponseBody responseBody) throws Exception {
//                        try {
//                            Base64InputStream encoder = new BASE64Encoder();
//                            String result = responseBody.byteStream();
//                            LogUtil.e("result " + result);
//                            JSONObject jsonObject=new JSONObject(result);
//
//
//                            if ("200".equals(jsonObject.getString("state"))) {
//                                return  Observable.just(jsonObject.getString("data").
//                                        replaceAll("null","\"\""));
//                            } else {
//                                LoginOuttimeException login= new LoginOuttimeException();
//                                login.setMeg(jsonObject.getString("msg"));
//                                return Observable.error(login);
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            return Observable.error(new ExceptionUtil.HttpErrorException());
////                            throw new RuntimeException("IOException when convert Response Body to String");
//                        }
//                    }
//                });
//    }
//    /**
//     * post string to server
//     */
//    public DisposableObserver<String> postString(String url, RequestBody paramMap) {
//        LogUtil.e("postString ");
//        return mHttpHelper.getApi(RemoteApi.class)
//                .postString(url, paramMap)
//                .flatMap(new Function<ResponseBody, DisposableObserver<String>>() {
//                    @Override
//                    public DisposableObserver<String> apply(ResponseBody responseBody) throws Exception {
//                        try {
//                            if (responseBody==null)
//                                LogUtil.e("responseBody==null ");
//                            String result = responseBody.string();
//                            LogUtil.e("result " + result);
//                            Gson gson = new Gson();
//                            BaseBean bb = gson.fromJson(result, BaseBean.class);
//                            if ("200".equals(bb.getState())) {
//                                return  Observable.just(bb.getData());
//                            } else {
//                                return Observable.error(new ExceptionUtil.LoginOuttimeException());
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            return Observable.error(new ExceptionUtil.HttpErrorException());
////                            throw new RuntimeException("IOException when convert Response Body to String");
//                        }
//                    }
//                });
//    }

    /**
     * 通过GET方式下载远程文件，支持大文件下载
     * 想要获知下载进度事件，请订阅{@link DownloadEvent}
     * 想要获知下载完成事件，请订阅{@link DownloadFinishEvent}
     */
    public void downloadFile(Context ctx, final String url) {
        Intent startIntent = new Intent(ctx, DownloadService.class);
        startIntent.putExtra(DownloadService.DOWNLOAD_URL, url);
        ctx.startService(startIntent);
    }

    /**
     * 通过POST方式上传文件，支持多文件上传
     * 想要获知上传进度事件，请订阅{@link UploadEvent}
     * 想要获知上传完成事件，请订阅{@link UploadFinishEvent}
     */
    public void uploadFile(Context ctx, String url, ArrayList<UploadParam> fileList, ArrayList<UploadParam> paramList) {
        Intent startIntent = new Intent(ctx, UploadService.class);
        startIntent.putExtra(UploadService.UPLOAD_URL, url);
        startIntent.putParcelableArrayListExtra(UploadService.UPLOAD_FILES, fileList);
        if (paramList != null) {
            startIntent.putParcelableArrayListExtra(UploadService.UPLOAD_PARAMS, paramList);
        }
        ctx.startService(startIntent);
    }

    public void uploadFile(Context ctx, String url, ArrayList<UploadParam> fileList) {
        uploadFile(ctx, url, fileList, null);
    }


}