package com.example.myproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class DetailsFragment extends Fragment {

    private TextView nameTextView;
    private TextView descriptionTextView;
    private Button voteButton;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Candidate candidate;

    public DetailsFragment() {
        // Required empty public constructor
    }
    public static DetailsFragment newInstance(Candidate candidate) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", candidate.getName());
        bundle.putString("description", candidate.getDescription());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name");
            String description = bundle.getString("description");
            String candidateId = bundle.getString("candidateId");

            nameTextView = view.findViewById(R.id.name_text_view);
            descriptionTextView = view.findViewById(R.id.description_text_view);
            voteButton = view.findViewById(R.id.vote_button);

            nameTextView.setText(name);
            descriptionTextView.setText(description);

            // Initialize the candidate object
            candidate = new Candidate(candidateId, name, description, 0);

            voteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    voteForCandidate();
                }
            });
        }

        return view;
    }


    private void voteForCandidate() {
        if (candidate != null) {
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DocumentReference userRef = db.collection("Users").document(userId);

                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot userSnapshot = task.getResult();
                            if (userSnapshot != null && userSnapshot.exists()) {
                                boolean hasVoted = userSnapshot.getBoolean("hasVoted");
                                if (!hasVoted) {
                                    int newVoteCount = candidate.getVoteCount() + 1;
                                    DocumentReference candidateRef = db.collection("Candidates").document(candidate.getId());

                                    db.runTransaction(new Transaction.Function<Void>() {
                                        @Nullable
                                        @Override
                                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                            DocumentSnapshot candidateSnapshot = transaction.get(candidateRef);
                                            int currentVoteCount = candidateSnapshot.getLong("voteCount").intValue();
                                            transaction.update(candidateRef, "voteCount", currentVoteCount + 1);
                                            transaction.update(userRef, "hasVoted", true);
                                            return null;
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Update the local vote count and disable the vote button
                                            candidate.setVoteCount(newVoteCount);
                                            voteButton.setEnabled(false);
                                            Toast.makeText(getContext(), "Vote successful", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Failed to vote", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    voteButton.setEnabled(false);
                                    Toast.makeText(getContext(), "You have already voted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            }
        }
    }

}
