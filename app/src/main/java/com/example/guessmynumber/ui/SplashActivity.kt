package com.example.guessmynumber.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.guessmynumber.MainActivity
import com.example.guessmynumber.R
import com.example.guessmynumber.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val thread = Thread{
            run{
                Thread.sleep(3000)
            }
            runOnUiThread{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        }
        thread.start()
    }
}