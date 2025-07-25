package com.example.fastapitesting

import android.R.attr.fontWeight
import android.R.attr.label
import android.R.attr.text
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.RoundedCorner
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
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
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.jar.Manifest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(mainViewModel: MainViewModel,navController: NavController,modifier: Modifier= Modifier){
    var showTimePickerDialog by rememberSaveable { mutableStateOf(false) }
    val bedTimeHour by mainViewModel.bedtimeHour.collectAsState()
    val context =LocalContext.current
    val bedTimeMinuite by mainViewModel.bedtimeMinuite.collectAsState()
    val isSwitchSelected by mainViewModel.isSwitchSelected.collectAsState()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {

            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )
    LaunchedEffect(Unit) {
        mainViewModel.GetPrefsPrevValue(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        val workRequest = OneTimeWorkRequestBuilder<BackgroundWorker>().build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
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
                Text("Enter Sleeping Hours:")
                MyNumberPicker(mainViewModel = mainViewModel)
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
@Composable
fun MyNumberPicker(mainViewModel: MainViewModel){
    val sleepingHours by mainViewModel.sleepingHours.collectAsState()
    val localContext=LocalContext.current
    AndroidView(
        factory = { context ->
            NumberPicker(context).apply {
                minValue = 0
                maxValue = 12
                value = sleepingHours
                setOnValueChangedListener { _, _, newVal ->
                    mainViewModel.SetSleepingHours(newVal)
                    Log.d("SleepingHours","amount of sleeping hours:$newVal")
                    mainViewModel.SaveToSharedPrefs(context=localContext)
                }
            }
        }
    )

}