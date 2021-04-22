package com.example.namufinder.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class RecordEntity (
    var keyword : String = ""
)   : Serializable {
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}