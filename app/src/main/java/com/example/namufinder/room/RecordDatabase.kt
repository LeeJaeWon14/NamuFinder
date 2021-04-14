package com.example.namufinder.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [RecordEntity::class], version = 1) //스키마 확인 불필요
abstract class RecordDatabase : RoomDatabase() {
    abstract fun getRecordDAO() : RecordDAO
    companion object {
        private var instance : RecordDatabase? = null

        //싱글톤 패턴을 사용하여 데이터의 일치성 유지
        fun getInstance(context : Context) : RecordDatabase {
            //instance 생성
            if(instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, RecordDatabase::class.java, "record.db")
                    .build() //Database builder 생성
            }
            return instance!!
        }
    }
}