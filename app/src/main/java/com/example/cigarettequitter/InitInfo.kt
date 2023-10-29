package com.example.cigarettequitter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InitInfo(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "price_of_cigarette") val priceOfCigarette: Float,
    @ColumnInfo(name = "amount_of_cigarette")val amountOfCigarette: Float,
    @ColumnInfo(name = "duration")val duration: Float,
    @ColumnInfo(name = "start_date") val startDate: Long // Store the start date as a Unix timestamp
)
