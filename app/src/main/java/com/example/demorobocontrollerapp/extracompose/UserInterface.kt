package com.example.demorobocontrollerapp.extracompose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Logo 'breathing' effect when app's initially opened

// Button 'glow' effect when it's pressed
@Composable
fun GlowingButton(
    enabled: Boolean,
    text: String, // Button text
    icon: @Composable (() -> Unit)? = null, // Optional icon
    btnColor: Color,
    textColor: Color,
    fontSize: TextUnit,
    onPress: () -> Unit,
    onRelease: () -> Unit, // Click event
    modifier: Modifier = Modifier // 🔹 Modifier stays flexible in MainActivity
) {
    // Track button press state
    var isPressed by remember { mutableStateOf(false) }

    //  Darken the btn color
    val glowColor = btnColor.copy(alpha = 0.7f)

    // Track button's actual size dynamically
    val buttonSize by remember { mutableStateOf(IntSize(0, 0)) }

    // Animate button color when pressed
    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) glowColor else btnColor,
        animationSpec = tween(durationMillis = 300)
    )

    // Animate shadow size for the glowing effect
    val shadowSize by animateDpAsState(
        targetValue = if (isPressed) 8.dp else (buttonSize.height * 0.20).dp,
        animationSpec = tween(durationMillis = 300)
    )
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    Box(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.5f) // Reduce transparency when disabled
            .background(backgroundColor, shape = CircleShape) // 🔹 Uses existing color
            .shadow(
                elevation = shadowSize, // Larger shadow for the glowing effect
                shape = CircleShape, // Circular shape for the shadow
                clip = false
            )
            .pointerInput(Unit) { // Detect touch gestures
                if(enabled) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            onPress()
                            job = coroutineScope.launch {
                                while (isPressed) {
                                    delay(100) // Adjust delay for message frequency
                                    onPress()
                                }
                            }// Start glow effect
                            tryAwaitRelease() // Keep effect until release
                            isPressed = false
                            job?.cancel()// Remove glow effect
                            onRelease() // Call actual click event
                        }
                    )
                }
            }
            .padding(16.dp), // Inner padding for content
        contentAlignment = Alignment.Center
    ) {
        // Use Row to align text and icon horizontally
        Row(
            verticalAlignment = Alignment.CenterVertically, // Align vertically center
            horizontalArrangement = Arrangement.Center // Center horizontally
        ) {
            Text(text, fontSize = fontSize, fontWeight = FontWeight.Bold, color = textColor)
            icon?.invoke() // Show icon if provided
        }
    }
}

@Composable
fun CustomButton(
    text: String,
    isEnabled: Boolean,
    padding: PaddingValues = PaddingValues(0.dp),
    color: Color = Color.Blue,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (isEnabled) color else Color.LightGray
    )
    val contentColor by animateColorAsState(
        if (isEnabled) Color.White else Color.Black
    )

    Box(
        Modifier
            .padding(padding)
            .background(backgroundColor, RoundedCornerShape(20))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ){
        Text(
            text,
            Modifier.align(Alignment.Center).padding(5.dp),
            color = contentColor,
            fontSize = 16.sp
        )
    }
}