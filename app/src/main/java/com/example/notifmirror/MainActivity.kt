package com.example.notifmirror

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.widget.Button
import android.widget.TextView
import java.net.NetworkInterface
import java.util.Collections

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ipText = findViewById<TextView>(R.id.ipText)
        val statusText = findViewById<TextView>(R.id.statusText)
        val permButton = findViewById<Button>(R.id.permButton)
        val startButton = findViewById<Button>(R.id.startButton)

        val ip = getLocalIpAddress()
        ipText.text = if (ip != null) {
            "Connect the laptop to:\nws://$ip:${LocalServer.PORT}"
        } else {
            "Could not detect IP.\nMake sure Wi-Fi or Hotspot is ON, then reopen the app."
        }

        permButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

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

    private fun getLocalIpAddress(): String? {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress && addr.hostAddress?.contains(":") == false) {
                        return addr.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}