package com.example.newssubscription.alarm.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newssubscription.alarm.domain.model.Alarm
import com.example.newssubscription.alarm.domain.repository.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmState())
    val state = _state.asStateFlow()
    private val _finishAlarm = Channel<Boolean>()
    val finishAlarm = _finishAlarm.receiveAsFlow()

    fun onIntent(intent: AlarmIntent) {
        when (intent) {
            AlarmIntent.LoadAlarm -> loadAlarm()

            is AlarmIntent.OnTimeChange ->
                _state.update {
                    it.copy(alarm = it.alarm?.copy(hour = intent.hour, minute = intent.minute))
                }

            AlarmIntent.OnToggle -> toggleAlarm()

            is AlarmIntent.ScheduleAlarm ->
                scheduleAlarm(intent.hour, intent.minute, intent.day)

            is AlarmIntent.OnDayChange -> _state.update {
                it.copy(
                    alarm = it.alarm?.copy(dayOfWeek = intent.day)
                )
            }
        }
    }

    private fun toggleAlarm() {
        _state.update { it.copy(isAlarmEnabled = !it.isAlarmEnabled) }
        if (_state.value.alarm != null && !_state.value.isAlarmEnabled) cancelAlarm()
    }


    private fun loadAlarm() = viewModelScope.launch {
        _state.update { it.copy(loading = true) }
        val alarm = alarmScheduler.getAlarm()
        if (alarm != null) _state.update {
            it.copy(alarm = alarm, isAlarmEnabled = true, loading = false)
        }
        else _state.update {
            it.copy(alarm = createDefaultAlarm(), loading = false, isAlarmEnabled = false)
        }
    }


    private fun scheduleAlarm(hour: Int, minute: Int, day: Int) = viewModelScope.launch {
        val alarm = Alarm(id = 1, hour = hour, minute = minute, dayOfWeek = day)
        alarmScheduler.schedule(alarm)
        _state.update { it.copy(alarm = alarm, isAlarmEnabled = true) }
        _finishAlarm.send(true)
    }


    private fun cancelAlarm() = viewModelScope.launch {
        _state.value.alarm?.let { alarmScheduler.cancel(it.id) }
        _state.update { it.copy(isAlarmEnabled = false) }
    }

    private fun createDefaultAlarm(): Alarm {
        val calendar = Calendar.getInstance()
        return Alarm(
            id = 1,
            hour = calendar.get(Calendar.HOUR_OF_DAY),
            minute = calendar.get(Calendar.MINUTE),
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        )
    }
}

