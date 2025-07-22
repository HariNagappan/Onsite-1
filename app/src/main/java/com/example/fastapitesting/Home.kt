package com.example.fastapitesting

import android.R.attr.fontWeight
import android.R.attr.text
import android.annotation.SuppressLint
import android.util.Log
import android.view.RoundedCorner
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(mainViewModel: MainViewModel,navController: NavController,modifier: Modifier= Modifier){
    var showTimePickerDialog by rememberSaveable { mutableStateOf(false) }
    val bedTimeHour by mainViewModel.bedtimeHour.collectAsState()
    val bedTimeMinuite by mainViewModel.bedtimeMinuite.collectAsState()
    val isSwitchSelected by mainViewModel.isSwitchSelected.collectAsState()
    val context =LocalContext.current
//    LaunchedEffect(Unit) {
//        mainViewModel.GetPrefsPrevValue(context)
//        val workRequest = OneTimeWorkRequestBuilder<BackgroundWorker>().build()
//        WorkManager.getInstance(context).enqueue(workRequest)
//    }
    Box(modifier=Modifier.fillMaxSize()){

        Column(
            modifier=Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            Text(
                text = "Current Bedtime",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${bedTimeHour}:${bedTimeMinuite} ${if(bedTimeHour>=12) "PM" else "AM"}",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = {
                        showTimePickerDialog=true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Bedtime,
                        tint = Color.Blue,
                        contentDescription = "Change Time",
                        modifier=Modifier.size(32.dp)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Disable wifi upon Reaching bedtime",
                    fontSize = 16.sp,
                )
                Switch(
                    checked = isSwitchSelected,
                    onCheckedChange = {
                        mainViewModel.ToggleSwitch()
                        mainViewModel.SaveToSharedPrefs(context=context)
                    }
                )
            }
        }
        if(showTimePickerDialog){
            TimePickerDialog(
                mainViewModel=mainViewModel,
                onConfirm = {timePickerState->
                    mainViewModel.SetBedtimeHour(newHour = timePickerState.hour)
                    mainViewModel.SetBedtimeMinuite(newMinuite = timePickerState.minute)
                    Log.d("general","newhour:${mainViewModel.bedtimeHour.value},newminuite:${mainViewModel.bedtimeMinuite.value}")
                    mainViewModel.SaveToSharedPrefs(context=context)

                    showTimePickerDialog=false
            },
                onDismiss = {showTimePickerDialog=false})
        }
    }
}