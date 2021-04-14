package com.example.namufinder

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.namufinder.adapter.BookmarkRecyclerAdapter
import com.example.namufinder.adapter.MyRecyclerAdapter
import com.example.namufinder.databinding.ActivityMainBinding
import com.example.namufinder.room.BookmarkDatabase
import com.example.namufinder.room.BookmarkEntity
import com.example.namufinder.room.RecordDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bookmark_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        recordList.adapter = listAdapterInit()
        recordList.setOnItemClickListener { parent, view, position, id ->
            //goWebView(parent.adapter.getItem(position).toString())
            Toast.makeText(this@MainActivity, parent.adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private var time : Long = 0
    override fun onBackPressed() { //뒤로가기 클릭 시 종료 메소드
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
                        else {
                            recordList.visibility = View.INVISIBLE
                            recycler.layoutManager = LinearLayoutManager(this@MainActivity)
                            recycler.adapter = MyRecyclerAdapter(itemCount, listener =  object : MyRecyclerAdapter.OnItemClickListener {
                                override fun onItemClick(v: View, pos: Int, title : String) {
                                    goWebView(title)
                                }
                            }, items = croll)
                        }
                    })
                }
            }.start()

            //키패드 자동 다운
            val imm = this@MainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }

        fun selectBookmark(view : View) {
            //Dialog 초기화
            val dlgView = View.inflate(this@MainActivity, R.layout.bookmark_layout, null)
            val dlg = AlertDialog.Builder(this@MainActivity).create()
            dlg.setView(dlgView)
            dlg.window?.setBackgroundDrawableResource(R.drawable.block)

            val recView = dlgView.findViewById<RecyclerView>(R.id.bookmarkRecycler)
            //Room Database 조회
            CoroutineScope(Dispatchers.IO).launch {
                //entities select
                val entities = BookmarkDatabase.getInstance(this@MainActivity)
                    .getBookmarkDAO()
                    .getBookmark()

                recView.layoutManager = LinearLayoutManager(this@MainActivity)
                recView.adapter = BookmarkRecyclerAdapter(entities, clickListener = object : BookmarkRecyclerAdapter.OnItemClickListener {
                    override fun onItemClick(v: View, pos: Int, title: String) { //리싸이클러뷰의 아이템 click listener 정의
                        goWebView(title.split(". ")[1])
                    }
                }, longClickListener = object : BookmarkRecyclerAdapter.OnItemLongClickListener { //리싸이클러뷰의 아이템 long click listener 정의
                    override fun onItemLongClick(v: View, pos: Int, title: String) {
                        //entity get
                        val entity = entities.get(pos)
                        //가져온 entity로 delete 실행
                        BookmarkDatabase.getInstance(this@MainActivity)
                            .getBookmarkDAO()
                            .deleteBookmark(entity)
                    }
                })
            }

            dlg.show()
        }

        fun longClick(view : View) {
            Toast.makeText(this@MainActivity, "Long Click", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listAdapterInit() : ArrayAdapter<String> {
        val arrayList : ArrayList<String> = ArrayList<String>()
        CoroutineScope(Dispatchers.Default).launch {
            val entities = RecordDatabase.getInstance(this@MainActivity)
                .getRecordDAO()
                .getRecord()

            for(entity in entities) {
                arrayList.add(entity.keword)
            }
        }
        val adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_list_item_1, arrayList)
        return adapter
    }

    private fun goWebView(keyword : String) {
        val bundle = Bundle()
        bundle.putString("keyword", keyword)
        val intent = Intent(this@MainActivity, WebViewActivity::class.java)
        intent.putExtra("myBundle", bundle)
        startActivity(intent)
    }
}