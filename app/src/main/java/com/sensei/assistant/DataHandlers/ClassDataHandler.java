package com.sensei.assistant.DataHandlers;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asad on 08-Jan-17.
 */

public class ClassDataHandler {
    ChildEventListener childEventListener;
    ValueEventListener valueEventListener;

    List<ClassDataHandler> ClassesList = new ArrayList<>();

    private static ClassDataHandler instance = null;

    private ClassDataHandler() {
    }

    public static ClassDataHandler getClassDataInstance() {
        if (instance == null) {
            instance = new ClassDataHandler();
        }
        return instance;
    }

    public void addChildListener(){
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
}
