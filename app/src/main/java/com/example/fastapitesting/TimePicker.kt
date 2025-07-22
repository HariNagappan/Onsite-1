package com.example.fastapitesting

import android.widget.TimePicker
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(mainViewModel: MainViewModel, onConfirm:(TimePickerState)->Unit, onDismiss:()->Unit){
    val currentTime= Calendar.getInstance()
    val bedTimeHour by mainViewModel.bedtimeHour.collectAsState()
    val bedTimeMinuite by mainViewModel.bedtimeMinuite.collectAsState()

    val timePickerState = rememberTimePickerState(
        initialHour = bedTimeHour,
        initialMinute = bedTimeMinuite,
        is24Hour = true,
    )
    ActualDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}
@Composable
fun ActualDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("Ok")
            }
        },
        text = { content() }
    )
}