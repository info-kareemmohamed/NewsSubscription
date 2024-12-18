package com.example.newssubscription.authentication.presentation.forgot_password

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newssubscription.R
import com.example.newssubscription.authentication.presentation.common.ButtonAuthentication
import com.example.newssubscription.authentication.presentation.common.ErrorText
import com.example.newssubscription.app.ui.Dimens.ExtraSmallPadding_6
import com.example.newssubscription.app.ui.Dimens.MediumPadding_20
import com.example.newssubscription.app.ui.Dimens.MediumPadding_24
import com.example.newssubscription.app.ui.theme.DarkRed
import com.example.newssubscription.app.ui.theme.LightRed
import com.example.newssubscription.app.ui.theme.NewsSubscriptionTheme


@Composable
fun OTPVerificationScreen(
    otpCode: String,
    length: Int = 4,
    onBackClick: () -> Unit,
    onVerifyClick: () -> Unit
) {
    var otpValue by remember { mutableStateOf("") }
    var isOtpValid by remember { mutableStateOf(true) }
    LaunchedEffect(otpValue) {
        isOtpValid = if (otpCode.equals(otpValue)) true
        else false

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MediumPadding_24),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "OTP Verification",
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            color = colorResource(id = R.color.text_medium)
        )
        Spacer(modifier = Modifier.height(ExtraSmallPadding_6))
        Text(
            text = "Enter the OTP sent to your email",
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.text_medium)
        )
        Spacer(modifier = Modifier.height(MediumPadding_20))

        OtpInputFields(
            length = length,
            isOtpValid = isOtpValid,
            otpValue = otpValue,
            onOtpValueChange = { otpValue = it }
        )

        if (!isOtpValid) ErrorText(error = "Invalid OTP")

        Spacer(modifier = Modifier.weight(1f))
        ButtonAuthentication(
            text = "Verify",
            onClick = {}
        )


    }

}

@Composable
fun OtpInputFields(
    otpValue: String,
    length: Int = 4,
    isOtpValid: Boolean = true,
    onOtpValueChange: (String) -> Unit
) {


    BasicTextField(
        value = otpValue,
        onValueChange = {
            if (it.length <= length) {
                onOtpValueChange(it)
            }
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        maxLines = 1
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(length) { index ->
                val char = when {
                    index >= otpValue.length -> ""
                    else -> otpValue[index].toString()
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.White)
                        .background(if (isOtpValid) Color.Transparent else LightRed.copy(alpha = 0.1f))
                        .size(65.dp)
                        .oTPBar(if (isOtpValid) Color.Black else DarkRed),

                    ) {
                    Text(
                        text = char,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                        ),
                        fontSize = 30.sp
                    )
                }
            }
        }
    }


}

fun Modifier.oTPBar(color: Color = Color.Black): Modifier = composed {
    border(
        width = 1.dp,
        color = color,
        shape = MaterialTheme.shapes.small
    )
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun OTPVerificationScreenPreview() {
    NewsSubscriptionTheme {
        OTPVerificationScreen(otpCode = "", onVerifyClick = {}, onBackClick = {})
    }
}