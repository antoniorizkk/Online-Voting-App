package com.example.myproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class AdminHomeFragment extends Fragment {

    private TextView userCountTV;
    private TextView candidateCountTV;
    private FirebaseFirestore firestore;

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_admin_home, container, false);
        firestore = FirebaseFirestore.getInstance();

        userCountTV = view.findViewById(R.id.user_tv);
        candidateCountTV = view.findViewById(R.id.candidate_tv);

        CollectionReference userCollection = firestore.collection("Users");
        userCollection.get().addOnCompleteListener( task -> {
            if(task.isSuccessful()){
                QuerySnapshot querySnapshot = task.getResult();
                if(querySnapshot != null){
                    int userCount = querySnapshot.size() - 1;
                    userCountTV.setText("Total Users: "+ userCount);
                }
                else{
                    Toast.makeText(getContext(), "Error when trying to show data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        CollectionReference candidateCollection = firestore.collection("Candidates");
        candidateCollection.get().addOnCompleteListener( task -> {
            if(task.isSuccessful()){
                QuerySnapshot querySnapshot = task.getResult();
                if(querySnapshot != null){
                    int userCount = querySnapshot.size();
                    candidateCountTV.setText("Total Candidate: "+ userCount);
                }
                else{
                    Toast.makeText(getContext(), "Error when trying to show data", Toast.LENGTH_SHORT).show();
                }
            }
        });


        userCountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new AdminUserFragment());
            }
        });

        candidateCountTV.setOnClickListener(new View.OnClickListener() {
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