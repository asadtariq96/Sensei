package com.sensei.Startup;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sensei.Activities.DashboardActivity;
import com.sensei.Activities.SignInActivity;
import com.sensei.R;

import static com.sensei.Application.MyApplication.firebaseUser;
import static com.sensei.Application.MyApplication.mAuth;


public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Intent signInIntent = new Intent(SplashActivity.this, SignInActivity.class);
        final Intent dashboardIntent = new Intent(SplashActivity.this, DashboardActivity.class);

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
        }, 2000);
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
