package com.rusin.fileTest.jsonObject;


import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    public String hash;
    @SerializedName("continue")
    public boolean continueStatus;
}
