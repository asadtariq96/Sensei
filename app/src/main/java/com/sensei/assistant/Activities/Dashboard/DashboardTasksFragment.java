package com.sensei.assistant.Activities.Dashboard;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.sensei.assistant.Adapters.DashboardAssignmentAdapter;
import com.sensei.assistant.Adapters.DashboardHomeworkAdapter;
import com.sensei.assistant.Adapters.DashboardQuizAdapter;
import com.sensei.assistant.DataHandlers.CourseDataHandler;
import com.sensei.assistant.DataModelClasses.AssignmentDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.DataModelClasses.HomeworkDataModel;
import com.sensei.assistant.DataModelClasses.QuizDataModel;
import com.sensei.assistant.R;
import com.squareup.otto.Subscribe;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import timber.log.Timber;

import static com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_LEFT;
import static com.sensei.assistant.Application.MyApplication.bus;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;


/**
 * Created by Asad on 17-Dec-16.
 */
public class DashboardTasksFragment extends Fragment {

    RecyclerView quizRecyclerview;
    RecyclerView assignmmentRecyclerview;
    RecyclerView homeworkRecyclerview;
    DashboardQuizAdapter quizAdapter;
    DashboardAssignmentAdapter assignmentAdapter;
    DashboardHomeworkAdapter homeworkAdapter;
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
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        bus.register(this);


        quizRecyclerview = (RecyclerView) view.findViewById(R.id.quiz_recyclerview);
        quizAdapter = new DashboardQuizAdapter(R.layout.quiz_layout, getCourseDataInstance().getListOfIncompleteQuizzes());
        quizAdapter.addHeaderView(quizHeaderView);
//        quizAdapter.openLoadAnimation(SLIDEIN_LEFT);
//        quizAdapter.isFirstOnly(false);

        assignmmentRecyclerview = (RecyclerView) view.findViewById(R.id.assignment_recyclerview);
        assignmentAdapter = new DashboardAssignmentAdapter(R.layout.quiz_layout, getCourseDataInstance().getListOfIncompleteAssignments());
        assignmentAdapter.addHeaderView(assignmentHeaderView);

        homeworkRecyclerview = (RecyclerView) view.findViewById(R.id.homework_recyclerview);
        homeworkAdapter = new DashboardHomeworkAdapter(R.layout.quiz_layout, getCourseDataInstance().getListOfIncompleteHomework());
        homeworkAdapter.addHeaderView(homeworkHeaderView);

//        OnItemChildClickListener onItemChildClickListener = new OnItemChildClickListener() {
//            @Override
//            public void onSimpleItemChildClick(final BaseQuickAdapter baseQuickAdapter, View view, final int i) {
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        Toast.makeText(getActivity(), "Quiz marked as completed!", Toast.LENGTH_SHORT).show();
////                        QuizDataModel quizDataModel = (QuizDataModel) baseQuickAdapter.getItem(i);
////                        getCourseDataInstance().updateTaskCompleted(quizDataModel, true);
////                        baseQuickAdapter.remove(i);
//                        quizRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteQuizzes().isEmpty() ? View.GONE : View.VISIBLE);
//                        assignmmentRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteAssignments().isEmpty() ? View.GONE : View.VISIBLE);
//                        homeworkRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteHomework().isEmpty() ? View.GONE : View.VISIBLE);
//
//
//                    }
//                }, 1000);
//            }
//        };


        quizRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        quizRecyclerview.setAdapter(quizAdapter);


        assignmmentRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        assignmmentRecyclerview.setAdapter(assignmentAdapter);

        homeworkRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeworkRecyclerview.setAdapter(homeworkAdapter);

//        quizRecyclerview.addOnItemTouchListener(onItemChildClickListener);
//        assignmmentRecyclerview.addOnItemTouchListener(onItemChildClickListener);
//        homeworkRecyclerview.addOnItemTouchListener(onItemChildClickListener);


//        quizRecyclerview.setItemAnimator(new SlideInLeftAnimator());
//        quizRecyclerview.getItemAnimator().setRemoveDuration(350);
//
//        assignmmentRecyclerview.setItemAnimator(new SlideInLeftAnimator());
//        assignmmentRecyclerview.getItemAnimator().setRemoveDuration(350);
//
//        homeworkRecyclerview.setItemAnimator(new SlideInLeftAnimator());
//        homeworkRecyclerview.getItemAnimator().setRemoveDuration(350);

        final View rootview = getActivity().findViewById(R.id.coordinator_layout);


        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
//                Timber.d("onItemSwipeListener onItemSwipeStart");
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, final int pos) {
//                Timber.d("onItemSwipeListener onItemSwiped");


                final QuizDataModel quizDataModel = quizAdapter.getItem(pos);
                String quizID = getCourseDataInstance().getQuizID(quizDataModel);
                final CourseDataModel courseDataModel = getCourseDataInstance().getCourse(quizDataModel);
                String courseID = getCourseDataInstance().getCourseID(courseDataModel);

                getCourseDataInstance().deleteQuiz(courseID, quizID);

                quizRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteQuizzes().isEmpty() ? View.GONE : View.VISIBLE);
                assignmmentRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteAssignments().isEmpty() ? View.GONE : View.VISIBLE);
                homeworkRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteHomework().isEmpty() ? View.GONE : View.VISIBLE);

                Snackbar.make(rootview, "Quiz deleted!", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getCourseDataInstance().addQuiz(courseDataModel, quizDataModel);
                            }
                        })
                        .show(); // Don’t forget to show!
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {
//                Timber.d("onItemSwipeListener onItemSwipeMoving");

            }
        };

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(quizAdapter);
        itemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(quizRecyclerview);
        quizAdapter.enableSwipeItem();
        quizAdapter.setOnItemSwipeListener(onItemSwipeListener);


        OnItemSwipeListener onItemSwipeListener2 = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
//                Timber.d("onItemSwipeListener2 onItemSwipeStart");

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, final int pos) {
//                Timber.d("onItemSwipeListener2 onItemSwiped");


                final AssignmentDataModel assignmentDataModel = assignmentAdapter.getItem(pos);
                String assignmentID = getCourseDataInstance().getAssignmentID(assignmentDataModel);
                final CourseDataModel courseDataModel = getCourseDataInstance().getCourse(assignmentDataModel);
                String courseID = getCourseDataInstance().getCourseID(courseDataModel);

                getCourseDataInstance().deleteAssignment(courseID, assignmentID);

                quizRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteQuizzes().isEmpty() ? View.GONE : View.VISIBLE);
                assignmmentRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteAssignments().isEmpty() ? View.GONE : View.VISIBLE);
                homeworkRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteHomework().isEmpty() ? View.GONE : View.VISIBLE);

                Snackbar.make(rootview, "Assignment deleted!", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getCourseDataInstance().addAssignment(courseDataModel, assignmentDataModel);

                            }
                        })
                        .show(); // Don’t forget to show!
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {
//                Timber.d("onItemSwipeListener2 onItemSwipeMoving");

            }
        };

        ItemDragAndSwipeCallback itemDragAndSwipeCallback2 = new ItemDragAndSwipeCallback(assignmentAdapter);
        itemDragAndSwipeCallback2.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(itemDragAndSwipeCallback2);
        itemTouchHelper2.attachToRecyclerView(assignmmentRecyclerview);
        assignmentAdapter.enableSwipeItem();
        assignmentAdapter.setOnItemSwipeListener(onItemSwipeListener2);


        OnItemSwipeListener onItemSwipeListener3 = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, final int pos) {


                final HomeworkDataModel homeworkDataModel = homeworkAdapter.getItem(pos);
                String homeworkID = getCourseDataInstance().getHomeworkID(homeworkDataModel);
                final CourseDataModel courseDataModel = getCourseDataInstance().getCourse(homeworkDataModel);
                String courseID = getCourseDataInstance().getCourseID(courseDataModel);

                getCourseDataInstance().deleteHomework(courseID, homeworkID);

                quizRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteQuizzes().isEmpty() ? View.GONE : View.VISIBLE);
                assignmmentRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteAssignments().isEmpty() ? View.GONE : View.VISIBLE);
                homeworkRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteHomework().isEmpty() ? View.GONE : View.VISIBLE);

                Snackbar.make(rootview, "Homework deleted!", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getCourseDataInstance().addHomework(courseDataModel, homeworkDataModel);

                            }
                        })
                        .show(); // Don’t forget to show!
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {
            }
        };

        ItemDragAndSwipeCallback itemDragAndSwipeCallback3 = new ItemDragAndSwipeCallback(homeworkAdapter);
        itemDragAndSwipeCallback3.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        ItemTouchHelper itemTouchHelper3 = new ItemTouchHelper(itemDragAndSwipeCallback3);
        itemTouchHelper3.attachToRecyclerView(homeworkRecyclerview);
        homeworkAdapter.enableSwipeItem();
        homeworkAdapter.setOnItemSwipeListener(onItemSwipeListener3);


    }

    public void updateData() {
        quizAdapter.setNewData(getCourseDataInstance().getListOfIncompleteQuizzes());
        quizRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteQuizzes().isEmpty() ? View.GONE : View.VISIBLE);
        assignmentAdapter.setNewData(getCourseDataInstance().getListOfIncompleteAssignments());
        assignmmentRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteAssignments().isEmpty() ? View.GONE : View.VISIBLE);
        homeworkAdapter.setNewData(getCourseDataInstance().getListOfIncompleteHomework());
        homeworkRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteHomework().isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void onStart() {
        super.onStart();

        updateData();


    }

    @Subscribe
    public void answerAvailable(CourseDataHandler.DataChangedEvent event) {
        Timber.d("event received");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateData();
            }
        }, 1000);
    }


}
