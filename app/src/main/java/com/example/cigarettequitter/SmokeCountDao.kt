package com.example.cigarettequitter
import androidx.room.*


@Dao
interface SmokeCountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(smokeCount: SmokeCount)

    @Query("SELECT * FROM smoke_count WHERE id = :id")
    suspend fun getSmokeCountById(id: Int): SmokeCount?
}