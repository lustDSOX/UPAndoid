package com.example.up;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private List<Feeling> myObjects; // Список объектов для отображения

    // Конструктор адаптера
    public RVAdapter(List<Feeling> myObjects) {
        this.myObjects = myObjects;
    }

    // Создание нового элемента списка (вызывается LayoutManager'ом)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new ViewHolder(view);
    }

    // Заполнение данных элемента списка (вызывается LayoutManager'ом)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Feeling myObject = myObjects.get(position);
        holder.textView.setText(myObject.title);
        Picasso.get().load(myObject.image).into(holder.imageView);
    }

    // Получение количества элементов списка
    @Override
    public int getItemCount() {
        return myObjects.size();
    }

    // Класс ViewHolder, содержит ссылки на виджеты элемента списка
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
