package com.sensei.Application;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

import com.sensei.BuildConfig;
import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.Startup.SplashActivity;
import com.squareup.otto.Bus;

import static android.content.ContentValues.TAG;
import static com.sensei.DataHandlers.CourseDataHandler.getCourseDataInstance;

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
    public static String UID;
    public static Bus bus;

    public static Context getContext() {
        return mContext;
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


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                Log.d("ApplicationClass", "onAuthStateChanged");
                Timber.d("onAuthStateChanged");
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    UID = firebaseUser.getUid();
                    getCourseDataInstance().addChildListener();

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
