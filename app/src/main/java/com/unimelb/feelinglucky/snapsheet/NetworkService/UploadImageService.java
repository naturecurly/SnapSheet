package com.unimelb.feelinglucky.snapsheet.NetworkService;

import com.unimelb.feelinglucky.snapsheet.Bean.ReturnMessage;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by leveyleonhardt on 10/4/16.
 */

public interface UploadImageService {
    @Multipart
    @POST("images/upload")
    Call<ReturnMessage> uploadImage(@Part("image") MultipartBody.Part image, @Part("description") RequestBody description);
}
