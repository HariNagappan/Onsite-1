package com.example.fastapitesting

import android.content.Context
import android.content.Intent
import androidx.annotation.ContentView
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainViewModel: ViewModel(){
    private var _bedtimeHour: MutableStateFlow<Int> = MutableStateFlow(22)
    private var _bedtimeMinuite: MutableStateFlow<Int> = MutableStateFlow(30)
    var bedtimeHour: StateFlow<Int> = _bedtimeHour
    var bedtimeMinuite: StateFlow<Int> = _bedtimeMinuite

    private var _isSwitchSelected: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isSwitchSelected: StateFlow<Boolean> = _isSwitchSelected


    fun SetBedtimeHour(newHour:Int){
        _bedtimeHour.value=newHour
    }
    fun SetBedtimeMinuite(newMinuite:Int){
        _bedtimeMinuite.value=newMinuite
    }
    fun ToggleSwitch(){
        _isSwitchSelected.value=!isSwitchSelected.value
    }

    fun SaveToSharedPrefs(context:Context){
        val prefs=context.getSharedPreferences(SHARED_PREFS_FILENAME,Context.MODE_PRIVATE)
        val edit=prefs.edit()
        edit.putInt(HOUR_KEY,_bedtimeHour.value)
        edit.putInt(MINUITE_KEY,_bedtimeMinuite.value)
        edit.putBoolean(TOGGLE_KEY,_isSwitchSelected.value)

        edit.apply()
    }
    fun GetPrefsPrevValue(context: Context){
        val prefs=context.getSharedPreferences(SHARED_PREFS_FILENAME,Context.MODE_PRIVATE)
        _bedtimeHour.value=prefs.getInt(HOUR_KEY,22)
        _bedtimeMinuite.value=prefs.getInt(MINUITE_KEY,30)
        _isSwitchSelected.value=prefs.getBoolean(TOGGLE_KEY,true)
    }
}