package com.example.myproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminSearchFragment extends Fragment {


    CollectionReference mref;
    private ListView listdata;
    private AutoCompleteTextView txtSearch;

    public AdminSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_admin_search, container, false);

        mref = FirebaseFirestore.getInstance().collection("Candidates");
        listdata = (ListView) view.findViewById(R.id.listData);
        txtSearch = (AutoCompleteTextView) view.findViewById(R.id.txtSearch);

        mref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                populateSearch(queryDocumentSnapshots);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure
            }
        });

        return view;
    }
    public void populateSearch(QuerySnapshot snapshot){
        ArrayList<String> names = new ArrayList<>();
        if(!snapshot.isEmpty()){
            for(QueryDocumentSnapshot qs: snapshot){
                String name = qs.getString("candidateName");
                names.add(name);
            }
            ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,names);
            txtSearch.setAdapter(adapter);
            txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String name = txtSearch.getText().toString();
                    searchCandidate(name);
                }
            });
        }else{
            Log.d("Candidates", "no data found");
        }

    }

    public void searchCandidate(String name){
        mref.whereEqualTo("candidateName", name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            ArrayList<String> listCandidate = new ArrayList<>();
                            for(QueryDocumentSnapshot qs: queryDocumentSnapshots){
                                Candidate candidate  = new Candidate(qs.getString("candidateId"), qs.getString("candidateName"), qs.getString("candidateDescription"),qs.getLong("voteCount").intValue());
                                listCandidate.add("Candidate Name:"+candidate.getName()+"\n Description:"+candidate.getDescription()
                                +"\n Vote Count: "+candidate.getVoteCount());
                            }
                            ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,listCandidate);
                            listdata.setAdapter(adapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Candidates","no data found");
                    }
                });
    }
}