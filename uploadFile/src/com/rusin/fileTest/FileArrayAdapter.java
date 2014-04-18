package com.rusin.fileTest;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.rusin.fileTest.model.FileUploadModel;

/**
 * Created by alexander on 18.04.14.
 */
public class FileArrayAdapter extends ArrayAdapter<FileUploadModel> {
    public FileArrayAdapter(Context context, int resource) {
        super(context, resource);
    }
}
