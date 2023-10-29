package com.example.cigarettequitter

import androidx.room.*


@Dao
interface InitInfoDao {
    @Insert
    suspend fun insertAll(initInfo: InitInfo)
    @Delete
    suspend fun delete(initInfo: InitInfo)
    @Update
    suspend fun update(initInfo: InitInfo)

    //this is for checking if the user has initialized the info(there is only on tuple at a time)
    @Query("SELECT COUNT(*) FROM InitInfo")
    fun getInitInfoCount(): Int

//    @Query("SELECT name FROM InitInfo WHERE startDate = (SELECT MAX(startDate) FROM InitInfo)")
//    fun getNewestName(): String
//
//    @Query("SELECT price_of_cigarette FROM InitInfo WHERE startDate = (SELECT MAX(startDate) FROM InitInfo)")
//    fun getNewestPriceOfCigarette(): Float
//
//    @Query("SELECT amount_of_cigarette FROM InitInfo WHERE startDate = (SELECT MAX(startDate) FROM InitInfo)")
//    fun getNewestAmountOfCigarette(): Float
//
//    @Query("SELECT duration FROM InitInfo WHERE startDate = (SELECT MAX(startDate) FROM InitInfo)")
//    fun getNewestDuration(): Float
//
//    @Query("SELECT startDate FROM InitInfo WHERE startDate = (SELECT MAX(startDate) FROM InitInfo)")
//    fun getNewestStartDate(): Long


    @Query("SELECT name FROM InitInfo WHERE start_date = (SELECT MAX(start_date) FROM InitInfo)")
    suspend fun getNewestName(): String

    @Query("SELECT price_of_cigarette FROM InitInfo WHERE start_date = (SELECT MAX(start_date) FROM InitInfo)")
    suspend fun getNewestPriceOfCigarette(): Float

    @Query("SELECT amount_of_cigarette FROM InitInfo WHERE start_date = (SELECT MAX(start_date) FROM InitInfo)")
    suspend fun getNewestAmountOfCigarette(): Float

    @Query("SELECT duration FROM InitInfo WHERE start_date = (SELECT MAX(start_date) FROM InitInfo)")
    suspend fun getNewestDuration(): Float

    @Query("SELECT start_date FROM InitInfo WHERE start_date = (SELECT MAX(start_date) FROM InitInfo)")
    suspend fun getNewestStartDate(): Long
}
