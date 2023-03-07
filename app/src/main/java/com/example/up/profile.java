package com.example.up;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ImageView imagee = findViewById(R.id.image_profile);
        String imageUrl = User.avatar;
        Picasso.get()
                .load(imageUrl)
                .transform(new round_transform(360))
                .into(imagee);
        ImageButton imageButton = findViewById(R.id.btn_to_profile);
        imageButton.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
        //imageView.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
        //imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
       // imageView.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
    }
}