package com.example.namufinder

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.namufinder.adapter.MyRecyclerAdapter
import com.example.namufinder.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class MainActivity : AppCompatActivity() {
    private val searchModelInit : SearchModel by lazy { SearchModel() }
    private var croll : Elements? = null
    private var itemCount : Int = 0
    private lateinit var myIntent : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.title = "검색"

        //Binding Class는 build.gradle에서 dataBinding을 활성화 시켜주면 자동생성
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.searchModel = searchModelInit
    }

    private var time : Long = 0
    override fun onBackPressed() {
        if(System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis()
            Toast.makeText(this@MainActivity, "한번 더 누르면 종료합니다", Toast.LENGTH_SHORT).show()
        }
        else if(System.currentTimeMillis() - time < 2000) {
            this.finishAffinity()
        }
    }

    inner class SearchModel {
        var searchKeyword : ObservableField<String> = ObservableField()

        fun click(view : View) {

            object : Thread() {
                override fun run() {
                    super.run()
                    //나무위키 검색결과 크롤링
                    croll = CrollingTask.searchTask("https://namu.wiki/Search?q=${searchModelInit.searchKeyword.get()}")

                    //크롤링으로 가져온 elements 사이즈로 RecyclerView의 item 수 지정
                    itemCount = croll!!.size

                    //UI스레드에서 RecyclerView 동적 생성
                    this@MainActivity.runOnUiThread(Thread() {
                        if(itemCount == 0) {
                            Toast.makeText(this@MainActivity, "검색결과가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                        //RecyclerView 생성
                        recycler.layoutManager = LinearLayoutManager(this@MainActivity)
                        recycler.adapter = MyRecyclerAdapter(itemCount, listener =  object : MyRecyclerAdapter.OnItemClickListener {
                            override fun onItemClick(v: View, pos: Int, title : String) {
                                myIntent = Intent(this@MainActivity, WebViewActivity::class.java)
                                val myBundle = Bundle()
                                myBundle.putString("keyword", title)
                                myIntent.putExtra("myBundle", myBundle)
                                startActivity(myIntent)
                            }
                        }, items = croll)
                    })
                }
            }.start()

            //키패드 자동 다운
            val imm = this@MainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
}