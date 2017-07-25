package com.example.junhee.servernodejs;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by JunHee on 2017. 7. 25..
 */


public interface IBbs {
    // server 끝에 '/' 꼭 넣어준다.
    public static final String SERVER = "http://172.30.1.34:1111/";

    @GET("bbs")
    public Observable<ResponseBody> read();

                    // 파라미터로 String or RequestBody를 넘길 수 있다.
    @POST("bbs")
    public Observable<ResponseBody> write(@Body RequestBody bbs);

    @PUT("bbs")
    public void update(Bbs bbs);

    @DELETE("bbs")
    public void delete(Bbs bbs);
}

