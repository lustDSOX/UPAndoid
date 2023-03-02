package com.example.up;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    TextInputLayout email;
    TextInputLayout password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }
    @SuppressLint("NotConstructor")
    public void Login(View v){
        if(email.getEditText().getText().toString().contains("@")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject postData = new JSONObject();
                        postData.put("email", email.getEditText().getText());
                        postData.put("password", password.getEditText().getText());

                        // создаем соединение
                        URL url = new URL("http://mskko2021.mad.hakta.pro/api/user/login");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");
                        // отправляем данные на сервер
                        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                        writer.write(postData.toString());
                        writer.flush();
                        // получаем ответ от сервера
                        StringBuilder sb = new StringBuilder();
                        int HttpResult = conn.getResponseCode();
                        if (HttpResult == HttpURLConnection.HTTP_OK) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                            String line = null;
                            while ((line = br.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            br.close();
                            JSONObject response = new JSONObject(sb.toString());
                            Log.d("response",response.toString());
                        } else {

                        }
                    }
                    catch (Exception e){
                        Log.d("e",e.toString());
                    }
                }
            }).start();
        }
    }

    public void GoReg(View v){
        //Intent intent = new Intent(Onboarding.this,Reg.class);
        // startActivity(intent);
    }
}