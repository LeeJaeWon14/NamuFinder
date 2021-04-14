package com.example.namufinder.room

import androidx.room.*

@Dao
interface RecordDAO {
    @Query("SELECT * FROM recordentity")
    fun getRecord() : List<RecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecrod(entity : RecordEntity)

    @Update
    fun updateRecord(entity : RecordEntity)

    @Delete
    fun deleteRecord(entity : RecordEntity)
}