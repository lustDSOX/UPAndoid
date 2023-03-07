package com.example.up;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ListView listView;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView image = findViewById(R.id.image);
        recyclerView = findViewById(R.id.my_recycler_view);
        listView = findViewById(R.id.QuotesList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // Установка ориентации горизонтальной
        recyclerView.setLayoutManager(layoutManager);
        String imageUrl = User.avatar;
        Picasso.get().load(imageUrl).into(image);
        TextView w_name = findViewById(R.id.welcome_name);
        w_name.setText("С возвращением, " + User.nickName);
        getFeelings();
        getQuotes();
    }

    public void getQuotes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // создаем соединение
                    URL url = new URL("http://mskko2021.mad.hakta.pro/api/quotes");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    int responseCode = conn.getResponseCode();
                    // Чтение ответа сервера
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        StringBuilder sb = new StringBuilder();
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        JSONObject response = new JSONObject(sb.toString());
                        // Обработка ответа сервера
                        List<Quote> quoteList = new ArrayList<Quote>();
                        JSONArray json_array = new JSONArray(response.getString("data"));
                        for (int i = 0; i < json_array.length(); i++) {
                            JSONObject json_object = json_array.getJSONObject(i);
                            Quote quote = new Quote();
                            quote.image = json_object.getString("image");
                            quote.title = json_object.getString("title");
                            quote.description = json_object.getString("description");
                            quoteList.add(quote);
                        }
                        //Заполняем список
                        LVAdapter adapter = new LVAdapter(MainActivity.this,quoteList);
                        listView.setAdapter(adapter);

                    } else {
                        Log.e("TAG", "Error: " + responseCode);
                    }

                    conn.disconnect();
                }
                catch (Exception e){
                    Log.d("e",e.toString());
                }
            }
        }).start();
    }

    public void getFeelings() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // создаем соединение
                    URL url = new URL("http://mskko2021.mad.hakta.pro/api/feelings");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    int responseCode = conn.getResponseCode();
                    // Чтение ответа сервера
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        StringBuilder sb = new StringBuilder();
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        JSONObject response = new JSONObject(sb.toString());
                        // Обработка ответа сервера
                        List<Feeling> feelingList = new ArrayList<Feeling>();
                        JSONArray json_array = new JSONArray(response.getString("data"));
                        for (int i = 0; i < json_array.length(); i++) {
                            JSONObject json_object = json_array.getJSONObject(i);
                            Feeling feeling = new Feeling();
                            feeling.image = json_object.getString("image");
                            feeling.title = json_object.getString("title");
                            feeling.position = json_object.getInt("position");
                            feelingList.add(feeling);
                        }
                        //Заполняем список
                        RVAdapter adapter = new RVAdapter(feelingList);
                        recyclerView.setAdapter(adapter);

                    } else {
                        Log.e("TAG", "Error: " + responseCode);
                    }

                    conn.disconnect();
                    }
                catch (Exception e){
                    Log.d("e",e.toString());
                }
            }
        }).start();
    }

    public void GoProfile(View v){
        Intent profile = new Intent(MainActivity.this,ProfileActivity.class);
        startActivity(profile);
    }

}