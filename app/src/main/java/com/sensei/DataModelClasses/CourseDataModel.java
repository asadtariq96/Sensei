package com.sensei.DataModelClasses;

import android.graphics.Color;

import com.google.firebase.database.Exclude;


import java.util.ArrayList;
import java.util.List;

import static android.R.attr.factor;
import static android.R.attr.installLocation;

/**
 * Created by Asad on 17-Dec-16.
 */
public class CourseDataModel {
    private String CourseName;
    private String CourseAbbreviation;
    private int CourseColorCode;
    private String Instructor;
    private int CreditHours;
    private List<ClassDataModel> classes = new ArrayList<>();
    private List<QuizDataModel> quizzes = new ArrayList<>();
    private List<AssignmentDataModel> assignments = new ArrayList<>();


    public CourseDataModel() {
    }

    public CourseDataModel(String CourseName, String courseAbbreviation, int CourseColorCode, String CourseInstructor, int CreditHours) {
        this.CourseName = CourseName;
        this.CourseColorCode = CourseColorCode;
        this.CourseAbbreviation = courseAbbreviation;
        this.Instructor = CourseInstructor;
        this.CreditHours = CreditHours;
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

    @Exclude
    public int getDarkerColor() {
        int a = Color.alpha(CourseColorCode);
        int r = Math.round(Color.red(CourseColorCode) * 0.8f);
        int g = Math.round(Color.green(CourseColorCode) * 0.8f);
        int b = Math.round(Color.blue(CourseColorCode) * 0.8f);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    public void setCourseColorCode(int courseColorCode) {
        CourseColorCode = courseColorCode;
    }

    public String getInstructor() {
        return Instructor;
    }

    public void setInstructor(String instructor) {
        Instructor = instructor;
    }

    public int getCreditHours() {
        return CreditHours;
    }

    public void setCreditHours(int creditHours) {
        CreditHours = creditHours;
    }

    public List<ClassDataModel> getClasses() {
        return classes;
    }

    public List<QuizDataModel> getQuizzes() {
        return quizzes;
    }

    public List<QuizDataModel> getIncompleteQuizzes() {
        List<QuizDataModel> temp = new ArrayList<>();
        for (QuizDataModel quiz : quizzes)
            if (!quiz.getCompleted())
                temp.add(quiz);

        return temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseDataModel that = (CourseDataModel) o;

        if (CourseColorCode != that.CourseColorCode) return false;
        if (!CourseName.equals(that.CourseName)) return false;
        if (!CourseAbbreviation.equals(that.CourseAbbreviation)) return false;
        return classes != null ? classes.equals(that.classes) : that.classes == null;

    }

    @Override
    public int hashCode() {
        int result = CourseName.hashCode();
        result = 31 * result + CourseAbbreviation.hashCode();
        result = 31 * result + CourseColorCode;
        result = 31 * result + (classes != null ? classes.hashCode() : 0);
        return result;
    }


    public List<AssignmentDataModel> getAssignments() {
        return assignments;
    }
}
