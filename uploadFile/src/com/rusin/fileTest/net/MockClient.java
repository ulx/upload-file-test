package com.rusin.fileTest.net;

import android.net.Uri;
import android.util.Log;
import com.google.gson.Gson;
import com.rusin.fileTest.App;
import com.rusin.fileTest.jsonObject.AddFileResponse;
import com.rusin.fileTest.jsonObject.LoginResponse;
import com.rusin.fileTest.jsonObject.UploadResponse;
import retrofit.client.ApacheClient;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import retrofit.mime.TypedString;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Random;

/**
 * Created by alexander on 16.04.14.
 */
public class MockClient implements Client {

    @Override
    public Response execute(Request request) throws IOException {
        Uri uri = Uri.parse(request.getUrl());

        Log.d("MOCK SERVER", "fetching uri: " + uri.toString());

        String responseString = "";
        Gson gson = new Gson();
        if (uri.getPath().equals("/login")) {
            responseString = "JSON STRING HERE LOGIN";
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.token = "61vLOLky6skRc7mnzK9dOZsRXINUxBZJ5I265YefcAQ1";

            responseString = gson.toJson(loginResponse);
        } else if (uri.getPath().equals("/addfile")) {
            Log.d("MOCK SERVER", "request: " + request.getBody());
            AddFileResponse addFileResponse = new AddFileResponse();
            addFileResponse.id = new Random().nextInt(1000);
            addFileResponse.start_byte = 0;
            responseString = gson.toJson(addFileResponse);

        } else if (uri.getPath().equals("/upload")) {
            Log.d("MOCK SERVER", "request: " + request.toString());

            UploadResponse uploadResponse = new UploadResponse();
            uploadResponse.continueStatus = false;
            uploadResponse.hash = "test";
            responseString = gson.toJson(uploadResponse);

        }
        else {
            responseString = "OTHER JSON RESPONSE STRING";
        }


        //return new Response(request.getUrl(), 200, "nothing", Collections.EMPTY_LIST, new TypedByteArray("application/json", responseString.getBytes()));
        return new Response(request.getUrl(), 200, "nothing", Collections.EMPTY_LIST, new TypedByteArray("application/json", responseString.getBytes()));
    }
}
