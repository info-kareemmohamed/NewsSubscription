package com.example.newssubscription.payment.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newssubscription.BuildConfig.Public_Key
import com.example.newssubscription.payment.presentation.components.InfoSubscription
import com.example.newssubscription.payment.presentation.components.PremiumUpgradeDialog
import com.example.newssubscription.payment.presentation.mvvm.PaymentViewModel
import com.example.newssubscription.payment.presentation.util.asUiText
import com.paymob.paymob_sdk.PaymobSdk
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.PartySystem
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    premium: Boolean
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showKonfettiView by remember { mutableStateOf(false) }

    if (premium) {
        val formattedStartDate = formatDate(state.value.user?.startPremiumDate)
        val formattedEndDate = formatDate(state.value.user?.expirationPremiumDate)

        InfoSubscription(
            startDate = formattedStartDate,
            endDate = formattedEndDate,
            months = state.value.user?.subscriptionMonths ?: 0
        ) { onDismiss() }


    } else if (!showKonfettiView) {
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

        PremiumUpgradeDialog(onDismiss = onDismiss, isLoading = state.value.isLoading) { plan ->
            viewModel.startPayment(
                amount = plan.price,
                subscriptionMonths = plan.subscriptionMonths
            )
        }

    }

    //Listen to finish payment
    LaunchedEffect(viewModel.finishPayment) {
        viewModel.finishPayment.collect {
            if (it) {
                showKonfettiView = true
            }
        }
    }

    if (showKonfettiView) {
        ShowKonfettiView(onDismiss = onDismiss)
    }

}


@Composable
private fun ShowKonfettiView(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        KonfettiView(
            modifier = Modifier.fillMaxSize(),
            parties = listOf(
                Party(
                    speed = 0f,
                    maxSpeed = 15f,
                    damping = 0.9f,
                    angle = Angle.BOTTOM,
                    spread = Spread.ROUND,
                    colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                    emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(100),
                    position = Position.Relative(0.0, 0.0).between(Position.Relative(1.0, 0.0))
                )
            ),
            updateListener =
            object : OnParticleSystemUpdateListener {
                override fun onParticleSystemEnded(
                    system: PartySystem,
                    activeSystems: Int,
                ) {
                    onDismiss()
                }
            },
        )

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