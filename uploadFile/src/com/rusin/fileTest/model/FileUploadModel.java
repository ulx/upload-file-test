package com.rusin.fileTest.model;

import com.roscopeco.ormdroid.Entity;

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


public FileUploadModel() {

}

    public FileUploadModel(int server_id, String path, long start_byte, long end_byte, boolean continue_send) {

        this.server_id = server_id;
        this.start_byte = start_byte;
        this.end_byte = end_byte;
        this.continue_send = continue_send;
        this.path = path;
    }

    public String toString() {
        return  "id - " + id
                + "id_server - " + server_id
                + " start_byte - " + start_byte
                + " end_byte - " + end_byte
                + " continue_send - " + continue_send
                + " path " + path;
    }
}
