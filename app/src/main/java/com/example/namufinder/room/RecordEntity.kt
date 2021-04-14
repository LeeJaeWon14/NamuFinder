package com.example.namufinder.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class RecordEntity (
    @PrimaryKey(autoGenerate = true) var id : Int = 0,
    var keyword : String = ""
)   : Serializable