package com.android.chatverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(Runnable {
            if (SharedPreferencesUtil(applicationContext).getFlagValue()){
                startActivity(Intent(this@SplashScreen,DashboardScreen::class.java))
            }
            else{
                val next = Intent(this@SplashScreen,MainActivity::class.java)
                startActivity(next)
            }
            finish()
        },3000)
        
//        var x = SharedPreferencesUtil(applicationContext)



    }
}