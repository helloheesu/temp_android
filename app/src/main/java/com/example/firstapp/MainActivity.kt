package com.example.firstapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.firstapp.ui.theme.FirstAppTheme
import android.Manifest
import android.app.KeyguardManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.WindowManager

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showWhenLockedAndTurnScreenOn()
        createNotiChannel()

        startService()





        setContent {


            FirstAppTheme {

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    GreetingText(message = "Hello")


                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        println("resumeddd")
        setShowWhenLocked(true)
        setTurnScreenOn(true)
    }

    private fun showWhenLockedAndTurnScreenOn() {
        setShowWhenLocked(true)
        setTurnScreenOn(true)

//        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
//        keyguardManager.requestDismissKeyguard(this, null)
    }

    private fun createNotiChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.POST_NOTIFICATIONS
            ), 200)
        }

        println("starting running service")
        val channel = NotificationChannel(CHANNEL_ID, "blah blah", NotificationManager.IMPORTANCE_HIGH).apply {
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    private fun startService() {
        println("trying intent creation")
        val intent = Intent(this, RunningService::class.java)
        println("broadcasting intent")
        startService(intent)
        println("intent dealt")

    }
}

@Composable
fun GreetingText(message: String, modifier: Modifier = Modifier) {
    Text(text = message)
}

@Preview(showBackground = true)
@Composable
fun GreetingTextPreview() {
    FirstAppTheme {
        GreetingText(message = "Happy Birthday Sam!")
    }
}


