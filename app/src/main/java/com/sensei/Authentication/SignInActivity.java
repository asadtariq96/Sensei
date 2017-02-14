package com.sensei.Authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.sensei.Activities.Dashboard.DashboardActivity;
import com.sensei.R;

import timber.log.Timber;

import static com.sensei.Application.MyApplication.mAuth;


public class SignInActivity extends AppCompatActivity {

    EditText Email;
    EditText Password;
    Button Signin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        Email = (EditText) findViewById(R.id.email_edittext);
        Password = (EditText) findViewById(R.id.password_edittext);
        Signin = (Button) findViewById(R.id.btn_signin);

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
                    Signin.setEnabled(true);
                else
                    Signin.setEnabled(false);

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

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
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

    private void signIn() {

        final MaterialDialog materialDialog = new MaterialDialog.Builder(SignInActivity.this)
                .title("Signing In")
                .content("Please Wait")
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .build();

        materialDialog.show();


        mAuth.signInWithEmailAndPassword(Email.getText().toString().trim(), Password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Timber.d("signInWithEmail:onComplete:" + task.isSuccessful());
//                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
                            SignInActivity.this.finish();
                            materialDialog.dismiss();
                        } else {
//                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Timber.d("signInWithEmail:failed" + task.getException());

                            Toast.makeText(SignInActivity.this, "Authentication Failed",
                                    Toast.LENGTH_SHORT).show();
                            materialDialog.dismiss();

                        }


                    }
                });

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}