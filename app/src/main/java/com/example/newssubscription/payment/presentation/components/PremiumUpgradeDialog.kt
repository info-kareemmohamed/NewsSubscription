package com.example.newssubscription.payment.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.newssubscription.R
import com.example.newssubscription.app.ui.theme.PremiumCardColor


@Composable
fun PremiumUpgradeDialog(
    onDismiss: () -> Unit,
    isLoading: Boolean = false,
    onPlanSelected: (Plan) -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val dialogHeight = screenHeight * 0.8f

    val scrollState = rememberScrollState()
    var loadingPlan by remember { mutableStateOf<Plan?>(null) }


    Dialog(onDismissRequest = onDismiss) {
        Box(modifier = Modifier.height(dialogHeight)) {
            PremiumLoader(
                modifier = Modifier
                    .size(screenHeight * 0.25f)
                    .align(Alignment.TopCenter),
                icon = R.raw.lottie_premium
            )

            Column {
                Spacer(modifier = Modifier.height(screenHeight * 0.19f))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dialogHeight)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(25.dp, 10.dp, 25.dp, 10.dp)
                        )
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Upgrade To Premium",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = colorResource(id = R.color.text_title),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                        )

                        val plans = listOf(
                            Plan(
                                title = "Premium -",
                                price = 7.99f,
                                subscriptionMonths = 1,
                                description = "Billed every month at 7.99/mo"
                            ),
                            Plan(
                                title = "Premium -",
                                price = 40.02f,
                                subscriptionMonths = 6,
                                description = "Billed every 6 months at 6.67/mo"
                            ),
                            Plan(
                                title = "Premium -",
                                price = 69.96f,
                                subscriptionMonths = 12,
                                description = "Billed every year at 5.83/mo"
                            )
                        )

                        plans.forEach { plan ->
                            SubscriptionCard(plan, isLoading && loadingPlan == plan) {
                                loadingPlan = plan
                                onPlanSelected(it)
                            }
                        }

                        val features = listOf(
                            "Get unlimited views – unlike the free plan with only 20 articles per day",
                            "Activate the alarm to schedule article reading, a feature not available in the free version"
                        )

                        features.forEach { feature ->
                            FeatureOption(feature)
                        }

                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.clip(RoundedCornerShape(5.dp))
                        ) {
                            Text(text = "Later", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionCard(plan: Plan, isLoading: Boolean, onCLick: (Plan) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = PremiumCardColor),
        onClick = {
            if (!isLoading) onCLick(plan)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading)
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(30.dp))
            else
                Image(
                    painter = painterResource(id = R.drawable.ic_king),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )

            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "${plan.title} ${plan.subscriptionMonths} Months",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = "${plan.currency} ${plan.price}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = plan.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun FeatureOption(feature: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_check),
            tint = Color.Green.copy(alpha = 0.4f),
            contentDescription = null
        )
        Text(
            text = feature,
            style = MaterialTheme.typography.bodySmall,
            color = colorResource(id = R.color.text_medium),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun PremiumLoader(modifier: Modifier = Modifier, icon: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(icon))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

data class Plan(
    val title: String,
    val price: Float = 0.0f,
    val currency: String = "EGP",
    val subscriptionMonths: Int = 0,
    val description: String
)
