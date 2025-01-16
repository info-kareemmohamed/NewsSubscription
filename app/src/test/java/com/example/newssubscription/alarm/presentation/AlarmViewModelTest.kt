package com.example.newssubscription.alarm.presentation

import com.example.newssubscription.TestCoroutineRule
import com.example.newssubscription.alarm.domain.model.Alarm
import com.example.newssubscription.alarm.domain.repository.AlarmScheduler
import com.example.newssubscription.alarm.presentation.mvi.AlarmIntent
import com.example.newssubscription.alarm.presentation.mvi.AlarmViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar


@OptIn(ExperimentalCoroutinesApi::class)
class AlarmViewModelTest {


    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val alarmScheduler: AlarmScheduler = mockk()

    private lateinit var viewModel: AlarmViewModel


    @Before
    fun setUp() {
        viewModel = AlarmViewModel(alarmScheduler)
    }

    @Test
    fun `Given no existing alarm, When loadAlarm is called, Then default alarm should be created and loading should be false`() =
        runTest {
            // Given
            coEvery { alarmScheduler.getAlarm() } returns null

            // When
            viewModel.onIntent(AlarmIntent.LoadAlarm)

            // Wait for the coroutine to complete
            advanceUntilIdle()

            // Then
            assertNotNull(viewModel.state.value.alarm)
            assertEquals(false, viewModel.state.value.loading)
            assertEquals(false, viewModel.state.value.isAlarmEnabled)
        }

    @Test
    fun `Given no existing alarm, When loadAlarm is called, Then default alarm should be created with current time`() =
        runTest {
            // Given
            coEvery { alarmScheduler.getAlarm() } returns null
            val currentTime = Calendar.getInstance()

            // When
            viewModel.onIntent(AlarmIntent.LoadAlarm)


            // Wait for the coroutine to complete
            advanceUntilIdle()

            // Then
            val state = viewModel.state.value
            assertNotNull(state.alarm)
            assertEquals(currentTime.get(Calendar.HOUR_OF_DAY), state.alarm?.hour)
            assertEquals(currentTime.get(Calendar.MINUTE), state.alarm?.minute)
            assertEquals(currentTime.get(Calendar.DAY_OF_WEEK), state.alarm?.dayOfWeek)
        }


    @Test
    fun `Given an alarm, When scheduleAlarm is called, Then alarm should be scheduled and finishAlarm should emit true`() =
        runTest {
            // Given
            val alarm = Alarm(id = 1, hour = 10, minute = 30, dayOfWeek = Calendar.MONDAY)
            coEvery { alarmScheduler.getAlarm() } returns alarm
            coEvery { alarmScheduler.schedule(alarm) } returns Unit

            // When
            viewModel.onIntent(AlarmIntent.LoadAlarm)
            viewModel.onIntent(
                AlarmIntent.ScheduleAlarm(hour = 10, minute = 30, day = Calendar.MONDAY)
            )

            // Wait for the coroutine to complete
            advanceUntilIdle()


            // Then
            assertEquals(alarm, viewModel.state.value.alarm)
            assertEquals(true, viewModel.state.value.isAlarmEnabled)
            assertEquals(true, viewModel.finishAlarm.first())
        }

    @Test
    fun `Given an alarm, When cancelAlarm is called, Then alarm should be canceled and isAlarmEnabled should be false`() =
        runTest {
            // Given
            val alarm = Alarm(id = 1, hour = 10, minute = 30, dayOfWeek = Calendar.MONDAY)
            coEvery { alarmScheduler.getAlarm() } returns alarm
            coEvery { alarmScheduler.cancel(alarm.id) } returns Unit

            // When
            viewModel.onIntent(AlarmIntent.LoadAlarm)

            // Wait for the coroutine to complete
            advanceUntilIdle()

            viewModel.onIntent(AlarmIntent.OnToggle)

            // Wait for the coroutine to complete
            advanceUntilIdle()

            // Then
            assertEquals(false, viewModel.state.value.isAlarmEnabled)
        }

}