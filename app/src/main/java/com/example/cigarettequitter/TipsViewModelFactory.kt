package com.example.cigarettequitter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TipsViewModelFactory(private val dao: TipDao)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TipViewModel::class.java)) {
            return TipViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
