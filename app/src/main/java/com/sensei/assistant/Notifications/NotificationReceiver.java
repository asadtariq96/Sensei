package com.sensei.assistant.Notifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sensei.assistant.Activities.Dashboard.DashboardActivity;
import com.sensei.assistant.R;

import br.com.goncalves.pugnotification.notification.PugNotification;
import timber.log.Timber;

import static com.sensei.assistant.DataHandlers.CourseDataHandler.getCourseDataInstance;

/**
 * Created by Asad on 9/20/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Timber.d("onReceive");
        if (getCourseDataInstance().getDailyNotificationString() != null) {

            PugNotification.with(context)
                    .load()
                    .title("Due this week")
                    .message(getCourseDataInstance().getDailyNotificationString())
                    .smallIcon(R.mipmap.ic_launcher)
                    .largeIcon(R.mipmap.ic_launcher)
                    .flags(Notification.DEFAULT_ALL)
                    .click(DashboardActivity.class)
                    .simple()
                    .build();
        }
//        Toast.makeText(context, "I'm running, quizzes:" + size, Toast.LENGTH_SHORT).show();

    }
}
