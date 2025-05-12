package com.example.demorobocontrollerapp.voicecontrol

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.DisposableEffect
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

private const val TAG = "VoiceCommandScreen" // tag for logs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceCommandScreen(
    viewModel: VoiceCommandViewModel,
    onBackPressed: () -> Unit
) {
    val recognizedText by viewModel.recognizedText.collectAsState()
    val commandStatus by viewModel.commandStatus.collectAsState()
    val isListening by viewModel.isListening.collectAsState()

    // Context for permission checking
    val context = LocalContext.current

    // Track permission state
    var hasPermission by remember { mutableStateOf(false) }

    // Permission request launcher
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "Microphone permission granted")
            hasPermission = true
        } else {
            Log.d(TAG, "Microphone permission denied")
            hasPermission = false
            viewModel.updateCommandStatus("Microphone permission required")
        }
    }

    // Check and request permission automatically when screen pops up first time
    LaunchedEffect(Unit) {
        val permissionStatus = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        )

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Microphone permission already granted")
            hasPermission = true
        } else {
            Log.d(TAG, "Requesting microphone permission")
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    // Lifecycle observer to properly clean up resources
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    Log.d(TAG, "Screen resumed")
                }
                Lifecycle.Event.ON_PAUSE -> {
                    if (isListening) {
                        Log.d(TAG, "Screen paused while listening, stopping recognition")
                        viewModel.stopListening()
                    }
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voice Commands") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = recognizedText,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = commandStatus,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Voice command button
            IconButton(
                onClick = {
                    Log.d(TAG, "Voice button clicked, current state: $isListening")
                    if (!hasPermission) {
                        Log.d(TAG, "Cannot start listening - no permission")
                        viewModel.updateCommandStatus("Microphone permission required")
                        return@IconButton
                    }

                    if (isListening) {
                        Log.d(TAG, "Stopping listening")
                        viewModel.stopListening()
                    } else {
                        Log.d(TAG, "Starting listening")
                        viewModel.startListening()
                    }
                },
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(if (isListening) Color.Red else Color.Blue)
            ) {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = if (isListening) "Stop Listening" else "Start Listening",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (!hasPermission) {
                    "Microphone permission needed"
                } else if (isListening) {
                    "Listening..."
                } else {
                    "Tap to speak"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}