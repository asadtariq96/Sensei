package com.sensei.Activities.Dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sensei.Adapters.DashboardAssignmentAdapter;
import com.sensei.Adapters.DashboardQuizAdapter;
import com.sensei.R;

import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;


/**
 * Created by Asad on 17-Dec-16.
 */
public class DashboardTasksFragment extends Fragment {

    RecyclerView quizRecyclerview;
    RecyclerView assignmmentRecyclerview;
    RecyclerView homeworkRecyclerview;
    DashboardQuizAdapter quizAdapter;
    DashboardAssignmentAdapter assignmentAdapter;
    DashboardQuizAdapter homeworkAdapter;
    private View quizHeaderView;
    private View assignmentHeaderView;
    private View homeworkHeaderView;
    private View footerView;
    private View footerView2;
    private View footerView3;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_tasks_layout, null);

        quizHeaderView = inflater.inflate(R.layout.quizzes_header_view, container, false);

        assignmentHeaderView = inflater.inflate(R.layout.quizzes_header_view, container, false);
        ((TextView) assignmentHeaderView.findViewById(R.id.textview)).setText("Assignments");

        homeworkHeaderView = inflater.inflate(R.layout.quizzes_header_view, container, false);
        ((TextView) homeworkHeaderView.findViewById(R.id.textview)).setText("Homework");

        footerView = inflater.inflate(R.layout.separator, container, false);
        footerView2 = inflater.inflate(R.layout.separator, container, false);
        footerView3 = inflater.inflate(R.layout.separator, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        quizRecyclerview = (RecyclerView) view.findViewById(R.id.quiz_recyclerview);
        quizAdapter = new DashboardQuizAdapter(R.layout.quiz_layout, getCourseDataInstance().getListOfQuizzes());
//        quizAdapter.addFooterView(footerView);
        quizAdapter.addHeaderView(quizHeaderView);


        assignmmentRecyclerview = (RecyclerView) view.findViewById(R.id.assignment_recyclerview);
        assignmentAdapter = new DashboardAssignmentAdapter(R.layout.quiz_layout, getCourseDataInstance().getListOfAssignments());
//        assignmentAdapter.addFooterView(footerView2);
        assignmentAdapter.addHeaderView(assignmentHeaderView);


//        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        quizRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        quizRecyclerview.setAdapter(quizAdapter);

        assignmmentRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        assignmmentRecyclerview.setAdapter(assignmentAdapter);
    }

    public void onStart() {
        super.onStart();


        quizAdapter.setNewData(getCourseDataInstance().getListOfQuizzes());
        quizAdapter.notifyDataSetChanged();

        quizRecyclerview.setVisibility(getCourseDataInstance().getListOfQuizzes().isEmpty() ? View.GONE : View.VISIBLE);

        assignmentAdapter.setNewData(getCourseDataInstance().getListOfAssignments());
        assignmentAdapter.notifyDataSetChanged();

        assignmmentRecyclerview.setVisibility(getCourseDataInstance().getListOfAssignments().isEmpty() ? View.GONE : View.VISIBLE);


//        if (getCourseDataInstance().getListOfClassesForCurrentDay().isEmpty())
//            placeHolder.setVisibility(View.VISIBLE);
//        else
//            placeHolder.setVisibility(View.GONE);

    }
}
