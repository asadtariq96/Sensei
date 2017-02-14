package com.sensei.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sensei.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.sensei.Activities.Assignments.AddAssignmentActivity;
import com.sensei.Activities.Quizzes.AddQuizActivity;
import com.sensei.Activities.AddHomeworkActivity;

import static com.sensei.R.id.date;

/**
 * Created by Asad on 18-Dec-16.
 */

public class DateTimeDialogBuilder {


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

    Context context;

    ArrayAdapter<myDate> dateAdapter;
    ArrayAdapter<myTime> timeAdapter;
    List<myDate> myDateList;
    List<myTime> myTimeList;

    MaterialDialog dialog;
    NDSpinner dateSpinner, timeSpinner;

    View view;

    DatePickerDialog dpd;
    TimePickerDialog tpd;

    Calendar now = Calendar.getInstance();

    LocalDate tempLocalDate = new LocalDate();
    LocalTime localTime;


    private void initAdapters() {

        LocalDate localDate = new LocalDate();
        myDateList = new ArrayList<>();
        myDateList.add(new myDate(localDate, "Today"));
        myDateList.add(new myDate(localDate.plusDays(1), "Tomorrow"));
        myDateList.add(new myDate(localDate.plusDays(7), context.getString(R.string.nextWeek, localDate.dayOfWeek().getAsText())));
        myDateList.add(new myDate("Select a date..."));

        dateAdapter = new ArrayAdapter<myDate>(context, android.R.layout.simple_spinner_item, myDateList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View v = vi.inflate(android.R.layout.simple_spinner_item, null);
                v.setPadding(0, v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
                final TextView t = (TextView) v.findViewById(android.R.id.text1);
                t.setText(myDateList.get(position).getValue());
                return v;
            }
        };
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        myTimeList = new ArrayList<>();
        myTimeList.add(new myTime(new LocalTime(8, 0), "Morning"));
        myTimeList.add(new myTime(new LocalTime(13, 0), "Afternoon"));
        myTimeList.add(new myTime(new LocalTime(18, 0), "Evening"));
        myTimeList.add(new myTime(new LocalTime(21, 0), "Night"));
        myTimeList.add(new myTime("Select a time..."));
        timeAdapter = new ArrayAdapter<myTime>(context, android.R.layout.simple_spinner_item, myTimeList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) { //spinner selected item view
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View v = vi.inflate(android.R.layout.simple_spinner_item, null);
                v.setPadding(0, v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
                final TextView t = (TextView) v.findViewById(android.R.id.text1);
                t.setText(myTimeList.get(position).getValue());
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) { //spinner dropdown item view
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    }

    private void initSpinners() {
        dateSpinner = (NDSpinner) view.findViewById(date);
        dateSpinner.setAdapter(dateAdapter);
        dateSpinner.setTag(0);
        dateSpinner.setSelection(0);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ((Integer) dateSpinner.getTag() == i) {
                    return;
                }


                dateSpinner.setTag(i);

                if (i == 3) {
                    dpd.show(((Activity) context).getFragmentManager(), "Datepickerdialog");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dateSpinner.setSelection(0);
            }
        });

        timeSpinner = (NDSpinner) view.findViewById(R.id.time);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setTag(0);
        timeSpinner.setSelection(0);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ((Integer) timeSpinner.getTag() == i) {
                    return;
                }


                timeSpinner.setTag(i);

                if (i == 4) {
                    tpd.show(((Activity) context).getFragmentManager(), "Timepickerdialog");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                timeSpinner.setSelection(0);
            }
        });

    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener onDateSetListener;

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                LocalDate localDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);
                myDateList.get(3).setLocalDate(localDate);
                dateAdapter.notifyDataSetChanged();
                dateSpinner.setAdapter(dateAdapter);
                dateSpinner.setSelection(3);
                dateSpinner.setTag(3);

            }
        };


        dpd = DatePickerDialog.newInstance(
                onDateSetListener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

    }

    private void initTimePicker() {
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

        tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false);


    }

    public void BuildReminderDialog(final Context context, final TextInputEditText Reminder, final AddQuizActivity activity) {
        this.context = context;
//        if (RootView.findViewById(R.id.reminder_container).getVisibility() == View.VISIBLE) {
//            dialog = new MaterialDialog.Builder(context)
//                    .title("Add reminder")
//                    .customView(R.layout.reminder_dialog_custom_layout, false)
//                    .positiveText("SAVE")
//                    .onPositive(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            RootView.findViewById(R.id.reminder_container).setVisibility(View.VISIBLE);
//
//                        }
//                    })
//                    .neutralText("DELETE")
//                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            dialog.dismiss();
//                            RootView.findViewById(R.id.reminder_container).setVisibility(GONE);
//                        }
//                    })
//                    .negativeText("CANCEL").build();
//        } else {
//
//            dialog = new MaterialDialog.Builder(context)
//                    .title("Add reminder")
//                    .customView(R.layout.reminder_dialog_custom_layout, false)
//                    .positiveText("SAVE")
//                    .onPositive(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            RootView.findViewById(R.id.reminder_container).setVisibility(View.VISIBLE);
//
//                        }
//                    })
//                    .negativeText("CANCEL").build();
//        }

        dialog = new MaterialDialog.Builder(context)
                .title("Add reminder")
                .customView(R.layout.reminder_dialog_custom_layout, false)
                .positiveText("SAVE")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (dateSpinner.getSelectedItemId() == 3) {
                            Reminder.setText(((myDate) dateSpinner.getSelectedItem()).getValue() + ", " + ((myTime) timeSpinner.getSelectedItem()).getValue());
                        } else
                            Reminder.setText(dateSpinner.getSelectedItem().toString() + ", " + ((myTime) timeSpinner.getSelectedItem()).getValue());

                        LocalDate tempLocalDate = ((myDate) dateSpinner.getSelectedItem()).obj;
                        LocalTime tempLocalTime = ((myTime) timeSpinner.getSelectedItem()).obj;


                        (activity).ReminderDateTime = new DateTime(
                                tempLocalDate.getYear(),
                                tempLocalDate.getMonthOfYear(),
                                tempLocalDate.getDayOfMonth(),
                                tempLocalTime.getHourOfDay(),
                                tempLocalTime.getMinuteOfHour());
                        Toast.makeText(context, activity.ReminderDateTime.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
//                .neutralText("DELETE")
//                .onNeutral(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        dialog.dismiss();
////                        RootView.findViewById(R.id.reminder_container).setVisibility(GONE);
//                    }
//                })
                .negativeText("CANCEL").build();

        view = dialog.getCustomView();

        initAdapters();
        initSpinners();
        initDatePicker();
        initTimePicker();


        dialog.show();
    }

    public void BuildReminderDialog(final Context context, final TextInputEditText Reminder, final AddAssignmentActivity activity) {
        this.context = context;
//        if (RootView.findViewById(R.id.reminder_container).getVisibility() == View.VISIBLE) {
//            dialog = new MaterialDialog.Builder(context)
//                    .title("Add reminder")
//                    .customView(R.layout.reminder_dialog_custom_layout, false)
//                    .positiveText("SAVE")
//                    .onPositive(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            RootView.findViewById(R.id.reminder_container).setVisibility(View.VISIBLE);
//
//                        }
//                    })
//                    .neutralText("DELETE")
//                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            dialog.dismiss();
//                            RootView.findViewById(R.id.reminder_container).setVisibility(GONE);
//                        }
//                    })
//                    .negativeText("CANCEL").build();
//        } else {
//
//            dialog = new MaterialDialog.Builder(context)
//                    .title("Add reminder")
//                    .customView(R.layout.reminder_dialog_custom_layout, false)
//                    .positiveText("SAVE")
//                    .onPositive(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            RootView.findViewById(R.id.reminder_container).setVisibility(View.VISIBLE);
//
//                        }
//                    })
//                    .negativeText("CANCEL").build();
//        }

        dialog = new MaterialDialog.Builder(context)
                .title("Add reminder")
                .customView(R.layout.reminder_dialog_custom_layout, false)
                .positiveText("SAVE")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (dateSpinner.getSelectedItemId() == 3) {
                            Reminder.setText(((myDate) dateSpinner.getSelectedItem()).getValue() + ", " + ((myTime) timeSpinner.getSelectedItem()).getValue());
                        } else
                            Reminder.setText(dateSpinner.getSelectedItem().toString() + ", " + ((myTime) timeSpinner.getSelectedItem()).getValue());

                        LocalDate tempLocalDate = ((myDate) dateSpinner.getSelectedItem()).obj;
                        LocalTime tempLocalTime = ((myTime) timeSpinner.getSelectedItem()).obj;


                        (activity).ReminderDateTime = new DateTime(
                                tempLocalDate.getYear(),
                                tempLocalDate.getMonthOfYear(),
                                tempLocalDate.getDayOfMonth(),
                                tempLocalTime.getHourOfDay(),
                                tempLocalTime.getMinuteOfHour());
                        Toast.makeText(context, activity.ReminderDateTime.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
//                .neutralText("DELETE")
//                .onNeutral(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        dialog.dismiss();
////                        RootView.findViewById(R.id.reminder_container).setVisibility(GONE);
//                    }
//                })
                .negativeText("CANCEL").build();

        view = dialog.getCustomView();

        initAdapters();
        initSpinners();
        initDatePicker();
        initTimePicker();


        dialog.show();
    }

    public void BuildReminderDialog(final Context context, final TextInputEditText Reminder, final AddHomeworkActivity activity) {
        this.context = context;
//        if (RootView.findViewById(R.id.reminder_container).getVisibility() == View.VISIBLE) {
//            dialog = new MaterialDialog.Builder(context)
//                    .title("Add reminder")
//                    .customView(R.layout.reminder_dialog_custom_layout, false)
//                    .positiveText("SAVE")
//                    .onPositive(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            RootView.findViewById(R.id.reminder_container).setVisibility(View.VISIBLE);
//
//                        }
//                    })
//                    .neutralText("DELETE")
//                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            dialog.dismiss();
//                            RootView.findViewById(R.id.reminder_container).setVisibility(GONE);
//                        }
//                    })
//                    .negativeText("CANCEL").build();
//        } else {
//
//            dialog = new MaterialDialog.Builder(context)
//                    .title("Add reminder")
//                    .customView(R.layout.reminder_dialog_custom_layout, false)
//                    .positiveText("SAVE")
//                    .onPositive(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            RootView.findViewById(R.id.reminder_container).setVisibility(View.VISIBLE);
//
//                        }
//                    })
//                    .negativeText("CANCEL").build();
//        }

        dialog = new MaterialDialog.Builder(context)
                .title("Add reminder")
                .customView(R.layout.reminder_dialog_custom_layout, false)
                .positiveText("SAVE")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (dateSpinner.getSelectedItemId() == 3) {
                            Reminder.setText(((myDate) dateSpinner.getSelectedItem()).getValue() + ", " + ((myTime) timeSpinner.getSelectedItem()).getValue());
                        } else
                            Reminder.setText(dateSpinner.getSelectedItem().toString() + ", " + ((myTime) timeSpinner.getSelectedItem()).getValue());

                        LocalDate tempLocalDate = ((myDate) dateSpinner.getSelectedItem()).obj;
                        LocalTime tempLocalTime = ((myTime) timeSpinner.getSelectedItem()).obj;


                        (activity).ReminderDateTime = new DateTime(
                                tempLocalDate.getYear(),
                                tempLocalDate.getMonthOfYear(),
                                tempLocalDate.getDayOfMonth(),
                                tempLocalTime.getHourOfDay(),
                                tempLocalTime.getMinuteOfHour());
                        Toast.makeText(context, activity.ReminderDateTime.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
//                .neutralText("DELETE")
//                .onNeutral(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        dialog.dismiss();
////                        RootView.findViewById(R.id.reminder_container).setVisibility(GONE);
//                    }
//                })
                .negativeText("CANCEL").build();

        view = dialog.getCustomView();

        initAdapters();
        initSpinners();
        initDatePicker();
        initTimePicker();


        dialog.show();
    }


    public void DueDatePicker(final TextInputEditText DueDate, final AddQuizActivity activity) {
        DatePickerDialog.OnDateSetListener onDateSetListener;

        onDateSetListener =new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {


                tempLocalDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);

                (activity).DueDateTime = new DateTime(
                        tempLocalDate.getYear(),
                        tempLocalDate.getMonthOfYear(),
                        tempLocalDate.getDayOfMonth(),
                        (localTime == null) ? 0 : localTime.getHourOfDay(),
                        (localTime == null) ? 0 : localTime.getMinuteOfHour());

                DueDate.setText(tempLocalDate.toString("dd MMMM, yyyy"));
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(addQuizActivity, addQuizActivity.DueDateTime.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }, 2000);


            }
        };


        dpd = DatePickerDialog.newInstance(
                onDateSetListener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(activity.getFragmentManager(), "Datepickerdialog");


    }

    public void DueDatePicker(final TextInputEditText DueDate, final AddAssignmentActivity activity) {
        DatePickerDialog.OnDateSetListener onDateSetListener;

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {


                tempLocalDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);

                (activity).DueDateTime = new DateTime(
                        tempLocalDate.getYear(),
                        tempLocalDate.getMonthOfYear(),
                        tempLocalDate.getDayOfMonth(),
                        (localTime == null) ? 0 : localTime.getHourOfDay(),
                        (localTime == null) ? 0 : localTime.getMinuteOfHour());

                DueDate.setText(tempLocalDate.toString("dd MMMM, yyyy"));
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(addAssignmentActivity, addAssignmentActivity.DueDateTime.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }, 2000);


            }
        };


        dpd = DatePickerDialog.newInstance(
                onDateSetListener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(activity.getFragmentManager(), "Datepickerdialog");


    }

    public void DueDatePicker(final TextInputEditText DueDate, final AddHomeworkActivity activity) {
        DatePickerDialog.OnDateSetListener onDateSetListener;

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {


                tempLocalDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);

                (activity).DueDateTime = new DateTime(
                        tempLocalDate.getYear(),
                        tempLocalDate.getMonthOfYear(),
                        tempLocalDate.getDayOfMonth(),
                        (localTime == null) ? 0 : localTime.getHourOfDay(),
                        (localTime == null) ? 0 : localTime.getMinuteOfHour());

                DueDate.setText(tempLocalDate.toString("dd MMMM, yyyy"));
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(addAssignmentActivity, addAssignmentActivity.DueDateTime.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }, 2000);


            }
        };


        dpd = DatePickerDialog.newInstance(
                onDateSetListener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(activity.getFragmentManager(), "Datepickerdialog");


    }

    public void DueTimePicker(final TextInputEditText DueTime, final AddQuizActivity activity) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener;
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                localTime = new LocalTime(hourOfDay, minute, second);

                (activity).DueDateTime = new DateTime(
                        tempLocalDate.getYear(),
                        tempLocalDate.getMonthOfYear(),
                        tempLocalDate.getDayOfMonth(),
                        hourOfDay,
                        minute);

                DueTime.setText(localTime.toString("h:mm a"));

//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(addQuizActivity, addQuizActivity.DueDateTime.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }, 2000);


            }
        };

        tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false);

        tpd.show(activity.getFragmentManager(), "Timepickerdialog");


    }

    public void DueTimePicker(final TextInputEditText DueTime, final AddAssignmentActivity activity) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener;
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                localTime = new LocalTime(hourOfDay, minute, second);

                (activity).DueDateTime = new DateTime(
                        tempLocalDate.getYear(),
                        tempLocalDate.getMonthOfYear(),
                        tempLocalDate.getDayOfMonth(),
                        hourOfDay,
                        minute);

                DueTime.setText(localTime.toString("h:mm a"));

//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(addQuizActivity, addQuizActivity.DueDateTime.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }, 2000);


            }
        };

        tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false);

        tpd.show(activity.getFragmentManager(), "Timepickerdialog");


    }

    public void DueTimePicker(final TextInputEditText DueTime, final AddHomeworkActivity activity) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener;
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                localTime = new LocalTime(hourOfDay, minute, second);

                (activity).DueDateTime = new DateTime(
                        tempLocalDate.getYear(),
                        tempLocalDate.getMonthOfYear(),
                        tempLocalDate.getDayOfMonth(),
                        hourOfDay,
                        minute);

                DueTime.setText(localTime.toString("h:mm a"));

//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(addQuizActivity, addQuizActivity.DueDateTime.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }, 2000);


            }
        };

        tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false);

        tpd.show(activity.getFragmentManager(), "Timepickerdialog");


    }


}
