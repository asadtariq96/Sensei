package com.sensei.assistant.Activities.Homework;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.github.clans.fab.FloatingActionButton;
import com.sensei.assistant.Activities.Assignments.AddAssignmentActivity;
import com.sensei.assistant.Activities.Assignments.AssignmentsListActivity;
import com.sensei.assistant.Activities.Quizzes.QuizDetailActivity;
import com.sensei.assistant.Activities.Quizzes.QuizzesListActivity;
import com.sensei.assistant.Adapters.CoursesListAdapterBRVAH;
import com.sensei.assistant.Adapters.DashboardHomeworkAdapter;
import com.sensei.assistant.Adapters.DashboardQuizAdapter;
import com.sensei.assistant.DataHandlers.CourseDataHandler;
import com.sensei.assistant.DataModelClasses.AssignmentDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.DataModelClasses.HomeworkDataModel;
import com.sensei.assistant.DataModelClasses.QuizDataModel;
import com.sensei.assistant.R;
import com.sensei.assistant.Utils.NavigationDrawerSetup;
import com.squareup.otto.Subscribe;

import timber.log.Timber;

import static com.sensei.assistant.Application.MyApplication.bus;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class HomeworkListActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawerSetup navigationDrawerSetup;
    private RecyclerView recyclerView;
    public DashboardHomeworkAdapter adapter;
    private FloatingActionButton addHomeworkFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        bus.register(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Homework");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationDrawerSetup = new NavigationDrawerSetup(drawerLayout, toolbar, navigationView, this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        addHomeworkFAB = findViewById(R.id.add_homework);
        adapter = new DashboardHomeworkAdapter(R.layout.quiz_layout, getCourseDataInstance().getListOfHomework());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnItemTouchListener(new OnItemClickListener() {


            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                HomeworkDataModel homeworkDataModel = getCourseDataInstance().getListOfHomework().get(i);


                final Intent intent = new Intent(HomeworkListActivity.this, HomeworkDetailActivity.class);
                String courseID = getCourseDataInstance().getCourseID(getCourseDataInstance().getCourse(homeworkDataModel));
                String homeworkID = getCourseDataInstance().getHomeworkID(homeworkDataModel);
                intent.putExtra("courseID", courseID);
                intent.putExtra("homeworkID", homeworkID);
//                Parcelable classObject = Parcels.wrap(classDataModel);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("classObject", classObject);
//                Parcelable courseObject = Parcels.wrap(getCourseDataInstance().getCourseOfClass(classDataModel));
//                bundle.putParcelable("courseObject", courseObject);
//                intent.putExtras(bundle);


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(intent);

                    }
                }, 200);

            }

        });

        addHomeworkFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeworkListActivity.this, AddHomeworkActivity.class));
            }
        });

        final View rootview = findViewById(R.id.rootview);

        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, final int pos) {

                final HomeworkDataModel homeworkDataModel = adapter.getItem(pos);
                String homeworkID = getCourseDataInstance().getHomeworkID(homeworkDataModel);
                final CourseDataModel courseDataModel = getCourseDataInstance().getCourse(homeworkDataModel);
                String courseID = getCourseDataInstance().getCourseID(courseDataModel);

                getCourseDataInstance().deleteHomework(courseID, homeworkID);

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
//                canvas.drawColor(ContextCompat.getColor(QuizzesListActivity.this, R.color.colorPrimaryLight));
//                canvas.drawText("Just some text", 0, 40, paint);
            }
        };

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        itemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter.enableSwipeItem();
        adapter.setOnItemSwipeListener(onItemSwipeListener);

    }

    @Subscribe
    public void answerAvailable(CourseDataHandler.DataChangedEvent event) {
        adapter.setNewData(getCourseDataInstance().getListOfHomework());
        Timber.d("event received");
    }

    public void onStart() {
        super.onStart();
//        coursesRef.addValueEventListener(valueEventListener);
        navigationDrawerSetup.ConfigureDrawer();
        adapter.setNewData(getCourseDataInstance().getListOfHomework());
    }

    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(6).setChecked(true);
    }
}
