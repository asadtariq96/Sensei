package com.sensei.assistant.DataModelClasses;

/**
 * Created by Asad on 11-Feb-17.
 */

public class UserSettings {
    String selectedSemester;
    String startTime;
    String endTime;
    int breakBetweenClasses;
    int classLength;

    public String getSelectedSemester() {
        return selectedSemester;
    }

    public void setSelectedSemester(String selectedSemester) {
        this.selectedSemester = selectedSemester;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getBreakBetweenClasses() {
        return breakBetweenClasses;
    }

    public void setBreakBetweenClasses(int breakBetweenClasses) {
        this.breakBetweenClasses = breakBetweenClasses;
    }

    public int getClassLength() {
        return classLength;
    }

    public void setClassLength(int classLength) {
        this.classLength = classLength;
    }
}
