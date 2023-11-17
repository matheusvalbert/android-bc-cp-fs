package com.matheusvalbert.app2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ServiceRestarterReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (
            intent.action.equals(Intent.ACTION_BOOT_COMPLETED) &&
            Util.hasPermissionToAccessContentProvider(context)
        ) {
            val service = Intent(context, CheckNewDataFromContentProvider::class.java)
            context.startForegroundService(service)
        }
    }
}