package com.kurlic.pechka.back.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.kurlic.pechka.MainActivity
import com.kurlic.pechka.R

const val ForegroundServiceBroadCast = "com.kurlic.pechka.vbuz"

class HeatForegroundService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    override fun onCreate() {
        super.onCreate()
        HandlerThread("ServiceStartArguments").apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
        val intent = Intent(ForegroundServiceBroadCast)
        sendBroadcast(intent)
    }

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {

            try {
                Log.d(
                    "vbuz",
                    "service started"
                )
                Thread.sleep(60000)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                Log.e(
                    "vbuz",
                    "thread interrupt"
                )
            }
            Log.d(
                "vbuz",
                "end"
            )

            stopSelf(msg.arg1)
        }
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        createNotificationChannel()
        val notification = getNotification()
        startForeground(
            1,
            notification
        )
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        return START_NOT_STICKY
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    inner class LocalBinder : Binder() {
        fun getService(): HeatForegroundService = this@HeatForegroundService
    }

    private val HeatServiceChannelID = "vbu"

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                HeatServiceChannelID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun getNotification(): Notification {
        val notificationIntent = Intent(
            this,
            MainActivity::class.java
        )
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            this,
            HeatServiceChannelID
        ).setContentTitle("Heat ывывыв").setContentText("The service is running...")
            .setSmallIcon(R.drawable.ic_launcher_background).setContentIntent(pendingIntent)
            .setOngoing(true).setAutoCancel(false).build()

        return notification
    }
}