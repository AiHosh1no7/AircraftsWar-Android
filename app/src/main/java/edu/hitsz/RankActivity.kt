package edu.hitsz

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import edu.hitsz.dao.Player
import edu.hitsz.dao.PlayerDAO
import java.io.*
import java.util.*


class RankActivity : AppCompatActivity() {
    private val playerList: MutableList<Player> = LinkedList()
    private val diff: Int = MainActivity.diff

    private fun readPlayerList() {
        playerList.clear()
        try {
            val fis = when(diff) {
                0 -> openFileInput("rankEasy")
                1 -> openFileInput("rankMedium")
                2 -> openFileInput("rankHard")
                else -> null
            }
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

    private fun addPlayer(playerName: String?, score: Int) {
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

    private fun deletePlayer(playerIndex: Int) {
        playerList.removeAt(playerIndex)
        savePlayerList()
    }

    private fun savePlayerList() {
        try {
            val fos = when(diff) {
                0 -> openFileOutput("rankEasy", MODE_PRIVATE)
                1 -> openFileOutput("rankMedium", MODE_PRIVATE)
                2 -> openFileOutput("rankHard", MODE_PRIVATE)
                else -> null
            }
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
        title = when(diff) {
            0 -> "Easy难度排行榜"
            1 -> "Medium难度排行榜"
            2 -> "Hard难度排行榜"
            else -> "这是什么难度"
        }
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

            val button = findViewById<Button>(R.id.backToMenuButton)
            val backToMenu = Intent(this@RankActivity, LauncherActivity::class.java)
            button.setOnClickListener {
                startActivity(backToMenu)
            }

            newRow.setOnClickListener {
                val builder = AlertDialog.Builder(this@RankActivity)
                builder.setTitle("删除记录")
                builder.setMessage("确认删除该记录？")
                builder.setPositiveButton("确认") { dialog, which ->
                    val tableRow = it as TableRow
                    scoreTable.removeView(tableRow)
                    deletePlayer(playerList.indexOf(player))
                }
                builder.setNegativeButton("取消") { dialog, which ->
                    return@setNegativeButton
                }
                builder.show()
            }

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

