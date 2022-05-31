package com.example.popit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val newGameButton = findViewById<Button>(R.id.new_game_button)
        val exitButton = findViewById<Button>(R.id.exit_button)

        newGameButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
        exitButton.setOnClickListener {
            onBackPressed()
        }
    }

}