package com.example.namufinder.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/***
 *
 * Entity와 DAO를 가지고 데이터베이스를 생성하거나
 * 버전관리를 하는 클래스
 */
@Database(entities = [BookmarkEntity::class], version = 1)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun getBookmarkDAO() : BookmarkDAO
    companion object {
        private var instance : BookmarkDatabase? = null

        //싱글톤 패턴을 사용하여 데이터의 일치성 유지
        fun getInstance(context : Context) : BookmarkDatabase {
            //instance 생성
            if(instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, BookmarkDatabase::class.java, "bookmark.db")
                    .build() //Database builder 생성
            }
            return instance!!
        }
    }
}