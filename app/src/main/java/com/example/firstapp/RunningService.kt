package com.example.firstapp

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat

const val CHANNEL_ID = "MY_FOREGROUND_CHANNEL_ID"

class RunningService: Service() {
    private lateinit var receiver: BroadcastReceiver

    private lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("onStartCommand")
        startMediaForTest()
        startRunningService()
        receiveScreenOff()

        println("finished startRunningService")
        return START_STICKY
    }

    private fun startMediaForTest() {
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        mediaPlayer.start()
        mediaPlayer.isLooping = true

    }


    private fun receiveScreenOff() {
        val intentFilter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val activityIntent = Intent(context, MainActivity::class.java)
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(activityIntent)
                println("screen off")
                println(context)
            }
        }

        registerReceiver(receiver, intentFilter)
    }

    private fun startRunningService() {
        val notiIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notiIntent,
            PendingIntent.FLAG_IMMUTABLE);

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentTitle("Foreground Service")
            .setContentText("Foreground Servxice is running")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOngoing(true)
            .build()

        println("notification created")
        startForeground(32352, notification)
        println("foreground started")
    }

    override fun onDestroy() {

        super.onDestroy()

        unregisterReceiver(receiver)
        mediaPlayer.release()

    }
}