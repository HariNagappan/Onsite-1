package com.example.fastapitesting

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.navOptions
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class BackgroundWorker(context: Context,workerParams: WorkerParameters): CoroutineWorker(context,workerParams){
    var sleepHour=22
    var sleepMinuite=30
    var shouldOffWifi=true
    init{
        val prefs=context.getSharedPreferences(SHARED_PREFS_FILENAME,Context.MODE_PRIVATE)
        sleepHour=prefs.getInt(HOUR_KEY,22)
        sleepMinuite=prefs.getInt(MINUITE_KEY,30)
        shouldOffWifi=prefs.getBoolean(TOGGLE_KEY,true)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val calendar= Calendar.getInstance()
        val curHour=calendar.get(Calendar.HOUR_OF_DAY)
        val curMinuite=calendar.get(Calendar.MINUTE)

        if(curHour*60+curMinuite >=sleepHour*60 + sleepMinuite &&shouldOffWifi){
            val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if(curHour>sleepHour+8){
                wifiManager.isWifiEnabled=true
            }
            else {
                if (wifiManager.isWifiEnabled) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(
                            applicationContext,
                            "Wi-Fi is ON. Please turn it OFF.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    wifiManager.isWifiEnabled = false
                    Log.d("general", "wifi is enabled bad boy")
                } else {
                    Log.d("general", "wifi is disabled good boy")
                }
            }
        }
        Log.d("general","running work")
        val nextRequest = OneTimeWorkRequestBuilder<BackgroundWorker>()
            .setInitialDelay(2, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context = applicationContext).enqueue(nextRequest)
        return Result.success()
    }

}
