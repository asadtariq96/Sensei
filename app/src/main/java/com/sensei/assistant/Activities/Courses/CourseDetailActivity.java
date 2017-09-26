package com.sensei.assistant.Activities.Courses;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.sensei.assistant.Activities.Classes.AddClassActivity;
import com.sensei.assistant.Activities.Classes.ClassDetailsActivity;
import com.sensei.assistant.Adapters.CourseClassesAdapter;
import com.sensei.assistant.DataModelClasses.ClassDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.R;

import timber.log.Timber;

import static android.view.View.GONE;
import static com.sensei.assistant.Application.Constants.REQUEST_CODE_EDIT_CLASS;
import static com.sensei.assistant.Application.Constants.REQUEST_CODE_EDIT_COURSE;
import static com.sensei.assistant.Application.Constants.RESULT_CODE_FINISH_ACTIVITY;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class CourseDetailActivity extends AppCompatActivity {
    String courseID;
    CourseDataModel courseDataModel;
    RecyclerView recyclerView;
    CourseClassesAdapter adapter;

    TextView CourseName;
    TextView CourseInstructor;
    TextView CreditHours;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        Intent intent = getIntent();
        courseID = intent.getStringExtra("courseID");
        courseDataModel = getCourseDataInstance().CoursesID.get(courseID);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Course Details");
        getSupportActionBar().setSubtitle(courseDataModel.getCourseAbbreviation());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CourseName = findViewById(R.id.course_name);
        CourseInstructor = findViewById(R.id.course_instructor);
        CreditHours = findViewById(R.id.credit_hours);


        recyclerView = findViewById(R.id.classes_recyclerview);

        adapter = new CourseClassesAdapter(R.layout.class_layout, getCourseDataInstance().getListOfClassesForCourse(courseDataModel));

//        View footerView = View.inflate(getApplicationContext(), R.layout.add_class_button_layout, null);
        View footerView = getLayoutInflater().inflate(R.layout.add_class_button_layout, (ViewGroup) findViewById(android.R.id.content), false);
        adapter.addFooterView(footerView);
        footerView.findViewById(R.id.add_class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetailActivity.this, AddClassActivity.class);
                intent.putExtra("courseID", courseID);
                startActivity(intent);
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(CourseDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {


            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                ClassDataModel classDataModel = getCourseDataInstance().getListOfClassesForCourse(courseDataModel).get(i);
                final Intent intent = new Intent(CourseDetailActivity.this, ClassDetailsActivity.class);
                String classID = getCourseDataInstance().getClassID(classDataModel);
                intent.putExtra("courseID", courseID);
                intent.putExtra("classID", classID);


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivityForResult(intent, REQUEST_CODE_EDIT_CLASS);


                    }
                }, 200);

            }

        });

        recyclerView.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                adapter.remove(i);
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.edit_class:
                Intent intent = new Intent(CourseDetailActivity.this, EditCourseDetailsActivity.class);
                intent.putExtra("courseID", courseID);
                startActivityForResult(intent, REQUEST_CODE_EDIT_COURSE);
                break;

//            case R.id.share:
//                shareCourse();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.class_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onStart() {
        Timber.d("onstart, calling updateFields");
        super.onStart();
        findViewById(R.id.toolbar).setBackgroundColor(courseDataModel.getCourseColorCode());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(courseDataModel.getDarkerColor());
        }
        adapter.setNewData(getCourseDataInstance().getListOfClassesForCourse(courseDataModel));
        CourseName.setText(courseDataModel.getCourseName());
        if (courseDataModel.getInstructor().isEmpty())
            ((View) CourseInstructor.getParent().getParent()).setVisibility(GONE);
        else {
            ((View) CourseInstructor.getParent().getParent()).setVisibility(View.VISIBLE);
            CourseInstructor.setText(courseDataModel.getInstructor());

        }

        if (courseDataModel.getCreditHours() == 0)
            ((View) CreditHours.getParent().getParent()).setVisibility(GONE);
        else {
            ((View) CreditHours.getParent().getParent()).setVisibility(View.VISIBLE);
            CreditHours.setText(String.valueOf(courseDataModel.getCreditHours()));

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EDIT_CLASS) {
            if (resultCode == RESULT_CODE_FINISH_ACTIVITY)
                finish();
        }

        if (requestCode == REQUEST_CODE_EDIT_COURSE) {
            if (resultCode == RESULT_CODE_FINISH_ACTIVITY) {
                finish();
            }
        }
    }

    public void shareCourse() {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.senseiassistant.com")
                .appendQueryParameter("courseID", courseID);
        String myUrl = builder.build().toString();

        Timber.d(myUrl);


        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(myUrl))
                .setDynamicLinkDomain("kwd65.app.goo.gl")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        Timber.d(dynamicLinkUri.toString());


        Intent sendIntent = new Intent();
//        String msg = "Hey, check this out: " + shortLink;
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, dynamicLinkUri.toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

//        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLink(Uri.parse(myUrl))
//                .setDynamicLinkDomain("kwd65.app.goo.gl")
//                // Set parameters
//                // ...
//                .buildShortDynamicLink()
//                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
//                    @Override
//                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
//                        if (task.isSuccessful()) {
//                            // Short link created
//                            Uri shortLink = task.getResult().getShortLink();
//                            Uri flowchartLink = task.getResult().getPreviewLink();
//
//                            Timber.d(shortLink.toString());
//
//                            Intent sendIntent = new Intent();
//                            String msg = "Hey, check this out: " + shortLink;
//                            sendIntent.setAction(Intent.ACTION_SEND);
//                            sendIntent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
//                            sendIntent.setType("text/plain");
//                            startActivity(sendIntent);
//
//
//                        } else {
//                            Timber.d("error:" + task.getException());
//                            // Error
//                            // ...
//                        }
//                    }
//                });


//        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLink(Uri.parse(courseID))
//                .setDynamicLinkDomain("kwd65.app.goo.gl")
//                // Set parameters
//                // ...
//                .buildShortDynamicLink()
//                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
//                    @Override
//                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
//                        if (task.isSuccessful()) {
//                            Timber.d("task successful");
//                            // Short link created
//                            Uri shortLink = task.getResult().getShortLink();
//                            Uri flowchartLink = task.getResult().getPreviewLink();
//
//                            Intent sendIntent = new Intent();
//                            String msg = "Hey, check this out: " + shortLink;
//                            sendIntent.setAction(Intent.ACTION_SEND);
//                            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
//                            sendIntent.setType("text/plain");
//                            startActivity(sendIntent);
//
//                        } else {
//                            Timber.d(task.getException().toString());
//                            // Error
//                            // ...
//                        }
//                    }
//                });

//        Uri dynamicLinkUri = dynamicLink.getUri();


    }
}
