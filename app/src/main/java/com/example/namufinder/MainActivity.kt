package com.example.namufinder

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.namufinder.adapter.BookmarkRecyclerAdapter
import com.example.namufinder.adapter.MyRecyclerAdapter
import com.example.namufinder.adapter.RecordRecyclerAdapter
import com.example.namufinder.databinding.ActivityMainBinding
import com.example.namufinder.room.BookmarkDAO
import com.example.namufinder.room.MyRoomDatabase
import com.example.namufinder.room.RecordDAO
import com.example.namufinder.room.RecordEntity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            CoroutineScope(Dispatchers.IO).launch {
                //나무위키 검색결과 크롤링
                croll = CrollingTask.searchTask("https://namu.wiki/Search?q=${searchModelInit.searchKeyword.get()}")

                //크롤링으로 가져온 elements 사이즈로 RecyclerView의 item 수 지정
                itemCount = croll!!.size

                //UI스레드에서 RecyclerView 동적 생성
                withContext(Dispatchers.Main) {
                    if(itemCount == 0) {
                        Toast.makeText(this@MainActivity, "검색결과가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                    //RecyclerView 생성
                    else {
                        recycler.layoutManager = LinearLayoutManager(this@MainActivity)
                        recycler.adapter = MyRecyclerAdapter(itemCount, listener =  object : MyRecyclerAdapter.OnItemClickListener {
                            override fun onItemClick(v: View, pos: Int, title : String) {
                                goWebView(title)

                                //검색한 단어를 DB에 저장
                                val entity = RecordEntity(title)
                                MyRoomDatabase.getInstance(this@MainActivity)
                                    .getRecordDAO()
                                    .insertRecrod(entity)
                            }
                        }, items = croll)
                    }

                    topLine.visibility = View.VISIBLE
                    bottomLine.visibility = View.VISIBLE
                }
            }

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
                val entities = getBookmarkDAO().getBookmark()

                recView.layoutManager = LinearLayoutManager(this@MainActivity)
                recView.adapter = BookmarkRecyclerAdapter(entities, clickListener = object : BookmarkRecyclerAdapter.OnItemClickListener {
                    override fun onItemClick(v: View, pos: Int, title: String) {
                        //리싸이클러뷰의 item click listener 정의

                        //해당 항목으로 웹뷰 이동
                        goWebView(title.split(". ")[1])
                    }
                }, longClickListener = object : BookmarkRecyclerAdapter.OnItemLongClickListener {
                    //리싸이클러뷰의 item long click listener 정의
                    override fun onItemLongClick(v: View, pos: Int, title: String) {
                        AlertDialog.Builder(this@MainActivity)
                            .setMessage("삭제하시겠습니까?")
                            .setPositiveButton("삭제", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    val entity = entities.get(pos)
                                    //가져온 entity로 delete 실행
                                    getBookmarkDAO().deleteBookmark(entity)
                                }
                            })
                            .setNegativeButton("취소", null)
                            .show()
                    }
                })
            }
            dlg.show()
        }

        fun showRecrod(view : View) {
            capabilityCheckRecord()

            val entities = getRecordDAO().getRecord()
            println("entities >> ${entities.toString()}")

            val dlgView = View.inflate(this@MainActivity, R.layout.record_layout, null)
            val dlg = AlertDialog.Builder(this@MainActivity).create()

            dlg.setView(dlgView)
            dlg.window?.setBackgroundDrawableResource(R.drawable.block)

            val recordRecycler = dlgView.findViewById<RecyclerView>(R.id.recordRecycler)
            recordRecycler.layoutManager = LinearLayoutManager(this@MainActivity)
            recordRecycler.adapter = RecordRecyclerAdapter(entities, object : RecordRecyclerAdapter.OnItemClickListener {
                override fun onItemClick(v: View, pos: Int, title: String) {
                    //ItemClick 구현
                    goWebView(title)
                }
            }, object : RecordRecyclerAdapter.OnItemDeleteListener {
                override fun onItemDeleteListener(v: View, pos: Int, entities: List<RecordEntity>) {
                    //ItemDelete 구현
                    val entity = entities.get(pos)
                    getRecordDAO().deleteRecord(entity)

                    entities.drop(pos)

                    Snackbar.make(v, "삭제되었습니다", Snackbar.LENGTH_SHORT).show()
                }
            })

            dlg.show()
        }
    }

    private fun goWebView(keyword : String) {
        val bundle = Bundle()
        bundle.putString("keyword", keyword)
        val intent = Intent(this@MainActivity, WebViewActivity::class.java)
        intent.putExtra("myBundle", bundle)
        startActivity(intent)
    }

    fun getBookmarkDAO() : BookmarkDAO {
        return MyRoomDatabase.getInstance(this@MainActivity).getBookmarkDAO()
    }

    fun getRecordDAO() : RecordDAO {
        return MyRoomDatabase.getInstance(this@MainActivity).getRecordDAO()
    }

    //저장된 기록이 5개 이상이면 마지막 기록 삭제
    fun capabilityCheckRecord() {
        val entities = getRecordDAO().getRecord()
        if(entities.size > 50) {
            getRecordDAO().deleteRecord(entities.get(entities.size -1))
        }
    }
}