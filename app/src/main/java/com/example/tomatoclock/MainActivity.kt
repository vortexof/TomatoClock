package com.example.tomatoclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var time_counter: Int = 0
    fun renewTime() {
        time_View.text = (time_counter / 60).toString() + ":" + (time_counter % 60).toString()
    }


    val timer = Timer("schedule", false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                time_counter++
                this@MainActivity.runOnUiThread(Runnable { renewTime() })
            }
        }, 1000, 1000)
    }




}
