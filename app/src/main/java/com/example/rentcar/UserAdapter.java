package com.example.rentcar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.userViewHolder> {
    ArrayList<User> users;

    public UserAdapter(ArrayList<User> users) {
        this.users = users;
    }


    @NonNull
    @Override
    public UserAdapter.userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, null, false);
        return new userViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.userViewHolder holder, int position) {
        holder.name.setText(users.get(position).getName());
        holder.username.setText(users.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class userViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView username;

        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textCardNombre);
            username = itemView.findViewById(R.id.textCardCorreo);
        }
    }
}
