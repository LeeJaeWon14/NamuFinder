package com.example.namufinder.room

import androidx.room.*

/***
 * Entity 클래스(DTO 역할)를 가지고
 * CRUD 매트릭스를 진행할 DAO 정의
 */
@Dao
interface BookmarkDAO {
    @Query("SELECT * FROM bookmarkentity")
    fun getBookmark() : List<BookmarkEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBookmark(entity: BookmarkEntity)

    @Update
    fun updateBookmark(entity: BookmarkEntity)

    @Delete
    fun deleteBookmark(entity: BookmarkEntity)
}