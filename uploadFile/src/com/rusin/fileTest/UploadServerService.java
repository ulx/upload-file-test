package com.rusin.fileTest;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.rusin.fileTest.jsonObject.UploadRequest;
import com.rusin.fileTest.jsonObject.UploadResponse;
import com.rusin.fileTest.model.FileUploadModel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alexander on 19.04.14.
 */
public class UploadServerService extends Service {
    private static final String LOG_TAG = "service_upload_file";
    private final static int COUNT_TREAD = 5;
    private final static int COUNT_READ_BYTE = 200;
    private ExecutorService es;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
        es = Executors.newFixedThreadPool(COUNT_TREAD);
        sendOldFile();
    }

    private void sendOldFile() {
        List<FileUploadModel> list = FileUploadModel.getUploadFile(false);
        for (FileUploadModel item : list) {
            UploadFileRun mr = new UploadFileRun(item.server_id);
            es.execute(mr);
        }

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service onStartCommand", Toast.LENGTH_SHORT).show();
        int idFile = intent.getIntExtra("id", -1);
        UploadFileRun mr = new UploadFileRun(idFile);
        es.execute(mr);
        return super.onStartCommand(intent, flags, startId);

    }

    class UploadFileRun implements Runnable {


        private static final String LOG_TAG = "upload_file";
        private final PartCallback mPartCallback;
        private final UploadRequest mRequest;
        private final File mFileSend;
        int idFile;
        private FileUploadModel fileUploadModel;

        public UploadFileRun(int idFile) {
            this.idFile = idFile;
            Log.d(LOG_TAG, "MyRun#" + idFile + " file");
            fileUploadModel = FileUploadModel.getFileUpload(idFile);
            mPartCallback = new PartCallback();
            mPartCallback.setFileUploadModel(fileUploadModel);
            mRequest = new UploadRequest();
            mPartCallback.setRequest(mRequest);
            mFileSend = new File(fileUploadModel.path);
        }

        public void run() {

            Log.d(LOG_TAG, "MyRun#" + idFile + " start");
            send();

        }

        private void send() {
            try {
                byte [] content = readFileSegment();
                TypedByteArray fileByte = new TypedByteArray("multipart/form-data;charset=utf-8", content);
                mRequest.id = fileUploadModel.server_id;
                mRequest.start_byte = fileUploadModel.start_byte;
                mRequest.end_byte = fileUploadModel.start_byte + (int)getCoutnSendByte();
                mRequest.token = App.getToken();
                sendPart(fileByte);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private long getCoutnSendByte() {
            return (fileUploadModel.start_byte + COUNT_READ_BYTE) > mFileSend.length() ? mFileSend.length() - fileUploadModel.start_byte : COUNT_READ_BYTE;
        }

        private void sendPart(TypedByteArray file) {
            mPartCallback.setRequest(mRequest);
            App.getServer().updateUser(file, mRequest, mPartCallback);
        }

        void stop() {
            Log.d(LOG_TAG, "MyRun#" + idFile + " end, stopSelf(" + idFile + ")");
            stopSelf(idFile);
        }

        public byte[] readFileSegment() throws IOException{
            int index = (int)fileUploadModel.start_byte;
            int count = (int)getCoutnSendByte();

            RandomAccessFile raf = new RandomAccessFile(mFileSend, "r");
            byte[] buffer = new byte[count];
            try {
                raf.skipBytes(index);
                raf.readFully(buffer, 0, count);
                return buffer;
            } finally {
                raf.close();
            }
        }

        class PartCallback implements Callback<UploadResponse> {

            private FileUploadModel fileUploadModel;
            private UploadRequest request;

            @Override
            public void success(UploadResponse uploadResponse, Response response) {

                if (uploadResponse.continueStatus) {
                    fileUploadModel.continue_send = true;
                    fileUploadModel.start_byte = request.end_byte;
                    fileUploadModel.save();
                    UploadFileRun.this.send();
                } else {
                    fileUploadModel.continue_send = false;
                    fileUploadModel.start_byte = request.end_byte;
                    fileUploadModel.save();
                    UploadFileRun.this.stop();
                }


            }

            @Override
            public void failure(RetrofitError retrofitError) {
                UploadFileRun.this.stop();
            }

            public void setFileUploadModel(FileUploadModel fileUploadModel) {
                this.fileUploadModel = fileUploadModel;
            }

            public void setRequest(UploadRequest request) {
                this.request = request;
            }
        }
    }

}
