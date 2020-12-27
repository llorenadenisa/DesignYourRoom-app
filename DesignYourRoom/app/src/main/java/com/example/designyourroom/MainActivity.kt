package com.example.designyourroom

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tap_anywhere.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse ))

        main_layout.setOnClickListener{ startApp() }
    }

    private fun startApp() {
        val intent = Intent(this, PhotoMethodSelector::class.java)
        startActivity(intent)
    }
}