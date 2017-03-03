package com.sensei.assistant.Activities.Quizzes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sensei.assistant.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.sensei.assistant.Utils.NDSpinner;

import static android.view.View.GONE;
import static com.sensei.assistant.R.id.date;

public class QuizDetailActivityOld extends AppCompatActivity {

    private MenuItem PinTaskMenuButton;
    private boolean isPinned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Linear Circuit Analysis");
        toolbar.setSubtitle("Quiz");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton add = (ImageButton) findViewById(R.id.button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddMenu(view);
            }
        });

        ImageButton options = (ImageButton) findViewById(R.id.button_options);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionsMenu(view);
            }
        });

//        findViewById(R.id.reminder_container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showReminderDialog();
//
//            }
//        });

    }


    private void showAddMenu(View v) {

//        new BottomSheet.Builder(this).sheet(R.menu.add_contents_task_detail).listener(new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                }
//            }
//        }).show();
    }

    private void showOptionsMenu(View v) {

//        new BottomSheet.Builder(this).sheet(R.menu.options_task_detail).listener(new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                }
//            }
//        }).show();
    }

    private void showReminderDialog() {
        class myDate {
            LocalDate obj;
            String dropdownValue;

            myDate(LocalDate obj, String dropdownValue) {
                this.obj = obj;
                this.dropdownValue = dropdownValue;
            }

            myDate(String dropdownValue) {
                this.dropdownValue = dropdownValue;
            }

            void setLocalDate(LocalDate localDate) {
                this.obj = localDate;
            }

            String getText() {
                return dropdownValue;
            }

            String getValue() {
                if (obj != null)
                    return obj.toString("dd MMMM");
                else
                    return "";
            }

            @Override //used to display the dropdown item text
            public String toString() {
                return getText();
            }
        }
        class myTime {
            LocalTime obj;
            String dropdownTime;
            String dropdownString;

            myTime(LocalTime obj, String dropdownString) {
                this.obj = obj;
                this.dropdownTime = obj.toString("h:mm a");
                this.dropdownString = dropdownString;
            }

            myTime(String dropdownString) {
                this.dropdownString = dropdownString;
            }

            void setLocalTime(LocalTime localTime) {
                this.obj = localTime;
            }

            String getText() {
                return dropdownString;
            }

            String getValue() {
                if (obj != null)
                    return obj.toString("h:mm a");
                else
                    return "";
            }

            @Override //used to display the dropdown item text
            public String toString() {
                return getText();
            }
        }

        final MaterialDialog dialog;

        if (findViewById(R.id.reminder_container).getVisibility() == View.VISIBLE) {
            dialog = new MaterialDialog.Builder(this)
                    .title("Add reminder")
                    .customView(R.layout.reminder_dialog_custom_layout, false)
                    .positiveText("SAVE")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            findViewById(R.id.reminder_container).setVisibility(View.VISIBLE);

                        }
                    })
                    .neutralText("DELETE")
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            findViewById(R.id.reminder_container).setVisibility(GONE);
                        }
                    })
                    .negativeText("CANCEL").build();
        } else {

            dialog = new MaterialDialog.Builder(this)
                    .title("Add reminder")
                    .customView(R.layout.reminder_dialog_custom_layout, false)
                    .positiveText("SAVE")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            findViewById(R.id.reminder_container).setVisibility(View.VISIBLE);

                        }
                    })
                    .negativeText("CANCEL").build();
        }


        final View view = dialog.getCustomView();


        LocalDate localDate = new LocalDate();
        final List<myDate> myDateList = new ArrayList<>();
        myDateList.add(new myDate(localDate, "Today"));
        myDateList.add(new myDate(localDate.plusDays(1), "Tomorrow"));
        myDateList.add(new myDate(localDate.plusDays(7), getString(R.string.nextWeek, localDate.dayOfWeek().getAsText())));
        myDateList.add(new myDate("Select a date..."));


        final ArrayAdapter<myDate> dateAdapter = new ArrayAdapter<myDate>(this, android.R.layout.simple_spinner_item, myDateList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View v = vi.inflate(android.R.layout.simple_spinner_item, null);
                v.setPadding(0, v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
                final TextView t = (TextView) v.findViewById(android.R.id.text1);
                t.setText(myDateList.get(position).getValue());
                return v;
            }
        };

        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final NDSpinner dateSpinner = (NDSpinner) view.findViewById(date);
        dateSpinner.setAdapter(dateAdapter);
        dateSpinner.setTag(0);
        dateSpinner.setSelection(0);

        DatePickerDialog.OnDateSetListener onDateSetListener;
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                LocalDate localDate = new LocalDate(year, ++monthOfYear, dayOfMonth);
                myDateList.get(3).setLocalDate(localDate);
                dateAdapter.notifyDataSetChanged();
                dateSpinner.setAdapter(dateAdapter);
                dateSpinner.setSelection(3);
                dateSpinner.setTag(3);

            }
        };

        Calendar now = Calendar.getInstance();
        final DatePickerDialog dpd = DatePickerDialog.newInstance(
                onDateSetListener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ((Integer) dateSpinner.getTag() == i) {
                    return;
                }


                dateSpinner.setTag(i);

                if (i == 3) {
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dateSpinner.setSelection(0);
            }
        });


        final List<myTime> myTimeList = new ArrayList<>();
        myTimeList.add(new myTime(new LocalTime(8, 0), "Morning"));
        myTimeList.add(new myTime(new LocalTime(13, 0), "Afternoon"));
        myTimeList.add(new myTime(new LocalTime(18, 0), "Evening"));
        myTimeList.add(new myTime(new LocalTime(21, 0), "Night"));
        myTimeList.add(new myTime("Select a time..."));

        final ArrayAdapter<myTime> timeAdapter = new ArrayAdapter<myTime>(this, android.R.layout.simple_spinner_item, myTimeList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) { //spinner selected item view
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View v = vi.inflate(android.R.layout.simple_spinner_item, null);
                v.setPadding(0, v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
                final TextView t = (TextView) v.findViewById(android.R.id.text1);
                t.setText(myTimeList.get(position).getValue());
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) { //spinner dropdown item view
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View v = vi.inflate(R.layout.time_dropdown_row, null);
                v.setPadding(0, v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
                final TextView t = (TextView) v.findViewById(R.id.text1);
                final TextView t2 = (TextView) v.findViewById(R.id.text2);
                t.setText(myTimeList.get(position).getText());
                if (position != 4)
                    t2.setText(myTimeList.get(position).getValue());
                return v;
            }
        };

        timeAdapter.setDropDownViewResource(R.layout.time_dropdown_row);


        final NDSpinner timeSpinner = (NDSpinner) view.findViewById(R.id.time);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setTag(0);
        timeSpinner.setSelection(0);

        TimePickerDialog.OnTimeSetListener onTimeSetListener;
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                LocalTime localTime = new LocalTime(hourOfDay, minute, second);
                myTimeList.get(4).setLocalTime(localTime);
                timeAdapter.notifyDataSetChanged();
                timeSpinner.setAdapter(timeAdapter);
                timeSpinner.setSelection(4);
                timeSpinner.setTag(4);
            }
        };

        final TimePickerDialog tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false);


        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ((Integer) timeSpinner.getTag() == i) {
                    return;
                }


                timeSpinner.setTag(i);

                if (i == 4) {
                    tpd.show(getFragmentManager(), "Timepickerdialog");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                timeSpinner.setSelection(0);
            }
        });


        dialog.show();

    }

    private void updatePinStatus() {
        if (isPinned) {
            PinTaskMenuButton.setIcon(R.drawable.ic_bookmark_white_24dp);
            Toast.makeText(QuizDetailActivityOld.this, "Task Pinned!", Toast.LENGTH_SHORT).show();
        } else {
            PinTaskMenuButton.setIcon(R.drawable.ic_bookmark_border_white_24dp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        if (item.getItemId() == R.id.pin) {
            isPinned = !isPinned;
            updatePinStatus();

        }

        if (item.getItemId() == R.id.reminder) {

            showReminderDialog();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_detail_menu, menu);
        PinTaskMenuButton = menu.findItem(R.id.pin);
        updatePinStatus();
        return true;
    }

}
