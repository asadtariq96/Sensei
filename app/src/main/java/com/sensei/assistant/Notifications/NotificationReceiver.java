package com.sensei.assistant.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Asad on 9/20/2017.
 */

public class NotificationReceiver {

    public static class ClassNotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        Timber.d("onReceive");
//        if (getCourseDataInstance().getDailyNotificationString() != null) {
//
//            PugNotification.with(context)
//                    .load()
//                    .title("Due this week")
//                    .message(getCourseDataInstance().getDailyNotificationString())
//                    .smallIcon(R.mipmap.ic_launcher)
//                    .largeIcon(R.mipmap.ic_launcher)
//                    .flags(Notification.DEFAULT_ALL)
//                    .click(DashboardActivity.class)
//                    .simple()
//                    .build();
//
//
//
//        }
////        Toast.makeText(context, "I'm running, quizzes:" + size, Toast.LENGTH_SHORT).show();
//
//    }
}
