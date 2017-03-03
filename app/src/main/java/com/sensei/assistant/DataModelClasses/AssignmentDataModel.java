package com.sensei.assistant.DataModelClasses;

import com.google.firebase.database.Exclude;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Created by Asad on 19-Jan-17.
 */

public class AssignmentDataModel {
    private String dueDate;
    private String dueTime;
    private String reminderTime;
    private String assignmentTitle;
    private String assignmentDescription;
    private Boolean isCompleted;

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public String getAssignmentDescription() {
        return assignmentDescription;
    }

    public void setAssignmentDescription(String assignmentDescription) {
        this.assignmentDescription = assignmentDescription;
    }

    @Exclude
    public LocalTime getDueTimeOriginal() {
        return (dueTime == null) ? null : new LocalTime().parse(dueTime);
    }

    @Exclude
    public LocalDate getDueDateOriginal() {
        return (dueDate == null) ? null : new LocalDate().parse(dueDate);
    }

    @Exclude
    public DateTime getReminderTimeOriginal() {
        return (reminderTime == null) ? null : new DateTime().parse(dueDate);

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssignmentDataModel that = (AssignmentDataModel) o;

        if (dueDate != null ? !dueDate.equals(that.dueDate) : that.dueDate != null) return false;
        if (dueTime != null ? !dueTime.equals(that.dueTime) : that.dueTime != null) return false;
        if (reminderTime != null ? !reminderTime.equals(that.reminderTime) : that.reminderTime != null)
            return false;
        if (!assignmentTitle.equals(that.assignmentTitle)) return false;
        return assignmentDescription != null ? assignmentDescription.equals(that.assignmentDescription) : that.assignmentDescription == null;

    }

    @Override
    public int hashCode() {
        int result = dueDate != null ? dueDate.hashCode() : 0;
        result = 31 * result + (dueTime != null ? dueTime.hashCode() : 0);
        result = 31 * result + (reminderTime != null ? reminderTime.hashCode() : 0);
        result = 31 * result + assignmentTitle.hashCode();
        result = 31 * result + (assignmentDescription != null ? assignmentDescription.hashCode() : 0);
        return result;
    }
}
