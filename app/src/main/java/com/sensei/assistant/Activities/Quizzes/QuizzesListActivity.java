package com.sensei.assistant.Activities.Quizzes;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.sensei.assistant.Activities.Classes.ClassDetailsActivity;
import com.sensei.assistant.Activities.Courses.CourseDetailActivity;
import com.sensei.assistant.Activities.Courses.CoursesListActivity;
import com.sensei.assistant.Adapters.DashboardQuizAdapter;
import com.sensei.assistant.DataHandlers.CourseDataHandler;
import com.sensei.assistant.DataModelClasses.ClassDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.DataModelClasses.QuizDataModel;
import com.sensei.assistant.R;
import com.sensei.assistant.Utils.NavigationDrawerSetup;
import com.squareup.otto.Subscribe;

import timber.log.Timber;

import static com.sensei.assistant.Application.MyApplication.bus;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;
import static org.joda.time.DateTimeConstants.MONDAY;

public class QuizzesListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawerSetup navigationDrawerSetup;
    private RecyclerView recyclerView;
    public DashboardQuizAdapter adapter;
    private FloatingActionButton addQuizFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes);
        bus.register(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quizzes");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationDrawerSetup = new NavigationDrawerSetup(drawerLayout, toolbar, navigationView, this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        addQuizFAB = findViewById(R.id.add_quiz);
        adapter = new DashboardQuizAdapter(R.layout.quiz_layout, getCourseDataInstance().getListOfQuizzes());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {


            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                QuizDataModel quizDataModel = getCourseDataInstance().getListOfQuizzes().get(i);


                final Intent intent = new Intent(QuizzesListActivity.this, QuizDetailActivity.class);
                String courseID = getCourseDataInstance().getCourseID(getCourseDataInstance().getCourse(quizDataModel));
                String quizID = getCourseDataInstance().getQuizID(quizDataModel);
                intent.putExtra("courseID", courseID);
                intent.putExtra("quizID", quizID);
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


        addQuizFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuizzesListActivity.this, AddQuizActivity.class));
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

                final QuizDataModel quizDataModel = adapter.getItem(pos);
                String quizID = getCourseDataInstance().getQuizID(quizDataModel);
                final CourseDataModel courseDataModel = getCourseDataInstance().getCourse(quizDataModel);
                String courseID = getCourseDataInstance().getCourseID(courseDataModel);

                getCourseDataInstance().deleteQuiz(courseID, quizID);

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
        adapter.setNewData(getCourseDataInstance().getListOfQuizzes());
        Timber.d("event received");
    }

    public void onStart() {
        Timber.d("onStart");
        super.onStart();
//        coursesRef.addValueEventListener(valueEventListener);
        navigationDrawerSetup.ConfigureDrawer();
        adapter.setNewData(getCourseDataInstance().getListOfQuizzes());
    }

    public void onResume() {
        Timber.d("onResume");

        super.onResume();
        navigationView.getMenu().getItem(4).setChecked(true); //select home by default in navigation drawer
    }
}
