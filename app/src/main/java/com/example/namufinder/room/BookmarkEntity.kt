package com.example.namufinder.room

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

/***
 * Room DB의 Table이 될
 * Entity 클래스(data class) 정의
 */
@Entity
data class BookmarkEntity (
    @PrimaryKey(autoGenerate = true) var id : Int = 0,
    var keyword : String = "",
    @Ignore var url : String = ""
)   : Serializable