package com.example.newssubscription.alarm.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newssubscription.alarm.presentation.components.AlarmDetailsSection
import com.example.newssubscription.alarm.presentation.components.AlarmToggleRow
import com.example.newssubscription.alarm.presentation.mvi.AlarmIntent
import com.example.newssubscription.alarm.presentation.mvi.AlarmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    // Handle dismiss action when alarm finishes
    LaunchedEffect(viewModel.finishAlarm) {
        viewModel.finishAlarm.collect { if (it) onDismiss() }
    }

    LaunchedEffect(Unit) {
        // Load alarm when BottomSheet is opened
        viewModel.onIntent(AlarmIntent.LoadAlarm)
    }

    if (state.value.loading) {
        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(30.dp))
    } else {
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val bottomSheetHeight = screenHeight * 0.8f

        val timePickerState = rememberTimePickerState(
            initialHour = state.value.alarm?.hour ?: 1,
            initialMinute = state.value.alarm?.minute ?: 10
        )
        val selectedDay = remember { mutableStateOf((state.value.alarm?.dayOfWeek ?: 2) - 1) }

        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bottomSheetHeight)
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                AlarmToggleRow(
                    isEnabled = state.value.isAlarmEnabled,
                    onToggle = { viewModel.onIntent(AlarmIntent.OnToggle) }
                )

                if (state.value.isAlarmEnabled) {
                    AlarmDetailsSection(
                        selectedDay = selectedDay.value,
                        onDaySelect = { selectedDay.value = it },
                        timePickerState = timePickerState,
                        onSetClick = {
                            viewModel.onIntent(
                                AlarmIntent.ScheduleAlarm(
                                    hour = timePickerState.hour,
                                    minute = timePickerState.minute,
                                    day = selectedDay.value + 1
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
