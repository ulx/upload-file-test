package com.rusin.fileTest.net;

import com.rusin.fileTest.jsonObject.*;
import retrofit.Callback;
import retrofit.http.*;
import retrofit.mime.TypedByteArray;

/**
 * Created by alexander on 16.04.14.
 */
public   interface FileUpload {
    @POST("/login")
    void login(@Body LoginRequest login, Callback<LoginResponse> cb);
    @POST("/addfile")
    void addfile(@Body AddFileRequest addfile, Callback<AddFileResponse> cb);
    @Headers({
            "Content Type​: multipart/form-data;charset=utf-8"
    })
    @Multipart
    @POST("/upload")
    UploadResponse updateUser(@Part("data") TypedByteArray file, @Part("info") @Body UploadRequest uploadFile, Callback<UploadResponse> cb);
}
