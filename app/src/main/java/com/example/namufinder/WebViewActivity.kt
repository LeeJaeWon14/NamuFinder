package com.example.namufinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_web_view.*
import java.util.*

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val myBundle : Bundle? = intent.getBundleExtra("myBundle")

        //WebView 초기화
        webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            settings.builtInZoomControls = true
            settings.setSupportZoom(true)
        }
        webView.loadUrl("https://namu.wiki/w/${myBundle?.getString("keyword")}") //WebView Url 지정

        webViewButton.setOnClickListener { finish() }
        webViewButtonAdd.setOnClickListener {
            Toast.makeText(this@WebViewActivity, "add Bookmark", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        if(webView.canGoBack()) {
            webView.goBack()
        }
        else {
            finish()
        }
    }
}