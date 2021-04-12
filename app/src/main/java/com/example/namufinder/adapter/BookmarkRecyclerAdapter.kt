package com.example.namufinder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.namufinder.R
import com.example.namufinder.room.BookmarkEntity

class BookmarkRecyclerAdapter(entities : List<BookmarkEntity>, listener : OnItemClickListener) : RecyclerView.Adapter<BookmarkRecyclerAdapter.BookmarkRecyclerViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(v : View, pos : Int, title : String)
    }

    //Field 정의
    private val entities = entities
    private val listener = listener

    //ViewHolder 정의
    class BookmarkRecyclerViewHolder(itemView : View, listener : OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        private val textView : TextView = itemView.findViewById(R.id.recycleItem)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkRecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_view_item, parent, false)
        return BookmarkRecyclerViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return entities.size
    }

    override fun onBindViewHolder(holder: BookmarkRecyclerViewHolder, position: Int) {
        holder.setTitle("${position +1}. ${entities.get(position).keyword}")
    }
}