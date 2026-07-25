package com.example.notifmirror

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

/**
 * Keeps LocalServer alive in the background as a foreground service,
 * so Android doesn't kill the WebSocket server when the app isn't in view.
 */
class ServerService : Service() {

    private var server: LocalServer? = null
    private val CHANNEL_ID = "notif_mirror_service"

    override fun onCreate() {
        super.onCreate()
        createChannel()
        startForeground(1, buildNotification())
        server = LocalServer(LocalServer.PORT)
        server?.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        server?.stop()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Notif Mirror Server",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notif Mirror running")
            .setContentText("Broadcasting notifications on port ${LocalServer.PORT}")
            .setSmallIcon(android.R.drawable.stat_sys_upload)
            .setOngoing(true)
            .build()
    }
}
