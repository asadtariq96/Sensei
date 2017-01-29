package com.sensei.Application;

import android.graphics.Color;

import org.joda.time.LocalTime;

import java.util.Random;

/**
 * Created by Asad on 17-Dec-16.
 */

public class Constants {

    final public static int[] COLORS_LIST = new int[]{
            Color.parseColor("#D32F2F"),
            Color.parseColor("#C2185B"),
            Color.parseColor("#7B1FA2"),
            Color.parseColor("#512DA8"),
            Color.parseColor("#303F9F"),
            Color.parseColor("#1976D2"),
            Color.parseColor("#0288D1"),
            Color.parseColor("#0097A7"),
            Color.parseColor("#00796B"),
            Color.parseColor("#388E3C"),
            Color.parseColor("#689F38"),
            Color.parseColor("#A4B42B"),
            Color.parseColor("#FBC02D"),
            Color.parseColor("#FFA000"),
            Color.parseColor("#F57C00"),
            Color.parseColor("#E64A19"),
            Color.parseColor("#5D4037"),
            Color.parseColor("#616161"),
            Color.parseColor("#455A64")

    };

    final public static int getRandomColor() {

        int rnd = new Random().nextInt(COLORS_LIST.length);
        return COLORS_LIST[rnd];
    }

    public static int DEFAULT_CLASS_LENGTH = 50;
    public static int DEFAULT_BREAK_LENGTH = 5;
    public static LocalTime DEFAULT_START_TIME = new LocalTime(8, 0, 0);
    public static LocalTime DEFAULT_END_TIME = new LocalTime(16, 0, 0);

//    public static int MONDAY = 1;
//    public static int TUESDAY = 2;
//    public static int WEDNESDAY = 3;
//    public static int THURSDAY = 4;
//    public static int FRIDAY = 5;
//    public static int SATURDAY = 6;

    public static int REQUEST_CODE_EDIT_CLASS = 100;
    public static int REQUEST_CODE_EDIT_COURSE = 200;
    public static int REQUEST_CODE_ADD_CLASS = 300;
    public static int RESULT_CODE_FINISH_ACTIVITY = 400;
//    public static int REQUEST_CODE_CLASS_DETAILS = 101;
//    public static int REQUEST_CODE_COURSE_DETAILS = 201;

    public static int getDayLength() {
        return DEFAULT_END_TIME.getHourOfDay() - DEFAULT_START_TIME.getHourOfDay();

    }

}
