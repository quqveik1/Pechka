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

    private fun sitInGrusha(totalTime: Long, step: Long) {
        for (time in 0 .. totalTime step step) {
            if(!needsToRun) {
                break
            }
            Thread.sleep(step)
        }
    }

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            sitInGrusha(10000, 1000)
            Log.d(
                DebugTag,
                "end"
            )

            serviceData.postValue(ServiceData(ServiceState.Stopped))
            stopSelf(msg.arg1)
        }
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        val extraData = intent?.extras?.getString(StopIntentTag)
        if(extraData != null) {
            needsToRun = false
        } else {
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
            HandlerThread("ServiceStartArguments").apply {
                start()

                serviceLooper = looper
                serviceHandler = ServiceHandler(looper)
            }
            serviceData.postValue(ServiceData(ServiceState.Active))
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
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

        val intentStop = Intent(
            this,
            StopServiceReceiver::class.java
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
                androidx.core.R.drawable.ic_call_answer,  // Укажите иконку для кнопки
                "STOOOOP",  // Текст на кнопке
                pendingIntentStop
            ).build()

        return notification
    }
}