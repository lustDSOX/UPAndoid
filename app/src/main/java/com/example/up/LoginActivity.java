package com.example.up;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {

    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLayout = findViewById(R.id.email);
        passwordLayout = findViewById(R.id.password);
    }
    @SuppressLint("NotConstructor")
    public void Login(View v){
        if(emailLayout.getEditText().getText().toString().contains("@")){
           getUser(emailLayout.getEditText().getText().toString(),passwordLayout.getEditText().getText().toString());
        }
    }

    void getUser(String email,String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject postData = new JSONObject();
                    postData.put("email", email);
                    postData.put("password", password);

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

                        // Получаем объект SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        // Получаем объект Editor для редактирования SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        // Сохраняем данные о пользователе
                        editor.putString("password", String.valueOf(password));
                        editor.putString("email", String.valueOf(email));
                        // Применяем изменения
                        editor.apply();

                        User.Id = response.getString("id");
                        User.avatar = response.getString("avatar");
                        User.nickName = response.getString("nickName");
                        User.email = response.getString("email");
                        User.token = response.getString("token");
                        Log.d("response",response.toString());

                        Intent main = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(main);
                    } else {
                        Toast.makeText(LoginActivity.this, "invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Log.d("e",e.toString());
                }
            }
        }).start();
    }

    public void GoReg(View v){
        //Intent intent = new Intent(Onboarding.this,Reg.class);
        // startActivity(intent);
    }
}