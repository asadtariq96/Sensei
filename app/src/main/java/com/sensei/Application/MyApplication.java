package com.sensei.Application;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

import com.sensei.DataModelClasses.CourseDataModel;
import com.sensei.Startup.SplashActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by Asad on 02-Nov-16.
 */

public class MyApplication extends android.app.Application {

    public static List<CourseDataModel> CourseList;
    public static FirebaseAuth mAuth;
    public static FirebaseUser firebaseUser;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    public static FirebaseDatabase database;
    public static DatabaseReference databaseReference;
    public static String UID;


    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new FontAwesomeModule());
        JodaTimeAndroid.init(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        databaseReference = database.getReference();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d("ApplicationClass", "onAuthStateChanged");
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    UID = firebaseUser.getUid();
                    Log.d("ApplicationClass", "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                } else {
                    Log.d("ApplicationClass", "onAuthStateChanged:signed_out");
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
