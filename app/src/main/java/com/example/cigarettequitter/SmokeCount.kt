package com.example.cigarettequitter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "smoke_count")
data class SmokeCount(
    @PrimaryKey val id: Int=1,
    @ColumnInfo(name = "total_cigarette_smoked") val totalCigaretteSmoked: Int,
    @ColumnInfo(name = "total_cigarette_smoked_of_the_day") val totalCigaretteSmokedOfTheDay: Int
)
