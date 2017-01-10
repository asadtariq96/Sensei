package com.sensei.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sensei.Activities.AssignmentDetailActivity;
import com.sensei.Activities.QuizDetailActivity;
import com.sensei.R;

/**
 * Created by Asad on 17-Dec-16.
 */
public class AssignmentsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignments, null);

        view.findViewById(R.id.cardview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), AssignmentDetailActivity.class));

            }
        });


        return view;
    }
}
