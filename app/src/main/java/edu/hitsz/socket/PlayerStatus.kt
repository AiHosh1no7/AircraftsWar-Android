package edu.hitsz.socket

import java.io.Serializable

class PlayerStatus: Serializable {
    public var playerID: String = ""
    public var playerScore: Int = 0
    public var playerReady: Boolean = true
}