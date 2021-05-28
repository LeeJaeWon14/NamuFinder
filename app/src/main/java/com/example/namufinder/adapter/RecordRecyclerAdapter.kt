package com.example.namufinder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.namufinder.R
import com.example.namufinder.room.MyRoomDatabase
import com.example.namufinder.room.RecordEntity
import com.google.android.material.snackbar.Snackbar

class RecordRecyclerAdapter(
    entities : List<RecordEntity>,
    clickListener : OnItemClickListener
) : RecyclerView.Adapter<RecordRecyclerAdapter.RecordRecyclerViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(title : String)
    }
    /*interface OnItemDeleteListener {
        fun onItemDeleteListener(v : View, pos : Int, entities: List<RecordEntity>)
    }*/

    //Field 정의
    private val entities = entities.toMutableList()
    private val listener = clickListener
    //private val deleteListener = deleteListener
    private var context : Context? = null

    //ViewHolder 정의
    class RecordRecyclerViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val textView : TextView = itemView.findViewById(R.id.recordItem)
        val deleteButton : Button = itemView.findViewById(R.id.deleteButton)

        fun setTitle(title : String) {
            this.textView.text = title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordRecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.record_recycle_view_item, parent, false)
        context = parent.context!!
        return RecordRecyclerViewHolder(itemView)
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

        holder.textView.setOnClickListener {
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(holder.textView.text.toString())
            }
        }
        holder.deleteButton.setOnClickListener {
            if(position != RecyclerView.NO_POSITION) {
                //deleteListener.onItemDeleteListener(holder.itemView, position, entities)

                removeItem(position)
                Snackbar.make(holder.itemView, "삭제되었습니다", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeItem(position : Int) {
        val entity = entities.get(position)
        entities.removeAt(position)
        MyRoomDatabase.getInstance(context!!).getRecordDAO().deleteRecord(entity)

        notifyDataSetChanged()
    }
}