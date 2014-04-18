package com.rusin.fileTest;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.roscopeco.ormdroid.Entity;
import com.rusin.fileTest.checksum.MD5Checksum;
import com.rusin.fileTest.jsonObject.AddFileRequest;
import com.rusin.fileTest.jsonObject.AddFileResponse;
import com.rusin.fileTest.model.FileUploadModel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.File;

public class ListUploadActivity extends Activity {
    private static final int REQUEST_CHOOSER = 112;
    private ArrayAdapter<FileUploadModel> mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_upload);
        ListView listView = (ListView)findViewById(R.id.listFile);
        mAdapter = new ArrayAdapter<FileUploadModel>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh();
    }

    private void refresh() {
        mAdapter.clear();
        for (FileUploadModel fileUpload : Entity.query(FileUploadModel.class).executeMulti()) {
            mAdapter.add(fileUpload);
        }
        mAdapter.notifyDataSetChanged();
    }
    public void chooseFileOnClick(View v){
        // Create the ACTION_GET_CONTENT Intent
        Intent getContentIntent = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(getContentIntent, "Select a file");
        startActivityForResult(intent, REQUEST_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSER:
                if (resultCode == RESULT_OK) {

                    final Uri uri = data.getData();

                    // Get the File path from the Uri
                    String path = FileUtils.getPath(this, uri);

                    // Alternatively, use FileUtils.getFile(Context, Uri)
                    if (path != null && FileUtils.isLocal(path)) {
                        File file = new File(path);
                        Toast.makeText(getApplicationContext(), "send file " + file.getName(), Toast.LENGTH_SHORT).show();
                        AddFileRequest addFileRequest = new AddFileRequest();
                        try {
                            addFileRequest.hash = MD5Checksum.getMD5Checksum(path);
                            addFileRequest.path = path;
                            addFileRequest.size = file.length();
                            addFileRequest.token = App.getToken();
                            App.getServer().addfile(addFileRequest, new FileAddCallback(addFileRequest));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    class FileAddCallback implements Callback<AddFileResponse> {

        private AddFileRequest addFileRequest;
        public FileAddCallback(AddFileRequest request) {
            addFileRequest = request;
        }

        @Override
        public void success(AddFileResponse addFileResponse, Response response) {
            FileUploadModel fileUploadModel = new FileUploadModel(addFileResponse.id, addFileRequest.path, addFileResponse.start_byte, 0, true);
            fileUploadModel.save();
            refresh();
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Button b = (Button)findViewById(R.id.button);
            b.setVisibility(View.VISIBLE);
        }
    }
}
