package com.rusin.fileTest.net;

import com.rusin.fileTest.jsonObject.*;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.*;
import retrofit.mime.TypedByteArray;

import java.util.List;


public class ServerClient {
    private static final String API_URL = "https://api.filesend.com";
    static class Contributor {
        String login;
        int contributions;
    }

    interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        List<Contributor> contributors(
                @Path("owner") String owner,
                @Path("repo") String repo
        );
    }

    interface FileUpload {
        @POST("/login")
        void login(@Body LoginRequest login, Callback<LoginResponse> cb);
        @POST("/addfile")
        void addfile(@Body AddFileRequest addfile, Callback<AddFileResponse> cb);
        @Headers({
                "Content Type​: multipart/form-data;charset=utf-8"
        })
//        @POST("upload")
//        void upload(@Body UploadRequest uploadFile, Callback<UploadResponse> cd);
        @Multipart
        @POST("/upload")
        UploadResponse updateUser(@Part("data") TypedByteArray file, @Part("info") @Body UploadRequest uploadFile);
    }


    public static void main(String... args) {
        // Create a very simple REST adapter which points the GitHub API endpoint.
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();

        // Create an instance of our GitHub API interface.
        GitHub github = restAdapter.create(GitHub.class);

        // Fetch and print a list of the contributors to this library.
        List<Contributor> contributors = github.contributors("square", "retrofit");
        for (Contributor contributor : contributors) {
            System.out.println(contributor.login + " (" + contributor.contributions + ")");
        }
    }
}
