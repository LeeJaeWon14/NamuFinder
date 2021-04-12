package com.example.namufinder.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/***
 * Room DB의 Table이 될
 * Entity 클래스 정의
 */
@Entity
class BookmarkEntity : Serializable {
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0

    val keyword : String = ""

    val url : String = ""
}