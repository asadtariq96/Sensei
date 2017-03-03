package com.sensei.assistant.Activities.Settings;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sensei.assistant.Application.Constants;
import com.sensei.assistant.DataModelClasses.SemesterDataModel;
import com.sensei.assistant.DataModelClasses.UserSettings;
import com.sensei.assistant.R;
import com.sensei.assistant.Utils.NavigationDrawerSetup;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

import static android.view.View.GONE;
import static com.sensei.assistant.Application.Constants.DEFAULT_BREAK_LENGTH;
import static com.sensei.assistant.Application.Constants.DEFAULT_CLASS_LENGTH;
import static com.sensei.assistant.Application.Constants.DEFAULT_END_TIME;
import static com.sensei.assistant.Application.Constants.DEFAULT_START_TIME;
import static com.sensei.assistant.Application.Constants.SELECTED_SEMESTER;
import static com.sensei.assistant.Application.MyApplication.UID;
import static com.sensei.assistant.Application.MyApplication.databaseReference;
import static com.sensei.assistant.Application.MyApplication.firebaseUser;
import static com.sensei.assistant.Application.MyApplication.mAuth;
import static com.sensei.assistant.Application.MyApplication.semestersReference;
import static com.sensei.assistant.Application.MyApplication.settingsReference;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, NumberPickerDialogFragment.NumberPickerDialogHandlerV2, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 34343;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawerSetup navigationDrawerSetup;

    LinearLayout selectedSemester;
    LinearLayout classLength;
    LinearLayout breakLength;
    LinearLayout dayStartTime;
    LinearLayout dayEndTime;
    LinearLayout newSemester;
    LinearLayout linkFacebook;
    LinearLayout linkGoogle;
    TextView selectedSemesterText;
    TextView classLengthText;
    TextView breakLengthText;
    TextView dayStartTimeText;
    TextView dayEndTimeText;

    LoginButton loginButton;
    CallbackManager mCallbackManager;


    final int BREAK_REF = 100;
    final int CLASS_LENGTH_REF = 200;

    LocalDate semesterStartDate = new LocalDate();
    LocalDate semesterEndDate;

    List<SemesterDataModel> SemesterList = new ArrayList<>();
    Map<String, SemesterDataModel> SemesterMap = new ArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mCallbackManager = CallbackManager.Factory.create();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationDrawerSetup = new NavigationDrawerSetup(drawerLayout, toolbar, navigationView, this);

        selectedSemester = (LinearLayout) findViewById(R.id.selected_semester);
        breakLength = (LinearLayout) findViewById(R.id.break_length);
        classLength = (LinearLayout) findViewById(R.id.class_length);
        dayStartTime = (LinearLayout) findViewById(R.id.day_start_time);
        dayEndTime = (LinearLayout) findViewById(R.id.day_end_time);
        newSemester = (LinearLayout) findViewById(R.id.add_new_semester);
        linkFacebook = (LinearLayout) findViewById(R.id.link_facebook_account);
        linkGoogle = (LinearLayout) findViewById(R.id.link_google_account);

        selectedSemesterText = (TextView) findViewById(R.id.selected_semester_text);
        breakLengthText = (TextView) findViewById(R.id.break_length_text);
        classLengthText = (TextView) findViewById(R.id.class_length_text);
        dayStartTimeText = (TextView) findViewById(R.id.day_start_time_text);
        dayEndTimeText = (TextView) findViewById(R.id.day_end_time_text);

        selectedSemester.setOnClickListener(this);
        classLength.setOnClickListener(this);
        breakLength.setOnClickListener(this);
        dayStartTime.setOnClickListener(this);
        dayEndTime.setOnClickListener(this);
        newSemester.setOnClickListener(this);
        linkFacebook.setOnClickListener(this);
        linkGoogle.setOnClickListener(this);


        selectedSemesterText.setText(Constants.SELECTED_SEMESTER_NAME);
        breakLengthText.setText(Constants.DEFAULT_BREAK_LENGTH + " Minutes");
        classLengthText.setText(Constants.DEFAULT_CLASS_LENGTH + " Minutes");
        dayStartTimeText.setText(DEFAULT_START_TIME.toString("h:mm a"));
        dayEndTimeText.setText(DEFAULT_END_TIME.toString("h:mm a"));
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
                Timber.d("facebook:onError" + error.toString());
                // ...
            }
        });


        settingsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final UserSettings userSettings = dataSnapshot.getValue(UserSettings.class);
                Constants.DEFAULT_START_TIME = new LocalTime().parse(userSettings.getStartTime());
                Constants.DEFAULT_END_TIME = new LocalTime().parse(userSettings.getEndTime());
                Constants.DEFAULT_BREAK_LENGTH = userSettings.getBreakBetweenClasses();
                Constants.DEFAULT_CLASS_LENGTH = userSettings.getClassLength();


                if (!Objects.equals(userSettings.getSelectedSemester(), Constants.SELECTED_SEMESTER))

                {
                    Constants.SELECTED_SEMESTER = userSettings.getSelectedSemester();

                    Query selectedSemesterNameQuery = semestersReference.orderByKey().equalTo(userSettings.getSelectedSemester());
                    selectedSemesterNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Constants.SELECTED_SEMESTER_NAME = dataSnapshot.child(userSettings.getSelectedSemester()).getValue(SemesterDataModel.class).getSemesterName();
                            selectedSemesterText.setText(Constants.SELECTED_SEMESTER_NAME);
                            getCourseDataInstance().getCourses();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        semestersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SemesterList.clear();
                SemesterMap.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SemesterList.add(postSnapshot.getValue(SemesterDataModel.class));
                    SemesterMap.put(postSnapshot.getKey(), postSnapshot.getValue(SemesterDataModel.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void onStart() {
        super.onStart();
        navigationDrawerSetup.ConfigureDrawer();
    }

    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(8).setChecked(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selected_semester:
                changeCurrentSemester();
                break;
            case R.id.class_length:
                changeClassLength();
                break;
            case R.id.break_length:
                changeBreak();
                break;
            case R.id.day_start_time:
                changeDayStart();
                break;
            case R.id.day_end_time:
                changeDayEnd();
                break;
            case R.id.add_new_semester:
                addNewSemester();
                break;
            case R.id.link_facebook_account:
                if (firebaseUser.getProviders().contains(("facebook.com")))
                    Toast.makeText(SettingsActivity.this, "Account already linked!", Toast.LENGTH_SHORT).show();
                else
                    loginButton.performClick();


//                FirebaseAuth.getInstance().getCurrentUser().unlink("facebook.com")
//                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (!task.isSuccessful()) {
//                                    // Auth provider unlinked from account
//                                }
//                            }
//                        });
                break;
            case R.id.link_google_account:
                linkGoogleAccount();


        }
    }

    private void linkGoogleAccount() {


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("490046406670-njv1rn90jcv7dpsolg6ab7upa4k4vhc0.apps.googleusercontent.com")
                .requestEmail()
                .build();
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

        Timber.d("handleFacebookAccessToken:" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        firebaseUser.linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Timber.d("linkWithCredential:onComplete:" + task.isSuccessful());


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Timber.d("Authentication Failed:" + task.getException().toString());
                            Toast.makeText(SettingsActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    private void addNewSemester() {
        final TextInputEditText semesterName;
        final LinearLayout startDate;
        final LinearLayout endDate;
        final TextView startDateText;
        final TextView endDateText;
        final MaterialDialog addNewSemesterDialog = new MaterialDialog.Builder(this)
                .title("Add new semester")
                .customView(R.layout.add_new_semester_dialog_view, false)
                .positiveText("Save")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        SemesterDataModel semesterDataModel = new SemesterDataModel();
                        semesterDataModel.setSemesterName(((TextInputEditText) dialog.getCustomView().findViewById(R.id.semester_name)).getText().toString().trim());
                        semesterDataModel.setStartDate(semesterStartDate.toString());
                        semesterDataModel.setEndDate(semesterEndDate == null ? "" : semesterEndDate.toString());
                        String key = databaseReference.child("semesters").child(UID).push().getKey();
                        databaseReference.child("semesters").child(UID).child(key).setValue(semesterDataModel);
//                        SemesterMap.put(key, semesterDataModel);

                    }
                })
                .negativeText("Cancel")
                .show();

        addNewSemesterDialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);


        final View view = addNewSemesterDialog.getCustomView();
        semesterName = (TextInputEditText) view.findViewById(R.id.semester_name);
        startDate = (LinearLayout) view.findViewById(R.id.start_date_container);
        endDate = (LinearLayout) view.findViewById(R.id.end_date_container);
        startDateText = (TextView) view.findViewById(R.id.start_date);
        startDateText.setText(semesterStartDate.toString("EEE, dd MMM yyyy"));
        endDateText = (TextView) view.findViewById(R.id.end_date);

        semesterName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty())
                    addNewSemesterDialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                else
                    addNewSemesterDialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);

            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog.OnDateSetListener onDateSetListener;
                onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        LocalDate localDate = new LocalDate(year, ++monthOfYear, dayOfMonth);
                        if (semesterEndDate != null && localDate.isAfter(semesterEndDate)) {
                            Toast.makeText(SettingsActivity.this, "Oops! Starting date can not be after ending date ", Toast.LENGTH_SHORT).show();
                        } else {
                            semesterStartDate = localDate;
                            startDateText.setText(semesterStartDate.toString("EEE, dd MMM yyyy"));
                        }


                    }
                };


                Calendar now = Calendar.getInstance();

                final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        onDateSetListener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                datePickerDialog.show(getFragmentManager(), "tpd");

            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog.OnDateSetListener onDateSetListener;
                onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        LocalDate localDate = new LocalDate(year, ++monthOfYear, dayOfMonth);
                        if (localDate.isBefore(semesterStartDate)) {
                            Toast.makeText(SettingsActivity.this, "Oops! Starting date can not be after ending date ", Toast.LENGTH_SHORT).show();
                        } else {
                            semesterEndDate = localDate;
                            endDateText.setText(semesterEndDate.toString("EEE, dd MMM yyyy"));
                        }


                    }
                };


                Calendar now = Calendar.getInstance();

                final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        onDateSetListener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                datePickerDialog.show(getFragmentManager(), "tpd");

            }
        });


    }

    void changeCurrentSemester() {


        final SemesterAdapter adapter = new SemesterAdapter(SemesterList, SemesterMap);
        adapter.setCallbacks(
                new SemesterAdapter.SemesterCallback() {
                    @Override
                    public void onItemClicked(int itemIndex) {


                    }
                });

        new MaterialDialog.Builder(this)
                .title("Select Semester")
                .adapter(adapter, null)
                .positiveText("DONE")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        int pos = adapter.selectedPosition;
                        int previous = SemesterList.indexOf(SemesterMap.get(SELECTED_SEMESTER));
                        if (pos != previous) {
                            settingsReference.child("selectedSemester").setValue(getSemesterID(SemesterList.get(pos)));

                        }


                    }
                })
                .show();


//        final int index = semesterNames.indexOf(SemesterMap.get(SELECTED_SEMESTER).getSemesterName());
//
//        MaterialDialog dialog = new MaterialDialog.Builder(this)
//                .title("Select Semester")
//                .items(semesterNames)
//                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
//                    @Override
//                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                        if (which != index) {
//
//                        }
//                        return true;
//                    }
//                })
//                .show();
//
//        dialog.setSelectedIndex(index);

    }

    void changeClassLength() {
        NumberPickerBuilder npb = new NumberPickerBuilder()
                .setCurrentNumber(Constants.DEFAULT_CLASS_LENGTH)
                .setMinNumber(BigDecimal.valueOf(0))
                .setDecimalVisibility(GONE)
                .setPlusMinusVisibility(GONE)
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .setLabelText("Minutes")
                .setReference(CLASS_LENGTH_REF);
        npb.show();
    }

    void changeBreak() {


        NumberPickerBuilder npb = new NumberPickerBuilder()
                .setCurrentNumber(Constants.DEFAULT_BREAK_LENGTH)
                .setMinNumber(BigDecimal.valueOf(0))
                .setDecimalVisibility(GONE)
                .setPlusMinusVisibility(GONE)
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .setLabelText("Minutes")
                .setReference(BREAK_REF);
        npb.show();


//        new MaterialDialog.Builder(this)
//                .title("Break between classes")
//                .content("Enter break in minutes")
//                .inputType(InputType.TYPE_CLASS_NUMBER)
//                .inputRangeRes(1, 3, R.color.md_red_500)
//
//                .input(null, String.valueOf(Constants.DEFAULT_BREAK_LENGTH), new MaterialDialog.InputCallback() {
//                    @Override
//                    public void onInput(MaterialDialog dialog, CharSequence input) {
//                        if (input.toString().isEmpty()) {
//                            Toast.makeText(SettingsActivity.this, "Text field empty.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            breakLengthText.setText(input.toString() + " Minutes");
//                            databaseReference.child("settings").child(UID).child("breakBetweenClasses").setValue(Integer.valueOf(input.toString()));
//                        }
//                    }
//                }).show();
    }

    void changeDayStart() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener;

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {


                LocalTime localTime = new LocalTime(hourOfDay, minute, second);

                if (localTime.isAfter(DEFAULT_END_TIME)) {
                    Toast.makeText(SettingsActivity.this, "Starting time can not be after ending time", Toast.LENGTH_SHORT).show();
                } else {
                    DEFAULT_START_TIME = localTime;
                    dayStartTimeText.setText(localTime.toString("h:mm a"));
                    databaseReference.child("settings").child(UID).child("startTime").setValue(localTime.toString());
                }

            }
        };

        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                onTimeSetListener,
                DEFAULT_START_TIME.getHourOfDay(),
                DEFAULT_START_TIME.getMinuteOfHour(),
                false);

        timePickerDialog.show(getFragmentManager(), "tpd");
    }

    void changeDayEnd() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener;

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {


                LocalTime localTime = new LocalTime(hourOfDay, minute, second);

                if (localTime.isBefore(DEFAULT_START_TIME)) {
                    Toast.makeText(SettingsActivity.this, "Ending time can not be before starting time", Toast.LENGTH_SHORT).show();
                } else {
                    DEFAULT_END_TIME = localTime;
                    dayEndTimeText.setText(localTime.toString("h:mm a"));
                    databaseReference.child("settings").child(UID).child("endTime").setValue(localTime.toString());
                }

            }
        };

        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                onTimeSetListener,
                DEFAULT_END_TIME.getHourOfDay(),
                DEFAULT_END_TIME.getMinuteOfHour(),
                false);

        timePickerDialog.show(getFragmentManager(), "tpd");
    }

    @Override
    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
        if (reference == BREAK_REF) {
            DEFAULT_BREAK_LENGTH = number.intValue();
            breakLengthText.setText(number.intValue() + " Minutes");
            databaseReference.child("settings").child(UID).child("breakBetweenClasses").setValue(number.intValue());
        } else if (reference == CLASS_LENGTH_REF) {
            DEFAULT_CLASS_LENGTH = number.intValue();
            classLengthText.setText(number.intValue() + " Minutes");
            databaseReference.child("settings").child(UID).child("classLength").setValue(number.intValue());
        }

    }

    String getSemesterID(SemesterDataModel semesterDataModel) {
        for (Map.Entry<String, SemesterDataModel> entry : SemesterMap.entrySet()) {
            if (semesterDataModel.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseLinkWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void firebaseLinkWithGoogle(GoogleSignInAccount account) {
        Timber.d("firebaseLinkWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Timber.d("signInWithCredential:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {

                            Toast.makeText(SettingsActivity.this, "Account linked!", Toast.LENGTH_SHORT).show();
                        }

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Timber.d("signInWithCredential:" + task.getException().toString());
                            Toast.makeText(SettingsActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
}
