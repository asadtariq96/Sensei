package com.sensei.assistant.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sensei.assistant.Activities.Quizzes.QuizDetailActivityOld;
import com.sensei.assistant.R;

/**
 * Created by Asad on 11-Nov-16.
 */

public class QuizzesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quizzes, null);

        view.findViewById(R.id.cardview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), QuizDetailActivityOld.class));

            }
        });


        return view;
    }
}
