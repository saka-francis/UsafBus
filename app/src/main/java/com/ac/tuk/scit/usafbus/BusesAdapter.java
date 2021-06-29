package com.ac.tuk.scit.usafbus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewTreeViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BusesAdapter extends RecyclerView.Adapter<BusesAdapter.ViewHolder> {

     Context context;
    List<Company> CompanyList;

    public BusesAdapter(List<Company> companyList, Context context) {

        super();
        this.CompanyList = companyList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public BusesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BusesAdapter.ViewHolder holder, int position) {
        Company companyList = CompanyList.get(position);

        holder.name.setText(companyList.getName());

        Glide.with(context)
                .load(companyList.getImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return CompanyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            image.findViewById(R.id.image);
            name.findViewById(R.id.name);
        }
    }
}
