package com.example.up;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class LVAdapter extends ArrayAdapter<Quote> {

    private List<Quote> myObjects;

    public LVAdapter(Context context, List<Quote> myObjects) {
        super(context, 0, myObjects);
        this.myObjects = myObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_item, parent, false);
        }

        Quote myObject = myObjects.get(position);

        // Получение View-элементов из макета my_object_layout.xml
        TextView title = convertView.findViewById(R.id.title);
        TextView description = convertView.findViewById(R.id.description);
        ImageView image = convertView.findViewById(R.id.image);

        // Заполнение View-элементов данными из myObject
        title.setText(myObject.title);
        description.setText(myObject.description);
        Picasso.get().load(myObject.image).into(image);

        return convertView;
    }
}

