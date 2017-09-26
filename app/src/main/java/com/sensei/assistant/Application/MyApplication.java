package com.sensei.assistant.Application;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.sensei.assistant.BuildConfig;
import com.squareup.otto.Bus;

import net.danlew.android.joda.JodaTimeAndroid;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by Asad on 02-Nov-16.
 */

public class MyApplication extends android.app.Application {

    //    public static List<CourseDataModel> CourseList;
    private static Context mContext;

    public static FirebaseAuth mAuth;
    public static FirebaseUser firebaseUser;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    public static FirebaseDatabase database;
    public static DatabaseReference databaseReference;
    public static DatabaseReference coursesReference;
    public static DatabaseReference settingsReference;
    public static DatabaseReference semestersReference;
    public static DatabaseReference classesReference;
    public static DatabaseReference quizzesReference;
    public static DatabaseReference assignmentsReference;
    public static DatabaseReference homeworkReference;
    public static String UID;
    public static Bus bus;
    public static boolean isUserSettingsLoaded = false;

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;


        Fabric.with(this, new Crashlytics());
        Iconify.with(new FontAwesomeModule());
        JodaTimeAndroid.init(this);


        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
            Timber.d("Timber Tree Planted");

        }

        bus = new Bus();


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        databaseReference = database.getReference();
        classesReference = databaseReference.child("classes");
        classesReference.keepSynced(true);
        quizzesReference = databaseReference.child("quizzes");
        quizzesReference.keepSynced(true);
        assignmentsReference = databaseReference.child("assignments");
        assignmentsReference.keepSynced(true);
        homeworkReference = databaseReference.child("homework");
        homeworkReference.keepSynced(true);
//        databaseReference.child("courses").keepSynced(true);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                Log.d("ApplicationClass", "onAuthStateChanged");
                Timber.d("onAuthStateChanged");
                isUserSettingsLoaded = false;
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    UID = firebaseUser.getUid();

                    settingsReference = databaseReference.child("settings").child(UID);
                    settingsReference.keepSynced(true);

                    semestersReference = databaseReference.child("semesters").child(UID);
                    semestersReference.keepSynced(true);


                    getCourseDataInstance().getUserSettings();

//                    Log.d("ApplicationClass", "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                    Timber.d("onAuthStateChanged:signed_in:" + firebaseUser.getUid());

                } else {
                    Timber.d("onAuthStateChanged:signed_out");

                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

//        CourseList = new ArrayList<>();
//        CourseList.add(new CourseDataModel("Signals and Systems", Constants.getRandomColor()));
//        CourseList.add(new CourseDataModel("Microcontroller and Microprocessor Based Design", Constants.getRandomColor()));
//        CourseList.add(new CourseDataModel("Numerical Methods", Constants.getRandomColor()));
//        CourseList.add(new CourseDataModel("Computer Networks", Constants.getRandomColor()));
//        CourseList.add(new CourseDataModel("Database Management", Constants.getRandomColor()));


    }


}
