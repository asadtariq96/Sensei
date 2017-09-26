package com.sensei.assistant.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.sensei.assistant.Activities.Dashboard.DashboardActivity;
import com.sensei.assistant.R;

import timber.log.Timber;

import static com.sensei.assistant.Application.MyApplication.isUserSettingsLoaded;
import static com.sensei.assistant.Application.MyApplication.mAuth;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class SignupActivity extends AppCompatActivity {

    EditText Email, Password;
    Button signupBtn;
    MaterialDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Email = findViewById(R.id.email_edittext);
        Password = findViewById(R.id.password_edittext);
        signupBtn = findViewById(R.id.btn_signup);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (emailValidation() == 1 && !Password.getText().toString().equals(""))
                    signupBtn.setEnabled(true);
                else
                    signupBtn.setEnabled(false);

            }
        };


        Email.addTextChangedListener(textWatcher);
        Password.addTextChangedListener(textWatcher);

        Email.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            int result = emailValidation();
                            if (result == -1)
                                Email.setError("Please enter a valid email address");
                            else
                                Email.setError(null);
                        }
                    }
                }

        );

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        progressDialog = new MaterialDialog.Builder(SignupActivity.this)
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .content("Signing up...")
                .build();
    }

    private void signUp() {

        progressDialog.show();


        mAuth.createUserWithEmailAndPassword(Email.getText().toString().trim(), Password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            launchDashboardActivity();
                            Timber.d("createUserWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Timber.w("createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                        // ...
                    }
                });
    }

    public void launchDashboardActivity() {

        if (isUserSettingsLoaded) {
            progressDialog.dismiss();
            SignupActivity.this.finish();
            startActivity(new Intent(SignupActivity.this, DashboardActivity.class));
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        getCourseDataInstance().registerSignUpActivityInstance(SignupActivity.this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getCourseDataInstance().unregisterSignUpActivityInstance();
    }


    private int emailValidation() {

        String email = Email.getText().toString();
        if (email.isEmpty()) {
            Email.setError(null);
            return 0;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return -1;
        } else {
            Email.setError(null);
            return 1;

        }

    }
}
