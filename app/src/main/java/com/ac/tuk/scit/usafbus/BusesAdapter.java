package com.ac.tuk.scit.usafbus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BusesAdapter extends RecyclerView.Adapter<BusesAdapter.ViewHolder> {


    Context context;
    ArrayList<Company> companyArrayList;


    // create constructor to initialize context and data sent from MainActivity
    public BusesAdapter(Context context, ArrayList<Company> companyArrayList) {
        this.context = context;
        this.companyArrayList = companyArrayList;

    }

    // Inflate the layout when viewHolder created
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //bind the data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        //Company current;
       // current = companyArrayList.get(position);

        holder.busCompanyName.setText(companyArrayList.get(position).getName());
        Picasso.get().load(companyArrayList.get(position).getImage()).into(holder.busCompanyImage);


//        holder.busCompanyName.setText(current.Name);
//        Glide.with(context)
//                .load("http://192.168.137.1//Android_booking_app/fetch_data.php" + current.Image)
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .into(holder.busCompanyImage);
    }


    // return total item from List
    @Override
    public int getItemCount() {
        return companyArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView busCompanyImage;
        TextView busCompanyName;

        // create constructor to get widget reference
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            busCompanyImage = itemView.findViewById(R.id.busCompanyLogo);
            busCompanyName = itemView.findViewById(R.id.busCompanyName);
        }
    }
}

