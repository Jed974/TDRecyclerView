package com.example.tdrecyclerview.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.VectorDrawable
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.tdrecyclerview.MainActivity
import com.example.tdrecyclerview.R

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // TODO: Step 1.11 create intent
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    // TODO: Step 1.12 create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // add style
    /*val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_baseline_create_24
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(null)
        .bigLargeIcon(null)*/

    // add snooze action
    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAGS)

    //val myLogo = (ResourcesCompat.getDrawable(applicationContext.resources, R.drawable.ic_stat_name, null) as VectorDrawable).toBitmap()

    // get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )


        // set title, text and icon to builder
        .setSmallIcon(R.drawable.material_ic_calendar_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

        // set content intent
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        // add style to builder
        //.setStyle(bigPicStyle)
        //.setLargeIcon(eggImage)

        // add snooze action
        .addAction(
            R.drawable.material_ic_calendar_black_24dp,
            applicationContext.getString(R.string.done),
            snoozePendingIntent
        )

        // set priority
        //.setPriority(NotificationCompat.PRIORITY_HIGH)

    // call notify
    notify(NOTIFICATION_ID, builder.build())
    println("notification")
}

// TODO: Step 1.14 Cancel all notifications
/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}
