package com.sensei.assistant.DataModelClasses;

/**
 * Created by Asad on 14-Feb-17.
 */

public class SemesterDataModel {
    String semesterName;
    String startDate;
    String endDate;

    public SemesterDataModel() {
    }

    public SemesterDataModel(String semesterName, String startDate, String endDate) {
        this.semesterName = semesterName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SemesterDataModel that = (SemesterDataModel) o;

        if (semesterName != null ? !semesterName.equals(that.semesterName) : that.semesterName != null)
            return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null)
            return false;
        return endDate != null ? endDate.equals(that.endDate) : that.endDate == null;

    }

    @Override
    public int hashCode() {
        int result = semesterName != null ? semesterName.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
