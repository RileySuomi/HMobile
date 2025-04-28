package com.example.demorobocontrollerapp.loading

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demorobocontrollerapp.ui.theme.DemoRoboControllerAppTheme
import kotlinx.coroutines.delay
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.material3.MaterialTheme
import com.example.demorobocontrollerapp.R

//use as 'preview'
@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    DemoRoboControllerAppTheme {
        DisplayLoading(onTimeout = {}) // pass in the 'viewModel' class
    }
}

@Composable
fun DisplayLoading(onTimeout: () -> Unit) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Bounce Animation
    val bounceAnim = remember { Animatable(0f) }

    // Fade-in Animation
    val fadeInAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(3500) // Delay before transition
        onTimeout()
    }

    // Bounce effect first
    LaunchedEffect(Unit) {
        bounceAnim.animateTo(
            targetValue = -100f, // Move up
            animationSpec = tween(durationMillis = 600, easing = EaseOutQuart)
        )
        bounceAnim.animateTo(
            targetValue = 0f, // Move back down
            animationSpec = tween(durationMillis = 800, easing = EaseInOutQuad)
        )
        fadeInAnim.animateTo(1f, animationSpec = tween(1000)) // Fade-in effect
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Animated Icon
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Animated Icon",
                modifier = Modifier
                    .size(100.dp)
                    .offset(y = bounceAnim.value.dp)
                    .clip(CircleShape)
            )

            // "HMobile" Text with Fade-In effect
            Text(
                text = "HMobile",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .alpha(fadeInAnim.value) // Fade-in effect
                    .padding(top = 10.dp)
            )
        }

        // "by RoboRangers" stays at the bottom
        Text(
            text = "by RoboRangers",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
        )
    }
}
