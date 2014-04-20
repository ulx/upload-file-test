package com.rusin.fileTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.rusin.fileTest.jsonObject.LoginRequest;
import com.rusin.fileTest.jsonObject.LoginResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickLigin(View v) {
        TextView login = (TextView)findViewById(R.id.editEmail);
        TextView password = (TextView)findViewById(R.id.editPass);
        LoginRequest request = new LoginRequest();
        request.email = login.getText().toString();
        request.password = password.getText().toString();
        App.getServer().login(request, new LoginCallback());
        v.setVisibility(View.INVISIBLE);
    }

    class LoginCallback implements Callback<LoginResponse> {

        @Override
        public void success(LoginResponse loginResponse, Response response) {
            App.setToken(loginResponse.token);
            Intent intent = new Intent(LoginActivity.this, ListUploadActivity.class);
            startActivity(intent);
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Button b = (Button)findViewById(R.id.button);
            b.setVisibility(View.VISIBLE);
        }
    }

}
