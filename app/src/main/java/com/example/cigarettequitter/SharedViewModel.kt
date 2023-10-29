package com.example.cigarettequitter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    // Reference to the database object
    val database = AppDatabase.getDatabase(application.applicationContext)

}
