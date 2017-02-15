package com.sensei.Authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sensei.Activities.Dashboard.DashboardActivity;
import com.sensei.R;

import timber.log.Timber;

import static com.sensei.Application.MyApplication.mAuth;


public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 4545;
    EditText Email;
    EditText Password;
    Button Signin;
    Button facebookLogin;
    Button googleLogin;
    LoginButton loginButton;
//    SignInButton googleSignInButton;
    CallbackManager mCallbackManager;
    public GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mCallbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("490046406670-njv1rn90jcv7dpsolg6ab7upa4k4vhc0.apps.googleusercontent.com")

                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Email = (EditText) findViewById(R.id.email_edittext);
        Password = (EditText) findViewById(R.id.password_edittext);
        Signin = (Button) findViewById(R.id.btn_signin);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Timber.d("facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Timber.d("facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Timber.d("facebook:onError", error);
                // ...
            }
        });

        facebookLogin = (Button) findViewById(R.id.login_facebook);
        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });

//        googleSignInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(SignInActivity.this, "clicked", Toast.LENGTH_SHORT).show();
//                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//                startActivityForResult(signInIntent, RC_SIGN_IN);
//            }
//        });

        googleLogin = (Button) findViewById(R.id.login_google);
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(SignInActivity.this, "googleLoginClicked", Toast.LENGTH_SHORT).show();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
//                googleSignInButton.performClick();
            }
        });


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

    private void handleFacebookAccessToken(AccessToken token) {
        Timber.d("handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());


        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Timber.d("signInWithCredential:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
                            SignInActivity.this.finish();
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Timber.d("signInWithCredential" + task.getException().toString());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Timber.d("firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Timber.d("signInWithCredential:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {

                            startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
                            SignInActivity.this.finish();
                        }

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Timber.d("signInWithCredential:" + task.getException().toString());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}