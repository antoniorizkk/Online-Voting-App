package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {

    private ShowCandidateFragment myFragment;
    private List<Candidate> candidateList;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public myAdapter(ShowCandidateFragment myFragment, List<Candidate> cList){
        this.myFragment = myFragment;
        this.candidateList = cList;
    }
    public void updateData(int position){
        Candidate item = candidateList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("uId",item.getId());
        bundle.putString("uName",item.getName());
        bundle.putString("uDesc", item.getDescription());

        AddCandidateFragment rf = new AddCandidateFragment();
        rf.setArguments(bundle);

        FragmentManager fragmentManager = myFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, rf);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void deleteData(int position){
        Candidate item = candidateList.get(position);
        db.collection("Candidates").document(item.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            notifyRemoved(position);
                            Toast.makeText(myFragment.getContext(), "Data deleted", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(myFragment.getContext(), "Error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void notifyRemoved(int position){
        candidateList.remove(position);
        notifyItemRemoved(position);
        myFragment.showData();

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myFragment.getContext()).inflate(R.layout.candidate, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(candidateList.get(position).getName());
        holder.desc.setText(candidateList.get(position).getDescription());
        holder.voteCount.setText(String.valueOf(candidateList.get(position).getVoteCount()));
    }

    @Override
    public int getItemCount() {

        return candidateList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, desc, voteCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_text);
            desc = itemView.findViewById(R.id.description_text);
            voteCount =  itemView.findViewById(R.id.voteCount_text);
        }
    }
}
