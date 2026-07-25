package com.example.notifmirror

import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.format.Formatter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ipText = findViewById<TextView>(R.id.ipText)
        val statusText = findViewById<TextView>(R.id.statusText)
        val permButton = findViewById<Button>(R.id.permButton)
        val startButton = findViewById<Button>(R.id.startButton)

        // Show the IP the laptop needs to connect to (works for Wi-Fi or hotspot)
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
        ipText.text = "Connect the laptop to:\nws://$ip:${LocalServer.PORT}"

        // Notification access has to be granted manually in system settings
        permButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        // Ask for POST_NOTIFICATIONS on Android 13+ (needed for our own foreground service notice)
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100
            )
        }

        startButton.setOnClickListener {
            startForegroundService(Intent(this, ServerService::class.java))
            statusText.text = "Server running on port ${LocalServer.PORT}"
        }
    }
}
