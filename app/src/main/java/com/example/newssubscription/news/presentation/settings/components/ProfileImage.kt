package com.example.newssubscription.news.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newssubscription.R

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    isLoading: Boolean = false,
    cameraIconId: Int = R.drawable.ic_photo_camera,
    onClick: () -> Unit = {}
) {

    Box(
        modifier = modifier
            .size(148.dp)
            .background(Color(0xffF1F3F4), CircleShape)
            .clickable { if (!isLoading) onClick() }
    ) {


        // Profile Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .align(Alignment.Center)
        )

        // Camera Icon Overlay
        Box(
            modifier = Modifier
                .padding(bottom = 5.dp, end = 3.dp)
                .size(30.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .align(Alignment.BottomEnd)

        ) {
            if (isLoading)
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            else
                Icon(
                    painter = painterResource(id = cameraIconId),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(15.dp)
                        .align(Alignment.Center)
                )
        }
    }
}
