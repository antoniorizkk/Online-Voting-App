package com.example.myproject;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class AddCandidateFragment extends Fragment {

    EditText candidateName, candidateDescription;
    Button addCandidate;
    Button showCandidate;
    FirebaseFirestore fb ;
    private String uName, uDesc, uId;
    public AddCandidateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_candidate, container, false);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Add Data");

        candidateName = view.findViewById(R.id.candidateName);
        candidateDescription = view.findViewById(R.id.candidatDescription);
        addCandidate = view.findViewById(R.id.addCandidate);



        fb = FirebaseFirestore.getInstance();

        Bundle bundle = getArguments();
        if(bundle != null){
            addCandidate.setText("Update");
            uName = bundle.getString("uName");
            uId = bundle.getString("uId");
            uDesc = bundle.getString("uDesc");
            candidateName.setText(uName);
            candidateDescription.setText(uDesc);
        } else{
            addCandidate.setText("Add");
        }

        addCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = candidateName.getText().toString().trim();
                String description = candidateDescription.getText().toString().trim();
                int voteCount = 0;


                Bundle bundle1 = getArguments();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(description)){
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(bundle1 != null){
                    String id = uId;
                    updateToFireStore(id, name, description);
                } else{
                    addData(name, description, voteCount);
                }

            }
        });
        return view;
    }

    private void updateToFireStore(String id, String name, String desc) {
        // Check for similar names in Firestore before updating the data
        Query query = fb.collection("Candidates").whereEqualTo("name", name);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null && !snapshot.isEmpty()) {
                        // Similar name found, show error message or take appropriate action
                        Toast.makeText(getContext(), "A candidate with the same name already exists.", Toast.LENGTH_SHORT).show();
                    } else {
                        // No similar name found, proceed with updating the data
                        fb.collection("Candidates").document(id)
                                .update("candidateName", name, "candidateDescription", desc)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Data Updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    // Error occurred while fetching FireStore data
                    Toast.makeText(getContext(), "Error occurred while checking for similar names.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void addData(String name, String description, int voteCount) {
        // Check for similar names in FireStore before uploading the data
        Query query = fb.collection("Candidates").whereEqualTo("candidateName", name);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null && !snapshot.isEmpty()) {
                        // Similar names found, show error message or take appropriate action
                        Toast.makeText(requireContext(), "A candidate with the same name already exists.", Toast.LENGTH_SHORT).show();
                    } else {
                        // No similar names found, proceed with uploading the data
                        String id = UUID.randomUUID().toString();

                        Map<String, Object> doc = new HashMap<>();
                        doc.put("candidateId", id);
                        doc.put("candidateName", name);
                        doc.put("candidateDescription", description);
                        doc.put("voteCount",voteCount);

                        // Add the data
                        fb.collection("Candidates").document(id).set(doc)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(requireContext(), "Data Uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    // Error occurred while fetching FireStore data
                    Toast.makeText(requireContext(), "Error occurred while checking for similar names.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}