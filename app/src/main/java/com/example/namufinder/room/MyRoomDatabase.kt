package com.example.namufinder.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/***
 *
 * Entity와 DAO를 가지고 데이터베이스를 생성하거나
 * 버전관리를 하는 클래스
 */
@Database(entities = [BookmarkEntity::class, RecordEntity::class], version = 1, exportSchema = false)
abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun getBookmarkDAO() : BookmarkDAO
    abstract fun getRecordDAO() : RecordDAO

    companion object {
        private var instance : MyRoomDatabase? = null

        //싱글톤 패턴을 사용하여 데이터의 일치성 유지
        fun getInstance(context : Context) : MyRoomDatabase {
            //instance 생성
            if(instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, MyRoomDatabase::class.java, "namuRoom.db")
                    .allowMainThreadQueries()
                    .build()//Database builder 생성
            }
            return instance!!
        }
    }
}