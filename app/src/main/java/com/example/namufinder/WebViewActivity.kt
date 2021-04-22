package com.example.namufinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.namufinder.room.BookmarkDAO
import com.example.namufinder.room.MyRoomDatabase
import com.example.namufinder.room.BookmarkEntity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val myBundle : Bundle? = intent.getBundleExtra("myBundle")

        //initWebView(myBundle!!)
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
            //bookmark entity 생성
            val bookmark = BookmarkEntity(myBundle?.getString("keyword")!!, webView.url!!)

            //insert 실행
            CoroutineScope(Dispatchers.IO).launch {
                getBookmarkDAO().insertBookmark(bookmark)
            }
            Snackbar.make(it, "즐겨찾기에 추가되었습니다", Snackbar.LENGTH_SHORT)
                .setAction("취소") {
                    //저장 후 바로 삭제할 수 있도록 추가
                    CoroutineScope(Dispatchers.IO).launch {
                        val entities = getBookmarkDAO().getBookmark()
                        getBookmarkDAO().deleteBookmark(entities.get(entities.size -1))
                    }
                    with(Toast(this@WebViewActivity)) {
                        setText("삭제되었습니다")
                        setGravity(Gravity.TOP, 0, 0)
                        duration = Toast.LENGTH_SHORT
                        show()
                    }
                }.show()
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

    fun getBookmarkDAO() : BookmarkDAO {
        return MyRoomDatabase.getInstance(this@WebViewActivity)
            .getBookmarkDAO()
    }
}