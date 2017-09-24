package com.sensei.assistant.Startup;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.sensei.assistant.Activities.Dashboard.DashboardActivity;
import com.sensei.assistant.Authentication.SignInActivity;
import com.sensei.assistant.R;

import org.json.JSONObject;


import static com.sensei.assistant.Application.MyApplication.firebaseUser;


public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Intent signInIntent = new Intent(SplashActivity.this, SignInActivity.class);
        final Intent dashboardIntent = new Intent(SplashActivity.this, DashboardActivity.class);
//        Shimmer shimmer = new Shimmer();
//        ShimmerTextView shimmerTextView = (ShimmerTextView) findViewById(R.id.shimmer_text_view);
//        shimmer.start(shimmerTextView);
//
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (firebaseUser != null) {
                    SplashActivity.this.startActivity(dashboardIntent);
                    SplashActivity.this.finish();
                } else {
                    SplashActivity.this.startActivity(signInIntent);
                    SplashActivity.this.finish();
                }
            }
        }, 1500);
    }

    @Override
    public void onStart() {
        super.onStart();
//        Log.d("Splash", "mAuth.addAuthStateListener");
//        mAuth.addAuthStateListener(mAuthListener);

    }


    @Override
    public void onStop() {
        super.onStop();
//        if (mAuthListener != null) {
//            Log.d("Splash", "mAuth.removeAuthStateListener");
//
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
    }

}
