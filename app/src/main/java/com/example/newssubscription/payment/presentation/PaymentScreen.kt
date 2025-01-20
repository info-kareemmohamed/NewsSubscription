package com.example.newssubscription.payment.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newssubscription.BuildConfig.Public_Key
import com.example.newssubscription.payment.presentation.components.InfoSubscription
import com.example.newssubscription.payment.presentation.components.PremiumUpgradeDialog
import com.example.newssubscription.payment.presentation.mvvm.PaymentViewModel
import com.example.newssubscription.payment.presentation.util.asUiText
import com.paymob.paymob_sdk.PaymobSdk
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    premium: Boolean
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current


    if (premium) {
        val formattedStartDate = formatDate(state.value.user?.startPremiumDate)
        val formattedEndDate = formatDate(state.value.user?.expirationPremiumDate)

        InfoSubscription(
            startDate = formattedStartDate,
            endDate = formattedEndDate,
            months = state.value.user?.subscriptionMonths ?: 0
        ) { onDismiss() }

    } else {
        val clientSecret by viewModel.clientSecret.collectAsState(initial = "")
        val errorMessage by viewModel.errorMessage.collectAsState(initial = null)

        LaunchedEffect(clientSecret) {
            if (clientSecret.isNotEmpty()) {
                startPaymentSdk(context, clientSecret, viewModel)
            }
        }

        LaunchedEffect(errorMessage) {
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage?.asUiText(context), Toast.LENGTH_LONG).show()
            }
        }

        LaunchedEffect(viewModel.finishPayment) {
            viewModel.finishPayment.collect { if (it) onDismiss() }
        }

        PremiumUpgradeDialog(onDismiss = onDismiss, isLoading = state.value.isLoading) { plan ->
            viewModel.startPayment(
                amount = plan.price,
                subscriptionMonths = plan.subscriptionMonths
            )
        }
    }
}

private fun formatDate(date: Date?): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return date?.let { dateFormat.format(it) } ?: "N/A"
}

private fun startPaymentSdk(context: Context, clientSecret: String, viewModel: PaymentViewModel) {
    PaymobSdk.Builder(
        context = context,
        clientSecret = clientSecret,
        publicKey = Public_Key,
        paymobSdkListener = viewModel
    ).build().start()
}