package com.sensei.assistant.DataModelClasses;

/**
 * Created by Asad on 9/24/2017.
 */

public interface TaskItem {
    int TYPE_QUIZ =0;
    int TYPE_ASSIGNMENT = 1;
    int TYPE_HOMEWORK = 2;
    int getListItemType();

}
