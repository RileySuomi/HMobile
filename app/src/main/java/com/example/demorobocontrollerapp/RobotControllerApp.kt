package com.example.demorobocontrollerapp

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.demorobocontrollerapp.ui.theme.DemoRoboControllerAppTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration

// General setting
const val TextColor = 0xFF212529 // dark gray // OxFF000000

// Monitor setting
val MonitorFontSize = 32.sp
const val MonitorBgColor = 0xFF212121 //-> dark gray
const val MonitorTextColor = 0xFFF8F8F8 // off-white

// Manipulation & Elevation setting
const val ManipBtnColor = 0xFF007BFF// 0xFF3498DB  // sky blue
const val ElevBtnColor = 0xFF1ABC9C // soft green
val ManipElevFontSize = 21.sp // readability
val ManipElevButtonWidth = 160.dp
val ManipElevButtonHeight = 50.dp

// Navigation setting
val NavFontSize = 21.sp // 'nav' = 'navigation'
const val NavBtnColor = 0xFFD3D3D3 // light gray
const val NavButtonMaxWidth = 0.2f

//use as 'preview'
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemoRoboControllerAppTheme {
        DisplayApp(viewModel = RobotControllerViewModel()) // pass in the viewModel class
    }
}

@Composable // The whole app display
fun DisplayApp(viewModel: RobotControllerViewModel) {
    val configuration = LocalConfiguration.current // check view mode
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    Column(
        modifier = Modifier.fillMaxSize() // use the whole screen size
    ) {
        if (isLandscape) { //'Landscape' view design section
            //Monitor sections
            Column(
                modifier = Modifier
                    .fillMaxWidth() // use allocated space as much as possible
                    .weight(0.4f) // take portion of the space vertically - increase/decrease as needed
            ){
                ShowMonitor(viewModel.displayText.value) // .value makes it a string
            }

            // Manipulation, Navigation,  Elevation column
            Column(
                modifier = Modifier
                    .fillMaxWidth() // use allocated space as much as possible
                    .weight(1.4f) // take portion of the space vertically - increase/decrease as needed
                    .padding(2.dp)
            ){

                // Manipulation ('Grab' & 'Release' buttons) & Elevation ('Lift' & 'Lower' buttons)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.SpaceAround // Updated to SpaceAround
                ) {
                    Column(
                        modifier = Modifier.weight(1f).padding(2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(modifier = Modifier.padding(3.dp)){
                            Grab(viewModel, isLandscape)
                        }
                        Column(modifier = Modifier.padding(3.dp)){
                            Lift(viewModel, isLandscape)
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Power(viewModel, isLandscape)
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(modifier = Modifier.padding(3.dp)){
                            Release(viewModel, isLandscape)
                        }
                        Column(modifier = Modifier.padding(3.dp)){
                            Lower(viewModel, isLandscape)
                        }
                    }
                }

                //Navigation ('Forward','Backward','Left','Right' buttons) section
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Forward(viewModel, isLandscape)
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                        ){
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Left(viewModel, isLandscape)
                                Right(viewModel, isLandscape)
                            }
                        }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Backward(viewModel, isLandscape)
                }
            }
        } else { //'Portrait' view design section
            // Monitor section
            Column(
                modifier = Modifier
                    .fillMaxWidth() // use allocated space as much as possible
                    .weight(0.7f) // take portion of the space vertically - increase/decrease as needed
            ) {
                Box(contentAlignment = Alignment.Center) {
                    ShowMonitor(viewModel.displayText.value) // .value makes it a string
                }
            }

            // Power button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Power(viewModel,isLandscape)
            }

            // Manipulation section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Grab(viewModel, isLandscape)
                    Spacer(modifier = Modifier.width(32.dp)) // add spacing between buttons
                    Release(viewModel, isLandscape)
                }
            }

            // Elevation section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Lift(viewModel, isLandscape)
                    Spacer(modifier = Modifier.width(32.dp)) // add spacing between buttons
                    Lower(viewModel, isLandscape)
                }
            }

            // Navigation section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .padding(8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Forward(viewModel, isLandscape)
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Left(viewModel, isLandscape)
                        Spacer(modifier = Modifier.weight(1f))
                        Right(viewModel, isLandscape)
                    }
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .padding(8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Backward(viewModel, isLandscape)
                }

            }
        }
    }
}

// Power button to turn on/off connection
@Composable // TODO: make toogle/switch work
fun Power(displayText: RobotControllerViewModel, isLandscape: Boolean) {
    Button(
        onClick = {
            displayText.switchPowerStatus()  // Toggle power status
            displayText.setDisplayText(
                if (displayText.isPowerOn) "Let's lift with ease!" else "Rest mode!"
            )
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(if (displayText.isPowerOn) 0xFF4CAF50 else 0xFFFF5733), // green if on, red if off
            contentColor = Color(TextColor) //
        ),
        modifier = Modifier
            .clip(CircleShape) // Make the button circular
            .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth - 55.dp)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight - 2.dp)
    ) {
        // Display button text and icon
        Text(
            if (displayText.isPowerOn) "On" else "Off",
            fontSize = ManipElevFontSize,
            fontWeight = FontWeight.Bold
        )
        Icon(Icons.Default.PlayArrow, contentDescription = "On and Off button")
    }
}

// Monitor: display text/action that's taking place/happening
@Composable
fun ShowMonitor(displayText: String){ //
    Column(
        // background
        modifier = Modifier
            .fillMaxSize()
            .background(Color(MonitorBgColor)), // makes the column take up the full available space
        verticalArrangement = Arrangement.SpaceEvenly, // evenly spaces the header, main, and footer
        horizontalAlignment = Alignment.CenterHorizontally // centers horizontally (optional)
    ) {
        // Monitor
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // assign how much space vertically
                .height(100.dp) // sets the height of the Box to 200dp
                .padding(16.dp) // adds padding around the Box (inside space)
                .border(2.dp, Color(MonitorBgColor)) // to simulate a monitor border
                .background(Color(MonitorBgColor)),
            contentAlignment = Alignment.Center // center content
        ) {
            Text(displayText, fontSize = MonitorFontSize ,
                color = Color(MonitorTextColor),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center) // center text horizontally & vertically
            )
        }
    }
}

// Manipulation: consist of 'Grab' & 'Release' btn
@Composable
fun Grab(displayText: RobotControllerViewModel , isLandscape: Boolean) { // 'Grab'
    Button(
        onClick = { displayText.setDisplayText( "Grabbing Item..." )},
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(ManipBtnColor), // button background color (soft green)
            contentColor = Color(TextColor)
        ),
        modifier = Modifier
            .clip(CircleShape) // make the button circular
            .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth + 10.dp)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
    ) {
        Text("Grab ",fontSize = ManipElevFontSize, fontWeight = FontWeight.Bold)
        Icon(Icons.Default.AddCircle, contentDescription = "Grab")
    }
}

@Composable
fun Release(displayText: RobotControllerViewModel, isLandscape: Boolean){
    Button(
        onClick = { displayText.setDisplayText("Releasing Item...") },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(ManipBtnColor), // button background color (soft green)
            contentColor = Color(TextColor)
        ), modifier = Modifier
            .clip(CircleShape) // make the button circular
            .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth + 10.dp)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
    ) {
        Text("Release ",fontSize = ManipElevFontSize, fontWeight = FontWeight.Bold)
        Icon(Icons.Default.CheckCircle, contentDescription = "Release")
    }
}

// Elevation: consist of 'Lift' & 'Lower' buttons
@Composable
fun Lift(displayText: RobotControllerViewModel, isLandscape: Boolean) { // 'Lift' btn
    Button(
        onClick = {displayText.setDisplayText("Lifting Item...")},
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(ElevBtnColor), // button background color (purple)
            contentColor = Color(TextColor) // text color (white)
        ), modifier = Modifier
            .clip(CircleShape) // make the button circular
            .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth + 10.dp)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
    ) {
        Text("Lift ",fontSize = ManipElevFontSize, fontWeight = FontWeight.Bold)
        Icon(
            Icons.Default.KeyboardArrowUp,
            contentDescription = "Lift"
        )
    }
}

@Composable
fun Lower(displayText: RobotControllerViewModel, isLandscape: Boolean){
    Button(
        onClick = {displayText.setDisplayText("Lowering Item...") },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(ElevBtnColor), // button background color (soft green)
            contentColor = Color(TextColor)
        ), modifier = Modifier
            .clip(CircleShape) // Make the button circular
            .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth + 10.dp)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
    ) {
        Text("Lower ",fontSize = ManipElevFontSize, fontWeight = FontWeight.Bold)
        Icon(
            Icons.Default.KeyboardArrowDown,
            contentDescription = "Lower"
        )
    }
}

// Navigation: consists of 'Forward' 'Backward' 'Left' 'Right'
@Composable
fun Forward(displayText: RobotControllerViewModel,isLandscape : Boolean) {
        Button(
            onClick = { displayText.setDisplayText( "Moving Forward...") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(NavBtnColor),
                contentColor = Color(TextColor)
            ),
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) NavButtonMaxWidth else NavButtonMaxWidth + NavButtonMaxWidth) // Take 20% of the screen width for landscape view
                .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
                .width(ManipElevButtonWidth)
        ) {
            Text("Forward ↑",
                fontSize = NavFontSize,
                fontWeight = FontWeight.Bold)
        }
}

@Composable
fun Backward(displayText: RobotControllerViewModel, isLandscape: Boolean){
        Button(
            onClick = {
                displayText.setDisplayText("Moving Backward...")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(NavBtnColor),
                contentColor = Color(TextColor)
            ),
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) NavButtonMaxWidth else NavButtonMaxWidth + NavButtonMaxWidth)
                .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
                .width(ManipElevButtonWidth)
        ) {
            Text("Backward ↓",
                fontSize = NavFontSize,
                fontWeight = FontWeight.Bold)
        }
}

@Composable
fun Left(displayText: RobotControllerViewModel, isLandscape: Boolean){
        Button(
            onClick = {
                displayText.setDisplayText("Moving Left...")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(NavBtnColor),
                contentColor = Color(TextColor)
            ),
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) NavButtonMaxWidth else NavButtonMaxWidth + NavButtonMaxWidth)
                .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
                .width(ManipElevButtonWidth)
        ) {
            Text("← Left",
                fontSize = NavFontSize,
                fontWeight = FontWeight.Bold)
        }
}

@Composable
fun Right(displayText: RobotControllerViewModel, isLandscape: Boolean){
        Button(
            onClick = {
                displayText.setDisplayText("Moving Right...")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(NavBtnColor),
                contentColor = Color(TextColor)
            ),
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) NavButtonMaxWidth else NavButtonMaxWidth + 0.4f)
                .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
                .width(ManipElevButtonWidth)
        ) {
            Text("Right →",
                fontSize = NavFontSize,
                fontWeight = FontWeight.Bold)
        }
}