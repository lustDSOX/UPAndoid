package com.example.up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class PhotoActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetector gestureDetector;
    Bitmap image;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ImageView imageView = findViewById(R.id.imageView);
        image = getIntent().getParcelableExtra("image");
        imageView.setImageBitmap(image);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    // Обработчик двойного нажатия на ImageView
                    if (imageView.getScaleX() == 1.0f) {
                        imageView.setScaleX(2.0f);
                        imageView.setScaleY(2.0f);
                    } else {
                        imageView.setScaleX(1.0f);
                        imageView.setScaleY(1.0f);
                    }
                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        gestureDetector = new GestureDetector(this, this);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Обрабатываем событие касания
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Определяем направление жеста по скорости по оси X
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            if (velocityX > 0) {
                // Справа налево - удаление фотографии
                DeleteImage(null);
            } else {
                // Слева направо - закрытие окна
                finish();
            }
        }
        return true;
    }


    JSONArray GetImage(){
        String fileName = "data.json";
        try {
            FileInputStream fileInputStream = openFileInput(fileName);
            int size = fileInputStream.available();
            byte[] buffer = new byte[size];
            fileInputStream.read(buffer);
            fileInputStream.close();
            String jsonString = new String(buffer, "UTF-8");// в этой строке jsonString будет содержать JSON-массив
            JSONArray json_array = new JSONArray(jsonString);
            return json_array;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void DeleteImage(View v){
        JSONArray jsonArray = GetImage();
        if(jsonArray == null)
            return;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String encoded = jsonObject.getString("image");
                byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (bitmap == image) { // myBitmap - ваш Bitmap, который нужно удалить
                    jsonArray.remove(i);
                    break;
                }
            }
            String fileName = "data.json";
            String jsonString = jsonArray.toString();
            FileOutputStream fileOutputStream = openFileOutput(fileName, MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            fileOutputStream.close();
        }
        catch (Exception e){}
    }

    public void GoBack(View v){
        finish();
    }
}