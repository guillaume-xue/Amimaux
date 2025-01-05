package ufr.m1.prog_mobile.projet.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.concurrent.TimeUnit
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.Calendar

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val title = inputData.getString("animal") ?: "Title"
        val message = inputData.getString("activite") ?: "Message"
        showNotification(title, message)
        return Result.success()
    }


    fun scheduleOneTimeNotification(context: Context, delayInMillis: Long) {
        val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .setInitialDelay(delayInMillis, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }


    fun scheduleDailyNotification(context: Context, delayInMillis: Long) {
        val workRequest = PeriodicWorkRequestBuilder<MyWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delayInMillis, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    fun scheduleWeeklyNotification(context: Context, delayInMillis: Long) {
        val workRequest = PeriodicWorkRequestBuilder<MyWorker>(7, TimeUnit.DAYS)
            .setInitialDelay(delayInMillis, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    fun showNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "notification_channel"

        val channel = NotificationChannel(channelId, "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationId = (System.currentTimeMillis() % Integer.MAX_VALUE).toInt()
        notificationManager.notify(notificationId, notification)
    }
}

