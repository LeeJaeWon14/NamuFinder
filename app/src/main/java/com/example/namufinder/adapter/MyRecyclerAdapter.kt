package com.example.namufinder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.namufinder.R
import org.jsoup.select.Elements

class MyRecyclerAdapter(
    private var listener: OnItemClickListener? = null, // 크롤링으로 받아온 뿌려줄 elements 객체
    private var items: Elements?
) : RecyclerView.Adapter<MyRecyclerAdapter.MyRecyclerViewHolder>() {
    //ItemClickListener 정의
    interface OnItemClickListener {
        fun onItemClick(pos : Int, title : String)
    }

    //Holder 정의
    class MyRecyclerViewHolder(itemView : View, listener : OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.recycleItem)

        fun setTitle(title : String) {
            this.textView.text = title
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_view_item, parent, false)
        return MyRecyclerViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    override fun onBindViewHolder(holder: MyRecyclerViewHolder, position: Int) {
        holder.setTitle(items!!.get(position).text().toString())

        holder.textView.setOnClickListener {
            if(position != RecyclerView.NO_POSITION && listener != null) {
                listener!!.onItemClick(position, holder.textView.text.toString())
            }
        }
    }
}