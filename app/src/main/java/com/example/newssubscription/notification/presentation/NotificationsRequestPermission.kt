package com.example.newssubscription.notification.presentation

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

@Composable
fun NotificationsRequestPermission(
    onDismissRequest: () -> Unit
) {
    // State to control whether the permission dialog is shown
    var showPermissionDialog by remember { mutableStateOf(false) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) or higher

        val context = LocalContext.current
        val activity = context as Activity

        // Check if the notification permission has already been granted
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        // Launcher to handle the result of the permission request
        val permissionResultActivityLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                // Show the permission dialog if the permission is not granted
                showPermissionDialog = !isGranted
            }
        )

        when {
            hasPermission -> {
                // Permission is already granted
                showPermissionDialog = true
            }

            shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                // User has previously denied the permission; show a rationale
                showPermissionDialog = true
            }

            else -> {
                // Request permission for the first time
                SideEffect {
                    permissionResultActivityLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    } else {
        // For Android versions lower than 13 (API 33), no explicit permission is needed
        showPermissionDialog = true
    }

    // Show the custom permission dialog if required
    if (showPermissionDialog) {
        PermissionDialog { onDismissRequest() }
    }
}
