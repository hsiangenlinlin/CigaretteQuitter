package com.example.cigarettequitter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeViewModelFactory(private val initInfoDao: InitInfoDao, private val smokeCountDao: SmokeCountDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(initInfoDao, smokeCountDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
