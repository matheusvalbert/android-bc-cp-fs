package com.matheusvalbert.app2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.net.toUri

class CheckNewDataFromContentProvider : Service() {

    companion object {
        private const val CHANNEL_ID = "Foreground Service ID"
        private val URI = "content://com.matheusvalbert.app1.mycp/test_table/".toUri()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.e(this::class.java.name, "started service")

        startCheckChangesLoop()
        sendNotificationToNotifyForegroundTaskStarted()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startCheckChangesLoop() {
        Thread(Runnable {
            var oldData = ""

            while (true) {
                val cr = contentResolver.query(URI, null, null, null, "_id")
                var data = ""

                while (cr?.moveToNext() == true) {
                    data += "${cr.getString(0)} - ${cr.getString(1)} | ${cr.getString(2)}\n"
                }

                cr?.close()

                if (data != oldData) {
                    Log.e(this::class.java.name, data)
                    oldData = data
                }

                Thread.sleep(3000)
            }
        }).start()
    }

    private fun sendNotificationToNotifyForegroundTaskStarted() {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_LOW)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentText("service is running")
            .setContentTitle("service enabled")
            .setSmallIcon(R.drawable.ic_launcher_background)

        startForeground(1001, notification.build())
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}