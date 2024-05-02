package com.kurlic.pechka.back.services.heatservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.kurlic.pechka.R
import com.kurlic.pechka.common.debug.services.startForegroundService

const val ForegroundServiceBroadCast = "com.kurlic.pechka.HeatForegroundService"
const val ForegroundServiceFolder = "HeatForegroundService"
const val ForegroundServiceFolderDataTag = "HeatForegroundService"
const val DebugTag = "HeatForegroundService"
const val StopIntentTag = "StopHeatForegroundService"

class HeatForegroundService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private var serviceData = MutableLiveData(ServiceData(ServiceState.Launching))

    private var needsToRun = true

    private var foregroundNotificationId = 1

    override fun onCreate() {
        super.onCreate()
        createObserver()
    }

    private fun createObserver() {
        serviceData.observeForever { data ->
            saveData(data)
        }
    }

    private fun saveData(data: ServiceData) {
        val prefs = getSharedPreferences(
            ForegroundServiceFolder,
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(data)
        editor.putString(
            ForegroundServiceFolderDataTag,
            json
        )
        editor.apply()

        sendBroadcastToApp()
    }

    private fun sendBroadcastToApp() {
        val intent = Intent(ForegroundServiceBroadCast)
        sendBroadcast(intent)
    }

    private fun sitInGrusha(
        totalTime: Long,
        stepT: Long
    ) {
        for (time in 0..totalTime step stepT) {
            if (!needsToRun) {
                break
            }
            Thread.sleep(stepT)
        }
    }

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            sitInGrusha(
                10000,
                1000
            )
            Log.d(
                DebugTag,
                "end"
            )
            doStopJob()
        }
    }

    fun doStopJob() {
        needsToRun = true
        serviceData.postValue(ServiceData(ServiceState.Stopped))
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf(foregroundNotificationId)
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        val extraData = intent?.extras?.getString(StopIntentTag)
        if (extraData != null) {
            needsToRun = false
        } else {

            HandlerThread("ServiceStartArguments").apply {
                start()

                serviceLooper = looper
                serviceHandler = ServiceHandler(looper)
            }

            foregroundNotificationId = startId

            createNotificationChannel()
            val notification = getNotification()
            startForeground(
                foregroundNotificationId,
                notification
            )
            serviceHandler?.obtainMessage()?.also { msg ->
                msg.arg1 = foregroundNotificationId
                serviceHandler?.sendMessage(msg)
            }

            serviceData.postValue(ServiceData(ServiceState.Active))
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private val HeatServiceChannelID = "HeatServiceChannelID"

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                HeatServiceChannelID,
                getString(R.string.heat_service_channel_name),
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun getNotification(): Notification {
        val intentStop = HeatServiceReceiver.getBroadcastIntent(
            applicationContext,
            StopServiceTag
        )
        val pendingIntentStop = PendingIntent.getBroadcast(
            this,
            0,
            intentStop,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(
            this,
            HeatServiceChannelID
        ).setContentTitle(getString(R.string.heat_service_notification_title))
            .setContentText(getString(R.string.heat_service_notification_text))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true).setAutoCancel(false).addAction(
                0,
                getString(R.string.stop),
                pendingIntentStop
            ).build()

        return notification
    }

    companion object {
        fun startService(context: Context) {
            startForegroundService(
                context,
                HeatForegroundService::class.java
            )
        }
    }
}