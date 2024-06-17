package com.example.myproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeUserFragment extends Fragment {


    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private userAdapter adapter;
    private List<Candidate> list;

    public HomeUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_user, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        adapter = new userAdapter(HomeUserFragment.this, list);
        recyclerView.setAdapter(adapter);
        showData();

        return view;
    }
    public void showData(){
        db.collection("Candidates").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        for(DocumentSnapshot snapshot : task.getResult()){
                            Candidate candidate = new Candidate(snapshot.getString("candidateId"), snapshot.getString("candidateName"), snapshot.getString("candidateDescription"), snapshot.getLong("voteCount").intValue() );
                            list.add(candidate);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Can't fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onCandidateSelected(Candidate candidate) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", candidate.getName());
        bundle.putString("description", candidate.getDescription());
        bundle.putString("candidateId", candidate.getId());
        fragment.setArguments(bundle);
        replaceFragment(fragment);

        // Handle the selected candidate here
        Toast.makeText(getContext(), "Selected Candidate: " + candidate.getName(), Toast.LENGTH_SHORT).show();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();

        if (fragments != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            for (Fragment frag : fragments) {
                if (frag != null && !frag.equals(fragment) && frag.isAdded()) {
                    fragmentTransaction.remove(frag);
                }
            }

            fragmentTransaction.replace(R.id.fragment_container_user, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}



