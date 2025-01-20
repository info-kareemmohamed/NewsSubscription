package com.example.newssubscription.news.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newssubscription.R
import com.example.newssubscription.alarm.presentation.AlarmScreen
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme
import com.example.newssubscription.news.presentation.settings.components.PickImageBottomSheet
import com.example.newssubscription.news.presentation.settings.components.ProfileImage
import com.example.newssubscription.news.presentation.settings.mvi.SettingsIntent
import com.example.newssubscription.news.presentation.settings.mvi.SettingsState
import com.example.newssubscription.news.presentation.settings.mvi.SettingsViewModel
import com.example.newssubscription.payment.presentation.PaymentScreen


@Composable
fun SettingsScreenRoot(
    viewModel: SettingsViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    SettingsScreen(
        state = state.value,
        onIntent = viewModel::onIntent,
        onLogout = onLogout
    )
}


@Composable
private fun SettingsScreen(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
    onLogout: () -> Unit
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showNotificationsBottomSheet by remember { mutableStateOf(false) }
    var showAlarmBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProfileImage(
            imageUrl = state.user?.profilePictureUrl ?: "",
            isLoading = state.imageLoading
        ) {
            showBottomSheet = true
        }
        Spacer(modifier = Modifier.height(5.dp))

        Row(

            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.user?.name ?: "",
                style = TextStyle(
                    color = colorResource(id = R.color.text_title),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            )
            if (state.user?.premium == true)
                Image(
                    modifier = Modifier.size(17.dp),
                    painter = painterResource(id = R.drawable.ic_verification),
                    contentDescription = null
                )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = state.user?.email ?: "",
            style = TextStyle(
                color = colorResource(id = R.color.text_medium),
                fontSize = MaterialTheme.typography.labelMedium.fontSize
            )
        )

        if (showBottomSheet)
            PickImageBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                onImageChange = { byteArray ->
                    onIntent(SettingsIntent.OnImageChange(byteArray = byteArray))
                    showBottomSheet = false
                }

            )


        Spacer(modifier = Modifier.height(50.dp))


        SettingOptionItem(
            iconId = R.drawable.ic_alarm_clock,
            title = "Alarm",
        ) {
            if (state.user?.premium == true) showAlarmBottomSheet = true
            else showPaymentDialog = true
        }
        Spacer(modifier = Modifier.height(20.dp))

        SettingOptionItem(
            iconId = R.drawable.ic_notification,
            title = "Notifications",
        ) {
            showNotificationsBottomSheet = true
        }
        Spacer(modifier = Modifier.height(20.dp))

        SettingOptionItem(
            iconId = R.drawable.ic_payment,
            title = "Payment",
        ) {
            showPaymentDialog = true
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onIntent(SettingsIntent.OnLogoutClick)
                    onLogout()
                }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xffF1F3F4), CircleShape),

                ) {


                Image(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.Center),

                    )

            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Logout",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = colorResource(id = R.color.text_title)
                ),
            )

            if (showAlarmBottomSheet) {
                AlarmScreen {
                    showAlarmBottomSheet = false
                }
            }

            if (showPaymentDialog)
                PaymentScreen(
                    onDismiss = { showPaymentDialog = false },
                    premium = state.user?.premium == true
                )
        }
    }
}

@Composable
fun SettingOptionItem(
    modifier: Modifier = Modifier,
    iconId: Int,
    title: String,
    onOptionClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOptionClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color(0xffF1F3F4), CircleShape),

            ) {


            Image(
                painter = painterResource(id = iconId),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.Center),

                )

        }
        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = colorResource(id = R.color.text_title)
            ),
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            tint = colorResource(id = R.color.body)
        )


    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    NewsSubscriptionTheme {
        SettingsScreen(
            state = SettingsState(),
            onIntent = {},
            onLogout = {}
        )
    }
}

