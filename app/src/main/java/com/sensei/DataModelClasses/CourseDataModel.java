package com.sensei.DataModelClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asad on 17-Dec-16.
 */

public class CourseDataModel {
    private String CourseName;
    private String CourseAbbreviation;
    private int CourseColorCode;
    private List<ClassDataModel> classesList = new ArrayList<>();


    public CourseDataModel() {
    }

    public CourseDataModel(String CourseName, String courseAbbreviation, int CourseColorCode) {
        this.CourseName = CourseName;
        this.CourseColorCode = CourseColorCode;
        this.CourseAbbreviation = courseAbbreviation;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getCourseAbbreviation() {
        return CourseAbbreviation;
    }

    public void setCourseAbbreviation(String courseAbbreviation) {
        CourseAbbreviation = courseAbbreviation;
    }

    public int getCourseColorCode() {
        return CourseColorCode;
    }

    public void setCourseColorCode(int courseColorCode) {
        CourseColorCode = courseColorCode;
    }

    public List<ClassDataModel> getClassesList() {
        return classesList;
    }
}
