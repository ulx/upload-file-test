package com.rusin.fileTest.net;

import android.net.Uri;
import android.util.Log;
import com.google.gson.Gson;
import com.rusin.fileTest.jsonObject.LoginResponse;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import java.io.IOException;
import java.util.Collections;

/**
 * Created by alexander on 16.04.14.
 */
public class MockClient implements Client {

    @Override
    public Response execute(Request request) throws IOException {
        Uri uri = Uri.parse(request.getUrl());

        Log.d("MOCK SERVER", "fetching uri: " + uri.toString());

        String responseString = "";

        if(uri.getPath().equals("/login")) {
            responseString = "JSON STRING HERE LOGIN";
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.token = "61vLOLky6skRc7mnzK9dOZsRXINUxBZJ5I265YefcAQ1";
            Gson gson = new Gson();
            responseString = gson.toJson(loginResponse);
        } else {
            responseString = "OTHER JSON RESPONSE STRING";
        }

        //return new Response(request.getUrl(), 200, "nothing", Collections.EMPTY_LIST, new TypedByteArray("application/json", responseString.getBytes()));
        return new Response(request.getUrl(), 200, "nothing", Collections.EMPTY_LIST, new TypedByteArray("application/json", responseString.getBytes()));
    }
}
