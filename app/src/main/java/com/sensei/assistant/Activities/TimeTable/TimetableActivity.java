package com.sensei.assistant.Activities.TimeTable;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.Gson;
import com.sensei.assistant.DataModelClasses.ClassDataModel;
import com.sensei.assistant.DataModelClasses.CourseDataModel;
import com.sensei.assistant.R;
import com.sensei.assistant.Utils.NavigationDrawerSetup;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

import static com.sensei.assistant.Application.Constants.DEFAULT_START_TIME;
import static com.sensei.assistant.Application.Constants.getDayLength;
import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

public class TimetableActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener {
    private static final int RC_STORAGE = 784;
    private NavigationView navigationView;
    private NavigationDrawerSetup navigationDrawerSetup;
    private WeekView weekView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Timetable");

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationDrawerSetup = new NavigationDrawerSetup(drawerLayout, toolbar, navigationView, this);

        weekView = findViewById(R.id.weekView);
        weekView.setMonthChangeListener(this);
        weekView.setXScrollingSpeed(0.0f);
        DateTime dateTime = new DateTime().dayOfWeek().withMinimumValue();
        weekView.goToDate(dateTime.toCalendar(Locale.getDefault()));
        weekView.goToHour(DEFAULT_START_TIME.getHourOfDay());

        weekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());

                return weekday.toUpperCase();
            }

            @Override
            public String interpretTime(int hour) {
                LocalTime localTime = new LocalTime(hour, 0);
                return localTime.toString("h a");
            }
        });


        weekView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = weekView.getMeasuredWidth();
                int height = weekView.getHeight();
                Timber.d("weekview height %d", height);
                Timber.d("weekview measured height %d", weekView.getMeasuredHeight());
                Timber.d("daylength %d", getDayLength());
                //TODO calculate hours dynamically
                weekView.setHourHeight(height / getDayLength() - 50);
//                weekView.setHourHeight(200);
                Timber.d("hour height %d", weekView.getHourHeight());

            }
        });


    }

    public void onStart() {
        super.onStart();
        navigationDrawerSetup.ConfigureDrawer();
        weekView.notifyDatasetChanged();
    }

    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(2).setChecked(true);
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        Timber.d("onMonthChange year:%d,month:%d", newYear, newMonth);

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }


        ).start();


        int counter = 0;
        List<WeekViewEvent> eventsList = new ArrayList<>();


        for (CourseDataModel courseDataModel : getCourseDataInstance().CoursesList) {
            for (ClassDataModel classDataModel : courseDataModel.getClasses()) {
                {
                    DateTime startOfPrevMonth = new DateTime().minusMonths(1).dayOfMonth().withMinimumValue().withTimeAtStartOfDay();
                    DateTime endOfNextMonth = startOfPrevMonth.plusMonths(2).dayOfMonth().withMaximumValue().withTimeAtStartOfDay();
                    int dayOfWeek = classDataModel.getDayOfWeek();

                    for (DateTime loopVar = startOfPrevMonth;
                         loopVar.isBefore(endOfNextMonth) || loopVar.isEqual(endOfNextMonth);
                         loopVar = loopVar.plusDays(1)) {

                        if (loopVar.getDayOfWeek() == dayOfWeek) {
                            DateTime start = new DateTime(loopVar.getYear(),
                                    loopVar.getMonthOfYear(),
                                    loopVar.getDayOfMonth(),
                                    classDataModel.getStartTimeOriginal().getHourOfDay(),
                                    classDataModel.getStartTimeOriginal().getMinuteOfHour());

                            DateTime end = new DateTime(loopVar.getYear(),
                                    loopVar.getMonthOfYear(),
                                    loopVar.getDayOfMonth(),
                                    classDataModel.getEndTimeOriginal().getHourOfDay(),
                                    classDataModel.getEndTimeOriginal().getMinuteOfHour());


                            Calendar startCalender = start.toCalendar(Locale.getDefault());
                            Calendar endCalender = end.toCalendar(Locale.getDefault());

                            WeekViewEvent event = new WeekViewEvent(++counter,
                                    courseDataModel.getCourseAbbreviation(),
                                    classDataModel.getLocation(),
                                    startCalender, endCalender);

                            event.setColor((courseDataModel.getCourseColorCode() & 0x00FFFFFF) | 0xBF000000);

                            eventsList.add(event);
                        }


                    }
                }

            }
        }

        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : eventsList) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event);
            }
        }


        Gson gson = new Gson();
        String temp = gson.toJson(matchedEvents);
        Timber.d(temp);
        Timber.d("matchedEvents length:%d", matchedEvents.size());

        return matchedEvents;


//        return eventsList;


    }

    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        //noinspection WrongConstant
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1)
                || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void store(Bitmap bm, String fileName) {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        shareImage(file);
    }

    private void shareImage(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(TimetableActivity.this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timetable_refresh, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle arrow click here
        if (item.getItemId() == R.id.refresh) {
            weekView.notifyDatasetChanged();
        }
        if (item.getItemId() == R.id.screenshot) {
            Timber.d("screenshot");
            String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(this, perms)) {

                store(screenShot(findViewById(R.id.rootview)), DateTime.now().toString() + ".jpg");
            } else {
                EasyPermissions.requestPermissions(this, "Please grant permission to save screenshot", RC_STORAGE, perms);
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
