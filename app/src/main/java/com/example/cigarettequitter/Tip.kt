package com.example.cigarettequitter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tip_table")
data class Tip(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "tip_title")
    var title: String = "",
    @ColumnInfo(name = "tip_content")
    var content: String = ""
)
