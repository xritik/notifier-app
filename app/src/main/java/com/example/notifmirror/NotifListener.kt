package com.example.notifmirror

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import org.json.JSONObject

/**
 * Android calls onNotificationPosted() every time ANY notification
 * appears on the phone (messages, calls, apps, etc). We turn it into
 * JSON and push it out over the WebSocket server.
 */
class NotifListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras
        val title = extras.getCharSequence("android.title")?.toString() ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""
        val appName = try {
            packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(sbn.packageName, 0)
            ).toString()
        } catch (e: Exception) {
            sbn.packageName
        }

        // Skip our own "server running" notification
        if (sbn.packageName == packageName) return

        val json = JSONObject().apply {
            put("app", appName)
            put("title", title)
            put("text", text)
            put("time", sbn.postTime)
        }

        LocalServer.instance?.broadcastNotification(json.toString())
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Optional: notify the laptop that a notification was dismissed
    }
}
