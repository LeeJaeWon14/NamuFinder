package com.example.namufinder

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkVersion()
        //Thread.sleep(500)

        CoroutineScope(Dispatchers.Default).launch {
            delay(1000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
    }

    private fun checkVersion() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(this@SplashActivity, "Version is over Marshmallow : ${Build.VERSION.SDK_INT}", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this@SplashActivity, "Version is under Marshmallow : ${Build.VERSION.SDK_INT}", Toast.LENGTH_SHORT).show()
        }
    }
}