package com.android.example.recipeapp.interactors.app

import android.util.Log
import com.android.example.recipeapp.util.MY_TAG
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.net.SocketFactory


object DoesNetworkHaveInternet {
    fun execute(socketFactory: SocketFactory): Boolean {
        return try {
            Log.d(MY_TAG, "execute: Pinging google")
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            Log.d(MY_TAG, "execute: Pinging Successful")
            true
        } catch (e: Exception) {
            Log.d(MY_TAG, "execute: No Internet Connection: ${e.message}")
            false
        }
    }
}