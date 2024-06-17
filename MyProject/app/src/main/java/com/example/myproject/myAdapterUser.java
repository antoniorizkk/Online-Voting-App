package com.example.myproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class myAdapterUser  extends RecyclerView.Adapter<myAdapterUser.MyViewHolder> {
    private AdminUserFragment fragment;
    private List<User> uList;

    public myAdapterUser(AdminUserFragment fragment, List<User> uList){
        this.fragment = fragment;
        this.uList = uList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(fragment.getContext()).inflate(R.layout.user, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.email.setText(uList.get(position).getUserEmail());
        if(uList.get(position).hasVoted() == false){
            holder.hasVoted.setText(String.valueOf("Has voted: No"));
        } else{
            holder.hasVoted.setText(String.valueOf("Has voted: Yes"));

        }
    }
    @Override
    public int getItemCount() {
        return uList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView email;
        TextView hasVoted;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            email =itemView.findViewById(R.id.email_text);
            hasVoted = itemView.findViewById(R.id.hasVoted_text);
        }
    }
}
