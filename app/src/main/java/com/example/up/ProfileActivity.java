package com.example.up;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    ListView photoList;
    private static final int REQUEST_CODE_SELECT_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        photoList = findViewById(R.id.my_recycler_view);
        ImageView imageView = findViewById(R.id.image_profile);
        TextView textView = findViewById(R.id.tx_name_profil);
        String imageUrl = User.avatar;
        Picasso.get().load(imageUrl).into(imageView);
        textView.setText(User.nickName);
        SetAdapter();
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

    void PutImage(Photo photo){
        JSONArray jsonArray = GetImage();
        if(jsonArray == null)
            jsonArray = new JSONArray();
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            JSONObject postData = new JSONObject();
            postData.put("image", encoded);
            postData.put("time", photo.timestamp);
            jsonArray.put(postData);

            String fileName = "data.json";
            String jsonString = jsonArray.toString();
            FileOutputStream fileOutputStream = openFileOutput(fileName, MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            fileOutputStream.close();
            SetAdapter();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    void SetAdapter(){
        JSONArray jsonArray = GetImage();
        List<Photo> photos = new ArrayList<>();
        try {
            if(jsonArray != null){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_object = jsonArray.getJSONObject(i);
                    Photo photo = new Photo();
                    String encoded = json_object.getString("image");
                    byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    photo.image = decodedBitmap;
                    photo.timestamp = json_object.getString("time");
                    photos.add(photo);
                }
            }
            if(photoList.getFooterViewsCount()==0){
            View footerView =  getLayoutInflater().inflate(R.layout.gallery_item, null);
            Button addButton = footerView.findViewById(R.id.add_btn);
            addButton.setVisibility(View.VISIBLE);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
                }
            });
            photoList.addFooterView(footerView);
            }
            GalleryAdapter adapter = new GalleryAdapter(ProfileActivity.this,photos);
            photoList.setAdapter(adapter);

        }
        catch (Exception e){}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try{
                ParcelFileDescriptor parcelFileDescriptor =
                        getContentResolver().openFileDescriptor(imageUri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                Photo photo = new Photo();
                photo.image = image;
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String currentTime = sdf.format(new Date());
                photo.timestamp = currentTime;
                PutImage(photo);
            }
            catch (Exception e){}
        }
    }
    public void GoMain(View v){
        Intent main = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(main);
    }

}