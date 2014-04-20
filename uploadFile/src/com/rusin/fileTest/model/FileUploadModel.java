package com.rusin.fileTest.model;

import com.roscopeco.ormdroid.Entity;

import java.util.List;

import static com.roscopeco.ormdroid.Query.eql;

/**
 * Created by alexander on 17.04.14.
 */
public class FileUploadModel extends Entity {
    public int id;
    public int server_id;
    public String path;
    public long start_byte;
    public long end_byte;
    public boolean continue_send;
public  long lenght;

public FileUploadModel() {

}
    public static FileUploadModel getFileUpload(int server_id) {
        FileUploadModel fileUpload = Entity.query(FileUploadModel.class).where(eql("server_id", server_id)).execute();
        return fileUpload;
    }

    public static List<FileUploadModel> getUploadFile(boolean isContinueSend) {
       List<FileUploadModel> list = Entity.query(FileUploadModel.class).where(eql("continue_send", isContinueSend)).executeMulti();
        return list;
    }

    public FileUploadModel(int server_id, String path, long lenght, long start_byte, long end_byte, boolean continue_send) {
        this.server_id = server_id;
        this.start_byte = start_byte;
        this.end_byte = end_byte;
        this.continue_send = continue_send;
        this.path = path;
        this.lenght = lenght;
    }

    public String toString() {
        return  "id - " + id
                + " id_server - " + server_id
                + " lenght - " + lenght
                + " start_byte - " + start_byte
                + " end_byte - " + end_byte
                + " continue_send - " + continue_send
                + " path " + path;

    }
}
