package com.unimelb.feelinglucky.snapsheet.NetworkService;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by asahui on 16/10/2016.
 */

public interface FileDownloadService {
    @GET("/public/")
    Call<ResponseBody> downloadFileWithFixedUrl();


    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
}
