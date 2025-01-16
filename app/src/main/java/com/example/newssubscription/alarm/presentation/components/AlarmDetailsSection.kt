package com.example.newssubscription.alarm.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDetailsSection(
    selectedDay: Int,
    onDaySelect: (Int) -> Unit,
    timePickerState: TimePickerState,
    onSetClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.alarm_will_ring_weekly),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            ),
            color = colorResource(id = R.color.text_title)
        )

        DaysList(
            selectedDay = selectedDay,
            onDayClick = onDaySelect
        )

        TimePicker(
            state = timePickerState,
            colors = TimePickerDefaults.colors(
                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                timeSelectorSelectedContentColor = Color.White,
                periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
            )
        )

        Button(
            onClick = onSetClick,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(text = stringResource(id = R.string.set_alarm), color = Color.White)
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)

fun PreviewAlarmDetailsSection() {
    NewsSubscriptionTheme {
        AlarmDetailsSection(
            selectedDay = 0,
            onDaySelect = {},
            timePickerState = TimePickerState(12, 0, true),
            onSetClick = {}
        )
    }
}
