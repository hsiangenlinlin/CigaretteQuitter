package com.example.cigarettequitter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit

class HomeViewModel(private val initInfoDao: InitInfoDao, private val smokeCountDao: SmokeCountDao):ViewModel() {

    private var totalCigaretteSmoked = 0
    private var totalCigaretteSmokedOfTheDay = 0
    private var currentDayOfYear: Int = -1


    private val _name = MutableLiveData<String?>()
    private val _quitDays = MutableLiveData<String?>()
    private val _moneySaved = MutableLiveData<String?>()
    private val _avoidCigarette = MutableLiveData<String?>()
    private val _currentQuota = MutableLiveData<Int?>()

    val name: MutableLiveData<String?>
        get() = _name
    val quitDays: MutableLiveData<String?>
        get() = _quitDays
    val moneySaved: MutableLiveData<String?>
        get() = _moneySaved
    val avoidCigarette: MutableLiveData<String?>
        get() = _avoidCigarette
    val currentQuota: MutableLiveData<Int?>
        get() = _currentQuota
///////////////////////////////////////////////////////
    //////////////////////testing///////////////////////////////
    private val _totalCigaretteSmokedAllTime = MutableLiveData<Int?>()
    private val _totalCigaretteSmokedToday = MutableLiveData<Int?>()
    val totalCigaretteSmokedAllTime: MutableLiveData<Int?>
        get() = _totalCigaretteSmokedAllTime
    val totalCigaretteSmokedToday: MutableLiveData<Int?>
        get() = _currentQuota
//////////////////////////////////////////////////////
//////////////////////////////////////////////////
    init {
        viewModelScope.launch {
            val smokeCount = getSmokeCount()
            totalCigaretteSmoked = smokeCount?.totalCigaretteSmoked ?: 0
            totalCigaretteSmokedOfTheDay = smokeCount?.totalCigaretteSmokedOfTheDay ?: 0
            updateCurrentQuota()
            initializeTotalCigaretteSmokedOfTheDay()
            updateDisplayData()
        }
        startQuotaUpdateTimer()
    }
    private suspend fun updateDisplayData() {
        _name.value = nameDisplay()
        _quitDays.value = quitDaysDisplay()
        _moneySaved.value = moneySavedDisplay()
        _avoidCigarette.value = avoidCigaretteDisplay()

        /////////testing////////////
        _totalCigaretteSmokedAllTime.value=getTotalCigaretteSmoked()
        _totalCigaretteSmokedToday.value=getTotalCigaretteSmokedOfTheDay()
        //////////////////////////////
    }
    fun decCurrentQuota() {
        currentQuota.value?.let { quota ->
            if (quota > 0) {
                _currentQuota.value = quota - 1
            }
        }
    }
    fun incCurrentQuota() {
        currentQuota.value?.let { quota ->
            _currentQuota.value = quota + 1

        }
    }
    private fun updateCurrentQuota() {
        viewModelScope.launch {
            val quota = quotaOfTheDay()
            _currentQuota.value = quota
        }
    }

    private fun startQuotaUpdateTimer() {
        viewModelScope.launch {
            while (true) {
                delay(getTimeUntilNextDay())
                updateCurrentQuota()
                initializeTotalCigaretteSmokedOfTheDay()
            }
        }
    }
    private fun getTimeUntilNextDay(): Long {
        val currentTime = System.currentTimeMillis()
        val midnightMillis = TimeUnit.MILLISECONDS.toDays(currentTime) * TimeUnit.DAYS.toMillis(1)
        return midnightMillis + TimeUnit.DAYS.toMillis(1) - currentTime
    }

     private suspend fun quotaOfTheDay(): Int{
        // interval means in how many days the quota of the day decrease 1
        val interval:Int = if(getNewestDuration()>=getNewestAmountOfCigarette()) getNewestDuration().toInt()/(getNewestAmountOfCigarette().toInt()-1) else 1
        // step is the number of cigarette to decrease
        val step = getDaysPassed()/interval
        return if (getDaysPassed()<getNewestDuration()){
            getNewestAmountOfCigarette().toInt()-step
        }else{
            0
        }
    }
    private fun initializeTotalCigaretteSmokedOfTheDay() {
        val current = getCurrentDayOfYear()
        if (current != currentDayOfYear) {
            totalCigaretteSmokedOfTheDay = 0
            currentDayOfYear = current
        }
    }
    private fun getCurrentDayOfYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_YEAR)
    }

    fun incTotalCigaretteSmoked(){
        totalCigaretteSmoked++
        totalCigaretteSmokedOfTheDay++
        updateSmokeCount(totalCigaretteSmoked,totalCigaretteSmokedOfTheDay)
    }
    fun decTotalCigaretteSmoked(){
        totalCigaretteSmoked--
        totalCigaretteSmokedOfTheDay--
        updateSmokeCount(totalCigaretteSmoked,totalCigaretteSmokedOfTheDay)
    }
    suspend fun getTotalCigaretteSmoked(): Int {
        val smokeCount = getSmokeCount()
        return smokeCount?.totalCigaretteSmoked ?: 0
    }

    suspend fun getTotalCigaretteSmokedOfTheDay(): Int {
        val smokeCount = getSmokeCount()
        return smokeCount?.totalCigaretteSmokedOfTheDay ?: 0
    }
    private fun updateSmokeCount(totalCigaretteSmoked: Int, totalCigaretteSmokedOfTheDay: Int) {
        viewModelScope.launch {
            val smokeCount = SmokeCount(
                id = 1,
                totalCigaretteSmoked = totalCigaretteSmoked,
                totalCigaretteSmokedOfTheDay = totalCigaretteSmokedOfTheDay
            )
            smokeCountDao.insert(smokeCount)
        }
    }

    private suspend fun getSmokeCount(): SmokeCount? {
        return withContext(Dispatchers.IO) {
            smokeCountDao.getSmokeCountById(1)
        }
    }


    private suspend fun nameDisplay():String{
        return getNewestName()
    }
    private suspend fun quitDaysDisplay():String{
        val days = getDaysPassed()-getNewestDuration()
        return days.toString()
    }


    private suspend fun moneySavedDisplay():String{
        // TODO: need to optimize the logic
        val savedMoney = getNewestPriceOfCigarette()*getNewestAmountOfCigarette()*getDaysPassed()
        return savedMoney.toString()
    }
    private suspend fun avoidCigaretteDisplay():String{
        val avoidedAmount = getNewestAmountOfCigarette()*getDaysPassed()-totalCigaretteSmoked
        return avoidedAmount.toString()
    }

    private suspend fun getDaysPassed(): Int {
        //return how many days user has been quitting
        val startDate = getNewestStartDate()
        val currentTime = System.currentTimeMillis()
        val millisecondsPassed = currentTime - startDate
        val daysPassed = TimeUnit.MILLISECONDS.toDays(millisecondsPassed)
        return daysPassed.toInt()
    }
    private suspend fun getNewestName(): String {
        return withContext(Dispatchers.IO) {
            initInfoDao.getNewestName()
        }
    }
    private suspend fun getNewestPriceOfCigarette(): Float {
        return withContext(Dispatchers.IO) {
            initInfoDao.getNewestPriceOfCigarette()
        }
    }
    private suspend fun getNewestAmountOfCigarette(): Float {
        return withContext(Dispatchers.IO) {
            initInfoDao.getNewestAmountOfCigarette()
        }
    }
    private suspend fun getNewestDuration(): Float {
        return withContext(Dispatchers.IO) {
            initInfoDao.getNewestDuration()
        }
    }
    private suspend fun getNewestStartDate(): Long {
        return withContext(Dispatchers.IO) {
            initInfoDao.getNewestStartDate()
        }
    }
}