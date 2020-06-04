package com.example.tomatoclock

import android.media.AudioManager
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Time
import java.util.*

class MainActivity : AppCompatActivity() {
    // comment for checking branching
    // random comment in master for br_check
    var time_counter: Int = 0
    var isRunning: Boolean = false
    var workTime: Int = 5
    var restTime: Int = 5
    var isWork: Boolean = true
    val soundPool = SoundPool(6, AudioManager.STREAM_MUSIC, 0)

    fun renewTime() {
        time_View.text = (time_counter / 60).toString() + ":" + (time_counter % 60).toString()
        if (isWork) {
            if (time_counter / 60 >= workTime) {
                isWork = false
                soundPool.play(1, 1F, 1F, 0, 0, 1F)
                state_View.text = "Rest"
                time_counter = 0
            }
        } else {
            if (time_counter / 60 >= restTime) {
                isWork = true
                soundPool.play(1, 1F, 1F, 0, 0, 1F)
                state_View.text = "Work"
                time_counter = 0
            }
        }
    }

    var timer: Timer = Timer()

    fun StartTimer():Timer {
        var timer = Timer("schedule", false)
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                time_counter++
                this@MainActivity.runOnUiThread(Runnable { renewTime() })
            }
        }, 1000, 1000)
        return timer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        soundPool.load(baseContext, R.raw.ding, 1)
        reset_btn.setOnClickListener {
            timer.cancel()
            time_counter = 0
            time_View.text = "0:00"
            start_btn.text = "Start"
            state_View.text = "Pause"
            isRunning = false
            start_btn.text = "Start"
            workTime = workNum_inp.text.toString().toInt()
            restTime = restNum_inp.text.toString().toInt()
        }

        start_btn.setOnClickListener {
            if (isRunning) {
                isRunning = false
                start_btn.text = "Start"
                state_View.text = "Pause"
                timer.cancel()
            } else {
                isRunning = true
                if (workTime != workNum_inp.text.toString().toInt()) {
                    workTime = workNum_inp.text.toString().toInt()
                    time_counter = 0
                    time_View.text = "0:00"
                }
                if (restTime != restNum_inp.text.toString().toInt()) {
                    restTime = restNum_inp.text.toString().toInt()
                    time_counter = 0
                    time_View.text = "0:00"
                }
                start_btn.text = "Pause"
                state_View.text = "Work"
                timer = StartTimer()
            }
        }
    }
}
