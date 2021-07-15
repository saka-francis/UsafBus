package com.ac.tuk.scit.usafbus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

public class BusesAdapter extends RecyclerView.Adapter<BusesAdapter.ViewHolder> {

     Context context;
     List<Company> bus;

    static {
        Collections.emptyList();
    }

    BusesAdapter current;

    // create constructor to initialize context and data sent from MainActivity
    public BusesAdapter(Context context, List<Company> bus) {
        this.context = context;
        this.bus = bus;
        this.current = current;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;
        Company current = bus.get(position);

        Glide.with(context)
                .load("http://192.168.208.126/Android_booking_app/fetch_data.php" + current.image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(viewHolder.image);

        viewHolder.name.setText(current.name);


    }

    @Override
    public int getItemCount() {
        return bus.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
        }
    }
}
