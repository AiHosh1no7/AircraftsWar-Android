package edu.hitsz.socket

import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket

class Matching: Runnable {
    override fun run() {
        try {
            val socket = Socket()
            socket.connect(InetSocketAddress("10.250.152.127",9999), 5000)

        } catch(e: Exception) {
            e.printStackTrace()
        }
    }
}