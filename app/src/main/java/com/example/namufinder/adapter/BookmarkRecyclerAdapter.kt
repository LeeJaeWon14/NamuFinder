package com.example.namufinder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.namufinder.R
import com.example.namufinder.room.BookmarkEntity
import com.example.namufinder.room.MyRoomDatabase
import com.google.android.material.snackbar.Snackbar

class BookmarkRecyclerAdapter(
    entities : List<BookmarkEntity>,
    clickListener : OnItemClickListener
) : RecyclerView.Adapter<BookmarkRecyclerAdapter.BookmarkRecyclerViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(title : String)
    }
    interface OnItemLongClickListener {
        fun onItemLongClick(pos : Int, recEntities : List<BookmarkEntity>)
    }

    //Field 정의
    private val entities = entities.toMutableList()
    private val listener = clickListener
    private var context : Context? = null

    //ViewHolder 정의
    class BookmarkRecyclerViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val textView : TextView = itemView.findViewById(R.id.recycleItem)


        fun setTitle(title : String) {
            this.textView.text = title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkRecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_view_item, parent, false)
        context = parent.context
        return BookmarkRecyclerViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return entities.size
    }

    override fun onBindViewHolder(holder: BookmarkRecyclerViewHolder, position: Int) {
        if(entities.isEmpty()) {
            holder.setTitle("기록이 없습니다")
        }
        else {
            holder.setTitle("${position +1}. ${entities.get(position).keyword}")
        }

        holder.textView.setOnClickListener {
            val pos = position
            if(pos != RecyclerView.NO_POSITION) {
                listener.onItemClick(holder.textView.text.toString())
            }
        }

        holder.textView.setOnLongClickListener {
            val pos = position
            if(pos != RecyclerView.NO_POSITION) {
                AlertDialog.Builder(context!!)
                    .setMessage("삭제하시겠습니까?")
                    .setPositiveButton("삭제") { dialog, which ->
                        removeItem(position)
                        Snackbar.make(holder.itemView, "삭제되었습니다.", Snackbar.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }
            false
        }
    }

    private fun removeItem(position : Int) {
        val entity = entities.get(position)
        entities.removeAt(position)
        MyRoomDatabase.getInstance(context!!).getBookmarkDAO().deleteBookmark(entity)

        notifyDataSetChanged()
    }
}