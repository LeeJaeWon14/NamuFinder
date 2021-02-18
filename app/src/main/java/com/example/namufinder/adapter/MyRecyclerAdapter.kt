package com.example.namufinder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.namufinder.R
import kotlinx.android.synthetic.main.recycle_view_item.view.*
import org.jsoup.select.Elements
import java.lang.NullPointerException

class MyRecyclerAdapter(itemCnt : Int = 1, listener : OnItemClickListener? = null, items : Elements?) : RecyclerView.Adapter<MyRecyclerAdapter.MyRecyclerViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(v : View, pos : Int, title : String)
    }

    class MyRecyclerViewHolder(itemView : View, listener : OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        private val textView = itemView.findViewById<TextView>(R.id.recycleItem)
        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(itemView, pos, textView.text.toString())
                }
            }
        }

        fun setTitle(title : String) {
            this.textView.text = title
        }
    }

    //private lateinit var listener : OnItemClickListener
    private var listener : OnItemClickListener?
    private var cnt : Int
    private var items : Elements?
    init {
        this.listener = listener
        this.cnt = itemCnt
        this.items = items
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_view_item, parent, false)
        return MyRecyclerViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return cnt
    }

    override fun onBindViewHolder(holder: MyRecyclerViewHolder, position: Int) {
        //holder.setTitle("갤럭시")
        holder.setTitle(items!!.get(position).text().toString())
    }
}