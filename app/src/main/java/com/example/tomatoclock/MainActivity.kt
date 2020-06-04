package com.example.tomatoclock

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
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
    val id1 = "What for is this id?"
    val id2 = "Probably, I understood this"  // Then... But what for name?

    fun notifications(isWork: Boolean) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val notificationChannel1: NotificationChannel = NotificationChannel(id1, "channel1", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder1 = Notification.Builder(this, id1)
        val notificationChannel2: NotificationChannel = NotificationChannel(id2, "channel2", NotificationManager.IMPORTANCE_DEFAULT)
        val builder2 = Notification.Builder(this, id1)
        notificationManager.createNotificationChannel(notificationChannel1)
        builder1.setContentIntent(contentIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("TomatoClock")
            .setContentText("It's time to work!")
        notificationManager.createNotificationChannel(notificationChannel2)
        builder2.setContentIntent(contentIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("TomatoClock")
            .setContentText("It's time to rest!")
        if (isWork)
            notificationManager.notify(100, builder1.build())
        else
            notificationManager.notify(100, builder2.build())
    }

    fun renewTime() {
        time_View.text = (time_counter / 60).toString() + ":" + (time_counter % 60).toString()
        if (isWork) {
            if (time_counter / 60 >= workTime) {
                isWork = false
                soundPool.play(1, 1F, 1F, 0, 0, 1F)
                notifications(isWork)
                state_View.text = "Rest"
                time_counter = 0
            }
        } else {
            if (time_counter / 60 >= restTime) {
                isWork = true
                soundPool.play(1, 1F, 1F, 0, 0, 1F)
                notifications(isWork)
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
