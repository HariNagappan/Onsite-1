package com.example.fastapitesting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForegroundService: Service() {
    private val CHANNEL_ID = "wifi_check_channel"
    private var sleepHour = 22
    private var sleepMinute = 30
    private var shouldOffWifi = true
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        val prefs = getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE)
        sleepHour = prefs.getInt(HOUR_KEY, 22)
        sleepMinute = prefs.getInt(MINUITE_KEY, 30)
        shouldOffWifi = prefs.getBoolean(TOGGLE_KEY, true)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Wi-Fi Check Running")
            .setContentText("Monitoring time to notify you...")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()

        startForeground(1, notification)

        job = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                CheckWifiStatus()
                delay(2000)
            }
        }

        return START_STICKY
    }

    private fun CheckWifiStatus() {
        val calendar = java.util.Calendar.getInstance()
        val curHour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        val curMinute = calendar.get(java.util.Calendar.MINUTE)

        Log.d("WifiService", "Checking time... $curHour:$curMinute")

        if ((curHour * 60 + curMinute) >= (sleepHour * 60 + sleepMinute) && shouldOffWifi) {
            val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifiManager.isWifiEnabled) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(applicationContext, "Wi-Fi is ON. Please turn it OFF.", Toast.LENGTH_SHORT).show()
                }
                showReminderNotification()
                Log.d("WifiService", "Wi-Fi is ON. Please turn it OFF.")
            } else {
                Log.d("WifiService", "Wi-Fi is already OFF. Good!")
            }
        }
    }

    private fun showReminderNotification() {
        val notify = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Turn off Wi-Fi")
            .setContentText("It’s your scheduled time to disable Wi-Fi for focus/sleep.")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1002, notify)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Wi-Fi Check Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }
    override fun onBind(intent: Intent?): IBinder?=null

}