package com.kurlic.pechka.back.services.heatservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.kurlic.pechka.R
import com.kurlic.pechka.back.services.client.AppServiceReceiver
import com.kurlic.pechka.common.services.launchForegroundService
import com.kurlic.pechka.common.services.startForegroundService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

const val DebugTag = "HeatForegroundService"

class HeatForegroundService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private var serviceData = BehaviorSubject.createDefault(ServiceData(ServiceState.Launching))
    private var serviceDataMutableFromActivity = ServiceDataMutableFromActivity()
    private val disposables = CompositeDisposable()

    private var needsToRun = true

    private var foregroundNotificationId = 1

    override fun onCreate() {
        super.onCreate()
        createObserver()
    }

    private fun createObserver() {
        disposables.add(serviceData.subscribe { data ->
            saveServiceData(data)
        })
    }

    private fun saveServiceData(data: ServiceData) {
        saveData(applicationContext, ForegroundServiceFolderDataTag, data)

        sendBroadcastToApp()
    }

    private fun sendBroadcastToApp() {
        val intent = Intent(AppServiceReceiver)
        sendBroadcast(intent)
    }

    private fun sitInGrusha(totalTime: Long, stepT: Long) {
        for (time in 0..totalTime step stepT) {
            if (!needsToRun) {
                break
            }
            Thread.sleep(stepT)
        }
    }

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            sitInGrusha(10000, 1000)
            Log.d(DebugTag, "end")
            doStopJob()
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    fun doStopJob() {
        needsToRun = true
        serviceData.onNext(ServiceData(ServiceState.Stopped))
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf(foregroundNotificationId)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val extraData = intent?.extras
        if (extraData == null) {
            Log.e(DebugTag, "To HeatForegroundService provided null data")
            throw KotlinNullPointerException("To HeatForegroundService provided null data")
        }

        val type = extraData.getString(PurposeMessageTag)

        if (type == HeatServiceMessageType.StopServiceTag.name) {
            needsToRun = false
        } else if (type == HeatServiceMessageType.StartServiceTag.name) {
            serviceStartWork(startId, extraData)
        }
        return START_NOT_STICKY
    }

    private fun serviceStartWork(startId: Int, data: Bundle) {
        HandlerThread("ServiceStartArguments").apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }

        serviceDataMutableFromActivity = Gson().fromJson(data.getString(IntentDataMessageTag), ServiceDataMutableFromActivity::class.java)
        Log.d(DebugTag, serviceDataMutableFromActivity.toString())

        foregroundNotificationId = startId

        createNotificationChannel()
        val notification = getNotification()
        startForeground(foregroundNotificationId, notification)
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = foregroundNotificationId
            serviceHandler?.sendMessage(msg)
        }

        serviceData.onNext(ServiceData(ServiceState.Active))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private val HeatServiceChannelID = "heatServiceChannelID"

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
            applicationContext, HeatServiceMessageType.StopServiceTag.name
        )
        val pendingIntentStop = PendingIntent.getBroadcast(
            this, 0, intentStop, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, HeatServiceChannelID)
            .setContentTitle(getString(R.string.heat_service_notification_title))
            .setContentText(getString(R.string.heat_service_notification_text))
            .setSmallIcon(R.drawable.ic_launcher_background).setOngoing(true).setAutoCancel(false)
            .addAction(0, getString(R.string.stop), pendingIntentStop).build()
    }

    companion object {
        fun startService(
            contextT: Context,
            serviceDataMutableFromActivity: ServiceDataMutableFromActivity
        ) {
            val intent = Intent(contextT, HeatForegroundService::class.java)
            intent.putExtra(PurposeMessageTag, HeatServiceMessageType.StartServiceTag.name)
            intent.putExtra(IntentDataMessageTag, Gson().toJson(serviceDataMutableFromActivity))
            launchForegroundService(contextT, intent)
        }
    }
}