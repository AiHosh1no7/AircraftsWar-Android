package edu.hitsz

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import edu.hitsz.dao.Player
import edu.hitsz.databinding.ActivityRankBinding
import edu.hitsz.databinding.RegisterDialogBinding
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
        val prevIntent: Intent = intent
        val scoreThisTime = prevIntent.getIntExtra("score",-1)
        val playerID = prevIntent.getStringExtra("id")
        readPlayerList()

        if(scoreThisTime != -1) {
            addPlayer(playerID, scoreThisTime)
        }

        val scoreTable = findViewById<TableLayout>(R.id.rankTable)

        for(player in playerList) {
            val newRow = TableRow(this@RankActivity)

            val playerIndex = TextView(this@RankActivity)
            playerIndex.text = (playerList.indexOf(player) + 1).toString()
            newRow.addView(playerIndex)

            val playerID = TextView(this@RankActivity)
            playerID.text = player.playerName
            newRow.addView(playerID)

            val playerScore = TextView(this@RankActivity)
            playerScore.text = player.score.toString()
            newRow.addView(playerScore)

            val playerDate = TextView(this@RankActivity)
            playerDate.text = player.dateTime
            newRow.addView(playerDate)

            scoreTable.addView(newRow,TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT
            ))
        }

        savePlayerList()
    }

    override fun onDestroy() {
        super.onDestroy()
        savePlayerList()
    }
}

