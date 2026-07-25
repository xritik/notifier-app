package com.example.notifmirror

import android.util.Log
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.util.Collections

/**
 * A tiny WebSocket server that lives inside the app.
 * The laptop connects to ws://<phone-ip>:8887 and receives
 * a JSON message every time a notification arrives.
 */
class LocalServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {

    // Keep track of every connected laptop/browser
    private val clients = Collections.synchronizedSet(mutableSetOf<WebSocket>())

    companion object {
        // Singleton so NotifListener can reach the running server instance
        var instance: LocalServer? = null
        const val PORT = 8887
    }

    init {
        instance = this
    }

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        clients.add(conn)
        Log.i("LocalServer", "Client connected: ${conn.remoteSocketAddress}")
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String?, remote: Boolean) {
        clients.remove(conn)
        Log.i("LocalServer", "Client disconnected")
    }

    override fun onMessage(conn: WebSocket, message: String) {
        // We don't expect the laptop to send anything back, but log it just in case
        Log.i("LocalServer", "Message from client: $message")
    }

    override fun onError(conn: WebSocket?, ex: Exception) {
        Log.e("LocalServer", "Server error", ex)
    }

    override fun onStart() {
        Log.i("LocalServer", "WebSocket server started on port $PORT")
    }

    /** Called by NotifListener whenever a new notification is posted */
    fun broadcastNotification(json: String) {
        synchronized(clients) {
            for (client in clients) {
                if (client.isOpen) client.send(json)
            }
        }
    }
}
