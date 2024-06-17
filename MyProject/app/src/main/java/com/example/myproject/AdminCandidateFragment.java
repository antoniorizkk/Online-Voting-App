package com.example.myproject;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminCandidateFragment extends Fragment {

    Button addCandidateBtn, showCandidate;
    FirebaseFirestore db;

    public AdminCandidateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_candidate, container, false);
        addCandidateBtn = view.findViewById(R.id.addCandidate_btn);
        showCandidate = view.findViewById(R.id.showCandidate_btn);
        db = FirebaseFirestore.getInstance();



        //click listener for add button
        addCandidateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new AddCandidateFragment());
            }
        });
        // click listener for show button
        showCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new ShowCandidateFragment());
            }
        });


        return view;
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

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}