package com.example.fastapitesting

import android.text.Layout
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(mainViewModel: MainViewModel, onConfirm:(TimePickerState)->Unit, onDismiss:()->Unit){
    val bedTimeHour by mainViewModel.bedtimeHour.collectAsState()
    val bedTimeMinuite by mainViewModel.bedtimeMinuite.collectAsState()
    var isInputTime by remember{ mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = bedTimeHour,
        initialMinute = bedTimeMinuite,
        is24Hour = true,
    )
    ActualDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) },
        ToggleButton = {
            IconButton(
                onClick = {isInputTime=!isInputTime}
            ) {
                Icon(
                    imageVector = if(isInputTime) Icons.Default.EditCalendar else Icons.Default.AccessTime,
                    contentDescription = "Choose between typing and clock gui"
                )
            }
        },
        content = {
            if(!isInputTime) {
                TimePicker(
                    state = timePickerState,
                )
            }
            else {
                TimeInput(
                    state = timePickerState
                )
            }
        }
    )
}
@Composable
fun ActualDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    ToggleButton: @Composable ()->Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
    ){
        Card{
            Column(modifier=Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    content()
                    Spacer(modifier=Modifier.height(40.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        ToggleButton()
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = { onDismiss() }) {
                            Text("Dismiss")
                        }
                        TextButton(onClick = { onConfirm() }) {
                            Text("Ok")
                        }
                    }
            }
        }
    }
}