package com.company.mapsproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.mapsproject.R;
import com.company.mapsproject.YourReminders;
import com.company.mapsproject.models.Reminder;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {

    ArrayList<Reminder> dataList;

    Context context;


    public MyAdapter(ArrayList<Reminder> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlayout,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getTitle());
        holder.description.setText(dataList.get(position).getDesc());
        holder.location.setText(dataList.get(position).getLocation());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView ;
        TextView title;
        TextView description;
        TextView location;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.imagecard);
            title = itemView.findViewById(R.id.titlecard);
            description = itemView.findViewById(R.id.descriptioncard);
            location = itemView.findViewById(R.id.locationcard);
        }
    }
}
