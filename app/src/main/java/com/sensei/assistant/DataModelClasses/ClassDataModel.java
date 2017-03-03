package com.sensei.assistant.DataModelClasses;

import com.google.firebase.database.Exclude;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

/**
 * Created by Asad on 01-Jan-17.
 */
public class ClassDataModel {
    private int dayOfWeek;
    private String startTime;
    private String endTime;
    private String location;
    private ClassType classType;

    public ClassDataModel() {
    }



    public ClassDataModel(int dayOfWeek, String startTime, String endTime, String location, ClassType classType) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.location = location;
        this.classType = classType;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Exclude
    public LocalTime getStartTimeOriginal() {
        return (startTime == null) ? null : new LocalTime().parse(startTime);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Exclude
    public LocalTime getEndTimeOriginal() {

        return (endTime == null) ? null : new LocalTime().parse(endTime);
    }

    @Exclude
    public String getDuration() {
        int minutes = Minutes.minutesBetween(getStartTimeOriginal(), getEndTimeOriginal()).getMinutes();
        return String.valueOf(minutes) + " mins";
    }

    public String getEndTime() {
        return endTime.toString();
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Exclude
    public ClassType getClassTypeOriginal() {
        return classType;
    }

    public String getClassType() {
        return classType.toString();
    }

    public void setClassType(String classTypeString) {
        this.classType = ClassType.valueOf(classTypeString);
    }

    public enum ClassType {
        LECTURE,
        LAB
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        ClassDataModel that = (ClassDataModel) o;
//
//        if (dayOfWeek != that.dayOfWeek) return false;
//        if (!startTime.equals(that.startTime)) return false;
//        if (!endTime.equals(that.endTime)) return false;
//        if (!location.equals(that.location)) return false;
//        return classType == that.classType;
//
//    }
//
//    @Override
//    public int hashCode() {
//        int result = dayOfWeek;
//        result = 31 * result + startTime.hashCode();
//        result = 31 * result + endTime.hashCode();
//        result = 31 * result + location.hashCode();
//        result = 31 * result + classType.hashCode();
//        return result;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassDataModel that = (ClassDataModel) o;

        if (dayOfWeek != that.dayOfWeek) return false;
        if (!startTime.equals(that.startTime)) return false;
        if (!endTime.equals(that.endTime)) return false;
        if (location != null ? !location.equals(that.location) : that.location != null)
            return false;
        return classType == that.classType;

    }

    @Override
    public int hashCode() {
        int result = dayOfWeek;
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + classType.hashCode();
        return result;
    }
}
