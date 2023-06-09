package com.example.rentcar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.userViewHolder> {
    ArrayList<Car> cars;

    public CardAdapter(ArrayList<Car> cars) {
        this.cars = cars;
    }


    @NonNull
    @Override
    public CardAdapter.userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, true);
        return new userViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.userViewHolder holder, int position) {
        holder.plateNumber.setText(cars.get(position).getPlatenumber());
        holder.brand.setText(cars.get(position).getBrand());
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public class userViewHolder extends RecyclerView.ViewHolder {
        TextView plateNumber;
        TextView brand;

        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            plateNumber = itemView.findViewById(R.id.textCardNombre);
            brand = itemView.findViewById(R.id.textCardCorreo);
        }
    }
}
