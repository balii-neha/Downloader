package com.kotlin.nehabaliassignmenttest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kotlin.nehabaliassignmenttest.view.HomeActivity
import com.kotlin.nehabaliassignmenttest.view.LoginActivity
import kotlin.concurrent.thread

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callingSplash()
    }

    private fun callingSplash() {
        thread {
            Thread.sleep(1000)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
    }
}