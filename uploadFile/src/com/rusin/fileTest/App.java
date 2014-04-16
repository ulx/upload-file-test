package com.rusin.fileTest;

import android.app.Application;
import com.rusin.fileTest.net.FileUpload;
import com.rusin.fileTest.net.MockClient;
import com.rusin.fileTest.net.ServerClient;
import retrofit.RestAdapter;


public class App extends Application {
    private static FileUpload fileSever;
    private static String token;

    public static FileUpload getServer() {
        if (fileSever == null) {
            // Create a very simple REST adapter which points the GitHub API endpoint.
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(ServerClient.API_URL)
                    .setClient(new MockClient())
                    .build();
            // Create an instance of our GitHub API interface.
            fileSever = restAdapter.create(FileUpload.class);
        }
       return fileSever;
    }

    public static void setToken(String token) {
        App.token = token;
    }
}
