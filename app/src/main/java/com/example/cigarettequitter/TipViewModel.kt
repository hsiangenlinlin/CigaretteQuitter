package com.example.cigarettequitter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TipViewModel(val dao: TipDao)  : ViewModel() {

    var newTipTitle = ""
    var newTipContent =""
    val tips = dao.getAll()

    fun addTip() {
        viewModelScope.launch {
            val tip = Tip()
            tip.title = newTipTitle
            tip.content = newTipContent
            dao.insert(tip)
        }
    }
}
//class TipViewModel : ViewModel() {
//
//    private val _tips = MutableLiveData<List<Tip>>()
//    val tips: LiveData<List<Tip>> get() = _tips
//
//    init {
//        // Create some sample tips
//        val tip1 = Tip(title = "Tip 1", content = "This is the content for Tip 1.")
//        val tip2 = Tip(title = "Tip 2", content = "This is the content for Tip 2.")
//        val tip3 = Tip(title = "Tip 3", content = "This is the content for Tip 3.")
//
//        // Add the tips to the list
//        val tipList = listOf(tip1, tip2, tip3)
//
//        // Update the LiveData with the list of tips
//        _tips.value = tipList
//    }
//}