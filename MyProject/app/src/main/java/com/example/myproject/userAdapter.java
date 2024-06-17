package com.example.myproject;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.MyViewHolder> {
    private HomeUserFragment fragment;
    private List<Candidate> candidateList;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int selectedItemPosition = -1;
    public userAdapter(HomeUserFragment myFragment, List<Candidate> cList){
        this.fragment = myFragment;
        this.candidateList = cList;
    }
    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(fragment.getContext()).inflate(R.layout.item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(candidateList.get(position).getName());
        holder.desc.setText(candidateList.get(position).getDescription());
        holder.voteCount.setText(String.valueOf(candidateList.get(position).getVoteCount()));

        int itemPosition = position;
        if (selectedItemPosition == position) {
            holder.itemView.setBackgroundColor(Color.GREEN); // Adjust the color to your preference
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        // Set the click listener for item selection
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedItem(itemPosition);
            }
        });
    }


    @Override
    public int getItemCount() {
        return candidateList.size();
    }
    public void setSelectedItem(int position) {
        if (selectedItemPosition != position) {
            int previousSelectedItemPosition = selectedItemPosition;
            selectedItemPosition = position;

            // Notify item change for visual update
            notifyItemChanged(previousSelectedItemPosition);
            notifyItemChanged(selectedItemPosition);

            // Notify the fragment about the selection change
            fragment.onCandidateSelected(candidateList.get(position));
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, desc, voteCount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_tv);
            desc = itemView.findViewById(R.id.description_tv);
            voteCount =  itemView.findViewById(R.id.voteCount_tv);
        }
    }
}
