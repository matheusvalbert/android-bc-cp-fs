package com.matheusvalbert.app2

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

object Util {

    private const val PERMISSION_NAME = "com.matheusvalbert.app1.mycp.RW"

    fun startServiceIfNeeded(ctx: Context) {
        if (hasPermissionToAccessContentProvider(ctx) && !isForegroundServiceRunning(ctx)) {
            val serviceIntent = Intent(ctx, CheckNewDataFromContentProvider::class.java)
            ctx.startForegroundService(serviceIntent)
        }
    }

    fun hasPermissionToAccessContentProvider(ctx: Context): Boolean {
        return ctx.checkSelfPermission(PERMISSION_NAME) == PackageManager.PERMISSION_GRANTED
    }

    private fun isForegroundServiceRunning(ctx: Context): Boolean {
        val manager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (CheckNewDataFromContentProvider::class.java.name.equals(service.service.className)) {
                return true
            }
        }
        return false
    }
}