package com.sensei.assistant.Activities.Dashboard;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.github.zagum.switchicon.SwitchIconView;
import com.sensei.assistant.Activities.Assignments.AssignmentDetailActivity;
import com.sensei.assistant.Activities.Courses.AddCourseActivity;
import com.sensei.assistant.Activities.Homework.HomeworkDetailActivity;
import com.sensei.assistant.Activities.Quizzes.AddQuizActivity;
import com.sensei.assistant.Activities.Quizzes.QuizDetailActivity;
import com.sensei.assistant.Activities.Quizzes.QuizzesListActivity;
import com.sensei.assistant.Adapters.DashboardAssignmentAdapter;
import com.sensei.assistant.Adapters.DashboardHomeworkAdapter;
import com.sensei.assistant.Adapters.DashboardQuizAdapter;
import com.sensei.assistant.Adapters.MainAdapter;
import com.sensei.assistant.DataHandlers.CourseDataHandler;
import com.sensei.assistant.DataModelClasses.AssignmentDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.DataModelClasses.HomeworkDataModel;
import com.sensei.assistant.DataModelClasses.QuizDataModel;
import com.sensei.assistant.R;
import com.squareup.otto.Subscribe;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import timber.log.Timber;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
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
    MainAdapter adapter;
    RecyclerView recyclerView;
    //    private View quizHeaderView;
//    private View assignmentHeaderView;
//    private View homeworkHeaderView;
    RelativeLayout placeholder;
    final String PREFS_NAME = "MyPrefsFile";


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_dashboard_tasks_layout, null);

//        quizHeaderView = inflater.inflate(R.layout.quizzes_header_view, container, false);
//
//        assignmentHeaderView = inflater.inflate(R.layout.quizzes_header_view, container, false);
//        ((TextView) assignmentHeaderView.findViewById(R.id.textview)).setText("Assignments");

//        homeworkHeaderView = inflater.inflate(R.layout.quizzes_header_view, container, false);
//        ((TextView) homeworkHeaderView.findViewById(R.id.textview)).setText("Homework");

//        footerView = inflater.inflate(R.layout.separator, container, false);
//        footerView2 = inflater.inflate(R.layout.separator, container, false);
//        footerView3 = inflater.inflate(R.layout.separator, container, false);


        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        bus.register(this);

//        placeholder = view.findViewById(R.id.placeholder);
        recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new MainAdapter(getCourseDataInstance().getListOfIncompleteQuizzes(),
                getCourseDataInstance().getListOfIncompleteAssignments(),
                getCourseDataInstance().getListOfIncompleteHomework());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.expandAllSections();
        adapter.shouldShowHeadersForEmptySections(false);


//        quizRecyclerview = (RecyclerView) view.findViewById(R.id.quiz_recyclerview);
//        quizAdapter = new DashboardQuizAdapter(R.layout.quiz_layout, getCourseDataInstance().getListOfIncompleteQuizzes(), true);
////        quizAdapter.addHeaderView(quizHeaderView);
//
//
//        assignmmentRecyclerview = (RecyclerView) view.findViewById(R.id.assignment_recyclerview);
//        assignmentAdapter = new DashboardAssignmentAdapter(R.layout.quiz_layout, getCourseDataInstance().getListOfIncompleteAssignments());
////        assignmentAdapter.addHeaderView(assignmentHeaderView);
//
//        homeworkRecyclerview = (RecyclerView) view.findViewById(R.id.homework_recyclerview);
//        homeworkAdapter = new DashboardHomeworkAdapter(R.layout.quiz_layout, getCourseDataInstance().getListOfIncompleteHomework());
////        homeworkAdapter.addHeaderView(homeworkHeaderView);
//
//        OnItemClickListener onItemClickListener = new OnItemClickListener() {
//            @Override
//            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//
//                Timber.d("onSimpleItemClick");
//
//                if (baseQuickAdapter == quizAdapter) {
//                    QuizDataModel quizDataModel = (QuizDataModel) baseQuickAdapter.getItem(i);
//                    final Intent intent = new Intent(getActivity(), QuizDetailActivity.class);
//                    String courseID = getCourseDataInstance().getCourseID(getCourseDataInstance().getCourse(quizDataModel));
//                    String quizID = getCourseDataInstance().getQuizID(quizDataModel);
//                    intent.putExtra("courseID", courseID);
//                    intent.putExtra("quizID", quizID);
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            startActivity(intent);
//
//                        }
//                    }, 200);
//                }
//
//                if (baseQuickAdapter == homeworkAdapter) {
//                    HomeworkDataModel homeworkDataModel = (HomeworkDataModel) baseQuickAdapter.getItem(i);
//                    final Intent intent = new Intent(getActivity(), HomeworkDetailActivity.class);
//                    String courseID = getCourseDataInstance().getCourseID(getCourseDataInstance().getCourse(homeworkDataModel));
//                    String homeworkID = getCourseDataInstance().getHomeworkID(homeworkDataModel);
//                    intent.putExtra("courseID", courseID);
//                    intent.putExtra("homeworkID", homeworkID);
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            startActivity(intent);
//
//                        }
//                    }, 200);
//                }
//
//                if (baseQuickAdapter == assignmentAdapter) {
//                    AssignmentDataModel assignmentDataModel = (AssignmentDataModel) baseQuickAdapter.getItem(i);
//                    final Intent intent = new Intent(getActivity(), AssignmentDetailActivity.class);
//                    String courseID = getCourseDataInstance().getCourseID(getCourseDataInstance().getCourse(assignmentDataModel));
//                    String assignmentID = getCourseDataInstance().getAssignmentID(assignmentDataModel);
//                    intent.putExtra("courseID", courseID);
//                    intent.putExtra("assignmentID", assignmentID);
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            startActivity(intent);
//
//                        }
//                    }, 200);
//                }
//            }
//        };
//
//        quizRecyclerview.addOnItemTouchListener(onItemClickListener);
//        assignmmentRecyclerview.addOnItemTouchListener(onItemClickListener);
//        homeworkRecyclerview.addOnItemTouchListener(onItemClickListener);
//
//        quizRecyclerview.addOnItemTouchListener(new OnItemChildClickListener() {
//            @Override
//            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, final int i) {
//                Timber.d("onSimpleItemChildClick");
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        quizAdapter.remove(i);
//                    }
//                }, 300);
//            }
//        });
//
//
//
//        quizRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//        quizRecyclerview.setAdapter(quizAdapter);
//
//
//        assignmmentRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//        assignmmentRecyclerview.setAdapter(assignmentAdapter);
//
//        homeworkRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//        homeworkRecyclerview.setAdapter(homeworkAdapter);
//
//
//        final View rootview = getActivity().findViewById(R.id.coordinator_layout);
//
//
//        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
//            @Override
//            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
////                Timber.d("onItemSwipeListener onItemSwipeStart");
//            }
//
//            @Override
//            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
//            }
//
//            @Override
//            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, final int pos) {
////                Timber.d("onItemSwipeListener onItemSwiped");
//
//
//                final QuizDataModel quizDataModel = quizAdapter.getItem(pos);
//                String quizID = getCourseDataInstance().getQuizID(quizDataModel);
//                final CourseDataModel courseDataModel = getCourseDataInstance().getCourse(quizDataModel);
//                String courseID = getCourseDataInstance().getCourseID(courseDataModel);
//
//                getCourseDataInstance().deleteQuiz(courseID, quizID);
//
//                quizRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteQuizzes().isEmpty() ? View.GONE : View.VISIBLE);
//                assignmmentRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteAssignments().isEmpty() ? View.GONE : View.VISIBLE);
//                homeworkRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteHomework().isEmpty() ? View.GONE : View.VISIBLE);
//
//                Snackbar.make(rootview, "Quiz deleted!", Snackbar.LENGTH_LONG)
//                        .setAction("Undo", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                getCourseDataInstance().addQuiz(courseDataModel, quizDataModel);
//                            }
//                        })
//                        .show(); // Don’t forget to show!
//            }
//
//            @Override
//            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {
////                Timber.d("onItemSwipeListener onItemSwipeMoving");
//
//            }
//        };
//
//        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(quizAdapter);
//        itemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
//        itemTouchHelper.attachToRecyclerView(quizRecyclerview);
//        quizAdapter.enableSwipeItem();
//        quizAdapter.setOnItemSwipeListener(onItemSwipeListener);
//
//
//        OnItemSwipeListener onItemSwipeListener2 = new OnItemSwipeListener() {
//            @Override
//            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
////                Timber.d("onItemSwipeListener2 onItemSwipeStart");
//
//            }
//
//            @Override
//            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
//            }
//
//            @Override
//            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, final int pos) {
////                Timber.d("onItemSwipeListener2 onItemSwiped");
//
//
//                final AssignmentDataModel assignmentDataModel = assignmentAdapter.getItem(pos);
//                String assignmentID = getCourseDataInstance().getAssignmentID(assignmentDataModel);
//                final CourseDataModel courseDataModel = getCourseDataInstance().getCourse(assignmentDataModel);
//                String courseID = getCourseDataInstance().getCourseID(courseDataModel);
//
//                getCourseDataInstance().deleteAssignment(courseID, assignmentID);
//
//                quizRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteQuizzes().isEmpty() ? View.GONE : View.VISIBLE);
//                assignmmentRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteAssignments().isEmpty() ? View.GONE : View.VISIBLE);
//                homeworkRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteHomework().isEmpty() ? View.GONE : View.VISIBLE);
//
//                Snackbar.make(rootview, "Assignment deleted!", Snackbar.LENGTH_LONG)
//                        .setAction("Undo", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                getCourseDataInstance().addAssignment(courseDataModel, assignmentDataModel);
//
//                            }
//                        })
//                        .show(); // Don’t forget to show!
//            }
//
//            @Override
//            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {
////                Timber.d("onItemSwipeListener2 onItemSwipeMoving");
//
//            }
//        };
//
//        ItemDragAndSwipeCallback itemDragAndSwipeCallback2 = new ItemDragAndSwipeCallback(assignmentAdapter);
//        itemDragAndSwipeCallback2.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
//        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(itemDragAndSwipeCallback2);
//        itemTouchHelper2.attachToRecyclerView(assignmmentRecyclerview);
//        assignmentAdapter.enableSwipeItem();
//        assignmentAdapter.setOnItemSwipeListener(onItemSwipeListener2);
//
//
//        OnItemSwipeListener onItemSwipeListener3 = new OnItemSwipeListener() {
//            @Override
//            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
//            }
//
//            @Override
//            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
//            }
//
//            @Override
//            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, final int pos) {
//
//
//                final HomeworkDataModel homeworkDataModel = homeworkAdapter.getItem(pos);
//                String homeworkID = getCourseDataInstance().getHomeworkID(homeworkDataModel);
//                final CourseDataModel courseDataModel = getCourseDataInstance().getCourse(homeworkDataModel);
//                String courseID = getCourseDataInstance().getCourseID(courseDataModel);
//
//                getCourseDataInstance().deleteHomework(courseID, homeworkID);
//
//                quizRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteQuizzes().isEmpty() ? View.GONE : View.VISIBLE);
//                assignmmentRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteAssignments().isEmpty() ? View.GONE : View.VISIBLE);
//                homeworkRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteHomework().isEmpty() ? View.GONE : View.VISIBLE);
//
//                Snackbar.make(rootview, "Homework deleted!", Snackbar.LENGTH_LONG)
//                        .setAction("Undo", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                getCourseDataInstance().addHomework(courseDataModel, homeworkDataModel);
//
//                            }
//                        })
//                        .show(); // Don’t forget to show!
//            }
//
//            @Override
//            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {
//            }
//        };
//
//        ItemDragAndSwipeCallback itemDragAndSwipeCallback3 = new ItemDragAndSwipeCallback(homeworkAdapter);
//        itemDragAndSwipeCallback3.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
//        ItemTouchHelper itemTouchHelper3 = new ItemTouchHelper(itemDragAndSwipeCallback3);
//        itemTouchHelper3.attachToRecyclerView(homeworkRecyclerview);
//        homeworkAdapter.enableSwipeItem();
//        homeworkAdapter.setOnItemSwipeListener(onItemSwipeListener3);


    }

    public void updateData() {
//        quizAdapter.setNewData(getCourseDataInstance().getListOfIncompleteQuizzes());
//        assignmentAdapter.setNewData(getCourseDataInstance().getListOfIncompleteAssignments());
//        homeworkAdapter.setNewData(getCourseDataInstance().getListOfIncompleteHomework());
//       updateVisibility();
//        quizAdapter.notifyDataSetChanged();
    }

    public void updateVisibility() {
        quizRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteQuizzes().isEmpty() ? View.GONE : View.VISIBLE);
        assignmmentRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteAssignments().isEmpty() ? View.GONE : View.VISIBLE);
        homeworkRecyclerview.setVisibility(getCourseDataInstance().getListOfIncompleteHomework().isEmpty() ? View.GONE : View.VISIBLE);

//        if (quizRecyclerview.getVisibility() == View.GONE
//                && assignmmentRecyclerview.getVisibility() == View.GONE
//                && homeworkRecyclerview.getVisibility() == View.GONE) {
//            placeholder.setVisibility(View.VISIBLE);
//        } else
//            placeholder.setVisibility(View.GONE);
    }

    public void showTutorial() {

        RecyclerView.ViewHolder holder = quizRecyclerview.findViewHolderForAdapterPosition(1);
        View view = holder.itemView;
        final SwitchIconView switchIconView = view.findViewById(R.id.done_checkbox);


        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(switchIconView, "Here you can view all your pending tasks!", "You can swipe any task to delete it. Let's mark this task as completed!")
                        .outerCircleColor(R.color.md_deep_purple_700)
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .transparentTarget(true),         // Specify whether the target is transparent (displays the content underneath)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        switchIconView.performClick();


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new MaterialDialog.Builder(getActivity())
                                        .title("Congratulations!")
                                        .content("You have completed Sensei's tutorial! You can now delete the Test Course added previously!")
                                        .positiveText("OK")
                                        .autoDismiss(true)
                                        .build()
                                        .show();
                            }
                        }, 1000);

                    }

                });

        getActivity().getSharedPreferences(PREFS_NAME, 0).edit().putBoolean("tasks", true).apply();


    }


    public void onStart() {
        super.onStart();

//        updateData();

        String getIntent = getActivity().getIntent().getStringExtra("string");


        if (getIntent != null && !getActivity().getSharedPreferences(PREFS_NAME, 0).getBoolean("tasks", false)) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showTutorial();
                }
            }, 1000);
        }

        adapter.notifyDataSetChanged();
        adapter.expandAllSections();


    }

    @Subscribe
    public void answerAvailable(CourseDataHandler.DataChangedEvent event) {
        Timber.d("event received");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                updateData();
            }
        }, 250);
    }


}
