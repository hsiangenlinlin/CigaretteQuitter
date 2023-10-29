package com.example.cigarettequitter

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [InitInfo::class, SmokeCount::class, Tip::class], version = 4)
abstract class AppDatabase: RoomDatabase() {
    abstract fun initInfoDao(): InitInfoDao
    abstract fun smokeCountDao(): SmokeCountDao
    abstract fun tipDao(): TipDao
    companion object{

        @Volatile
        private var INSTANCE : AppDatabase? = null
        //to avoid duplicate database
        fun getDatabase(context: Context): AppDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }

        }

    }
}