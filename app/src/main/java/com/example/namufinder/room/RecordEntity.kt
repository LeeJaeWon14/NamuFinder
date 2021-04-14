package com.example.namufinder.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecordEntity (
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val keword : String = ""
)