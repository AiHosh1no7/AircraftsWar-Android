package edu.hitsz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import edu.hitsz.databinding.ActivityInputBinding
import org.w3c.dom.Text

class InputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        val prevIntent = intent
        val currentScore = prevIntent.getIntExtra("currentScore", 0)
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val idInput = findViewById<EditText>(R.id.editText1)
        val scoreView = findViewById<TextView>(R.id.scoreView)

        scoreView.text = "当前分数为: $currentScore"
        confirmButton.setOnClickListener (object: View.OnClickListener {
            override fun onClick(v: View?) {
                if(TextUtils.isEmpty(idInput.text)) {
                    Toast.makeText(this@InputActivity, "ID不能为空！", Toast.LENGTH_SHORT).show()
                    return
                }
                println("CONFIRM")
                val showRank = Intent(this@InputActivity, RankActivity::class.java)
                showRank.putExtra("score", currentScore)
                showRank.putExtra("id", idInput.text.toString())
                println(idInput.text)
                startActivity(showRank)
            }
        })


        cancelButton.setOnClickListener {
            println("CANCEL")
            val showRank = Intent(this@InputActivity, RankActivity::class.java)
            showRank.putExtra("score", -1)
            showRank.putExtra("id", idInput.text.toString())
            println(idInput.text)
            startActivity(showRank)
        }
    }
}