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

    @Insert(onConflict = OnConflictStrategy.IGNORE) //중복값이 있을 경우 설정하는 어노테이션, 설정값은 무시
    fun insertBookmark(entity: BookmarkEntity)

    @Update
    fun updateBookmark(entity: BookmarkEntity)

    @Delete
    fun deleteBookmark(entity: BookmarkEntity)
}