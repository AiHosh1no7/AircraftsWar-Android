package edu.hitsz.socket

import java.io.Serializable

class PlayerStatus(playerID: String, playerScore: Int, playerReady: Boolean): Serializable {
    public var playerID: String = playerID
    public var playerScore: Int = playerScore
    public var playerReady: Boolean = playerReady
}