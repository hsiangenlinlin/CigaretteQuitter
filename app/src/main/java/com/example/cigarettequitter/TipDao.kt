package com.example.cigarettequitter

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
@Dao
interface TipDao {
    @Insert
    suspend fun insert(tip: Tip)
    @Update
    suspend fun update(tip: Tip)
    @Delete
    suspend fun delete(tip: Tip)
    @Query("SELECT * FROM tip_table WHERE id = :key")
    fun get(key: Long): LiveData<Tip>
    @Query("SELECT * FROM tip_table ORDER BY id DESC")
    fun getAll(): LiveData<List<Tip>>
}