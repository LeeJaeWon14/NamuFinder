package com.example.namufinder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.namufinder.R
import com.example.namufinder.room.BookmarkEntity
import com.example.namufinder.room.RecordEntity

class RecordRecyclerAdapter(
    entities : List<RecordEntity>,
    clickListener : OnItemClickListener,
    deleteListener : OnItemDeleteListener
) : RecyclerView.Adapter<RecordRecyclerAdapter.RecordRecyclerViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(v : View, pos : Int, title : String)
    }
    interface OnItemDeleteListener {
        fun onItemDeleteListener(v : View, pos : Int, entities: List<RecordEntity>)
    }

    //Field 정의
    private val entities = entities
    private val listener = clickListener
    private val deleteListener = deleteListener

    //ViewHolder 정의
    class RecordRecyclerViewHolder(itemView : View, listener : OnItemClickListener?, deleteListener : OnItemDeleteListener?, entities : List<RecordEntity>) : RecyclerView.ViewHolder(itemView) {
        private val textView : TextView = itemView.findViewById(R.id.recordItem)
        private val deleteButton : Button = itemView.findViewById(R.id.deleteButton)
        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(itemView, pos, textView.text.toString())
                }
            }
            deleteButton.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && deleteListener != null) {
                    deleteListener.onItemDeleteListener(itemView, pos, entities)
                }
            }
        }

        fun setTitle(title : String) {
            this.textView.text = title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordRecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.record_recycle_view_item, parent, false)
        return RecordRecyclerViewHolder(itemView, listener, deleteListener, entities)
    }

    override fun getItemCount(): Int {
        return entities.size
    }

    override fun onBindViewHolder(holder: RecordRecyclerViewHolder, position: Int) {
        if(entities.isEmpty()) {
            println("entities is empty")
            holder.setTitle("기록이 없습니다")
        }
        else {
            holder.setTitle(entities.get(position).keyword)
        }
    }
}