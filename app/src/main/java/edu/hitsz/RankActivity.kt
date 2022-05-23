package edu.hitsz

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.hitsz.dao.Player
import edu.hitsz.strategy.Context
import java.io.*
import java.util.*

class RankActivity : AppCompatActivity() {
    private val playerList: MutableList<Player> = LinkedList()

    fun readPlayerList() {
        playerList.clear()
        try {
            val fis = openFileInput("rank")
            val ois = ObjectInputStream(fis)
            while (true) {
                val player = ois.readObject() as Player
                playerList.add(player)
            }
        } catch (e: FileNotFoundException) {
            savePlayerList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addPlayer(playerName: String?, score: Int) {
        val newPlayer = Player(playerName, score)
        for (player in playerList) {
            if (player.score >= score) {
                continue
            } else {
                playerList.add(playerList.indexOf(player), newPlayer)
                return
            }
        }
        playerList.add(newPlayer)
        savePlayerList()
    }

    fun deletePlayer(playerIndex: Int) {
        playerList.removeAt(playerIndex)
        savePlayerList()
    }

    fun savePlayerList() {
        try {
            val fos = openFileOutput("rank", MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            for (player in playerList) {
                oos.writeObject(player)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun printPlayerList() {
        println("***************")
        println("  得分排行榜")
        println("***************")
        for (player in playerList) {
            print("第" + (playerList.indexOf(player) + 1) + "名 ")
            print(player.playerName + "," + player.score + " ")
            println(player.dateTime)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)
        val prevIntent: Intent = getIntent()
        readPlayerList()
    }
}