package com.example.designyourroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startBtn = findViewById<Button>(R.id.btnStart)
        startBtn.setOnClickListener{ startApp() }
    }

    private fun startApp() {
        val intent = Intent(this, PhotoMethodSelector::class.java)
        startActivity(intent)
    }
}