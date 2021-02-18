package com.example.namufinder

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionCheck()
        //Thread(Runnable { Thread.sleep(2000) })
        Thread.sleep(1000)
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
    }

    private fun permissionCheck() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(this@SplashActivity, "Version is over Marshmallow : ${Build.VERSION.SDK_INT}", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this@SplashActivity, "Version is under Marshmallow : ${Build.VERSION.SDK_INT}", Toast.LENGTH_SHORT).show()
        }
    }
}