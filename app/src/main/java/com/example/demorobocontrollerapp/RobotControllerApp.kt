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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demorobocontrollerapp.ui.theme.DemoRoboControllerAppTheme

// General setting
const val TextColor = 0xFF212529 // dark gray // OxFF000000
// const val OffButtonColor = 0xFF929292 // dark-ish gray to signify 'power off'

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

// Positioning Setting (arms)
val extendBtnColor = 0xFFFFE0B2
val retractBtnColor = 0xFFFFCC80
const val PosButtonMaxWidth = 0.2f

//use as 'preview'
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemoRoboControllerAppTheme {
        DisplayApp(viewModel = RobotControllerViewModel())
        //DisplayApp(viewModel = RobotControllerViewModel(), onSettingPressed = {}) // pass in the 'viewModel' class
    }
}

@Composable // The whole app display
fun DisplayApp(viewModel: RobotControllerViewModel) {
    // fun DisplayApp(viewModel: RobotControllerViewModel, onSettingPressed: () -> Unit) {
    val configuration = LocalConfiguration.current // check view mode
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold (
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text(text = "Home", fontSize = 22.sp) },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color.Black,
//                    titleContentColor = Color.White,
//                    actionIconContentColor = Color.White
//                ),
//                 actions = {
//                    IconButton(onClick = {onSettingPressed()}) {
//                        Icon(
//                            imageVector = Icons.Filled.Settings,
//                            contentDescription = "Localized description"
//                        )
//                    }
//                }
//            )
//        },

        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it) // use the whole screen size
            ) {
                if (isLandscape) { //'Landscape' view design section
                    // Monitor sections
                    Column(
                        modifier = Modifier
                            .fillMaxWidth() // use allocated space as much as possible
                            .weight(0.4f) // take portion of the space vertically - increase/decrease as needed
                    ){
                        ShowMonitor(viewModel.displayText.value) // .value gets it as string
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
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column(modifier = Modifier
                                    .padding(3.dp)
                                ) {
                                    Grab(viewModel, isLandscape)
                                }
                                Column(modifier = Modifier
                                    .padding(3.dp)
                                ){
                                    Lift(viewModel, isLandscape)
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Power(viewModel, isLandscape)
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column(modifier = Modifier
                                    .padding(3.dp)
                                ) {
                                    Release(viewModel, isLandscape)
                                }
                                Column(modifier = Modifier
                                    .padding(3.dp)
                                ){
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
                                modifier = Modifier
                                    .fillMaxWidth(),
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
                            .weight(0.2f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Power(viewModel,isLandscape)
                    }

                    // Manipulation section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f),
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
                            .weight(0.3f),
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
                            .weight(0.6f)
                            .padding(8.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally)
                        {
                            Forward(viewModel, isLandscape)
                        }

                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f)
                        ){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Left(viewModel, isLandscape)
                                Spacer(modifier = Modifier.weight(0.7f))
                                Right(viewModel, isLandscape)
                            }
                        }

                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.6f)
                            .padding(8.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally)
                        {
                            Backward(viewModel, isLandscape)
                        }
                        
                        // Positioning ('Extend' and 'Retract')
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                        ){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Extend(viewModel, isLandscape)
                                Spacer(modifier = Modifier.weight(0.2f))
                                Retract(viewModel, isLandscape)
                            }
                        }
                    }
                }
            }
        }
    )
}

// Power button to turn on/off connection
@Composable
fun Power(viewModel: RobotControllerViewModel, isLandscape: Boolean) {
    // Access the current context (Activity or Context)
    val context = LocalContext.current
    Button(
        onClick = {
            viewModel.switchPowerStatus()  // Toggle power status

            // Toggle the text based on the power status
            viewModel.setDisplayText(
                if (viewModel.isPowerOn.value) "Let's lift with ease!" else "Rest mode!"
            )

            // Connect to WebSocket when power is turned on
            if (viewModel.isPowerOn.value) {
                viewModel.webSocketManager.connect(context)  // Call the connect method when power is ON
            } else {
                viewModel.webSocketManager.disconnect()  // Optionally, close the connection when power is OFF
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(if (viewModel.isPowerOn.value) 0xFFFF5733 else 0xFF4CAF50), // Green if on, red if off
            contentColor = Color(TextColor)
        ),
        modifier = Modifier
            .clip(CircleShape) // Make the button circular
            .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth - 55.dp)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight - 2.dp)
    ) {
        Text(
            if (viewModel.isPowerOn.value) "Off" else "On",
            fontSize = ManipElevFontSize,
            fontWeight = FontWeight.Bold
        )
        Icon(Icons.Default.PlayArrow, contentDescription = "On and Off button")
    }
}

// TODO : This will be camera input, change it to do so ASAP
// Temporarily a monitor: display text/action that's taking place/happening
@Composable
fun ShowMonitor(displayText: String){ //
    Column(
        // background
        modifier = Modifier
            .fillMaxSize() // makes the column take up the full available space
            .background(Color(MonitorBgColor)),
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
fun Grab(viewModel: RobotControllerViewModel , isLandscape: Boolean) {
        GlowingButton(
            enabled = viewModel.isPowerOn.value,
            text = "Grab",
            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Grab") },
            btnColor = Color(ManipBtnColor),
            textColor = Color(TextColor),
            fontSize = ManipElevFontSize,
            onPress = {
                if(viewModel.isPowerOn.value) {
                    viewModel.webSocketManager.sendMessage("Grab")
                }
            },
            onRelease = {
                viewModel.setDisplayText("")
            },
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(ManipBtnColor)) // Set the button's background color
                .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth + 10.dp)
                .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp),

            )

    // TODO: Double check 'landscape' view
//    Button(
//        onClick = { viewModel.webSocketManager.sendMessage("Grab");viewModel.setDisplayText("Grabbing item...") },
//        enabled = !viewModel.isPowerOn.value,
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color(ManipBtnColor), // button background color (soft green)
//            contentColor = Color(TextColor)
//        ),
//        modifier = Modifier
//            .clip(CircleShape) // make the button circular
//            .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth + 10.dp)
//            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
//    ) {
//        Text("Grab ",fontSize = ManipElevFontSize, fontWeight = FontWeight.Bold)
//        Icon(Icons.Default.AddCircle, contentDescription = "Grab")
//    }
}

@Composable
fun Release(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text ="Release" ,
        icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Release") },
        btnColor = Color(ManipBtnColor) ,
        textColor = Color(TextColor),
        fontSize = ManipElevFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.webSocketManager.sendMessage("Release")
                viewModel.setDisplayText("Releasing item...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(ManipBtnColor)) // Set the button's background color
            .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth + 10.dp)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
    )
    // TODO: Setup 'landscape' view
//    Button(
//        onClick = { viewModel.webSocketManager.sendMessage("Release");viewModel.setDisplayText("Releasing Item...") },
//        enabled = !viewModel.isPowerOn.value,
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color(ManipBtnColor), // button background color (soft green)
//            contentColor = Color(TextColor)
//        ), modifier = Modifier
//            .clip(CircleShape) // make the button circular
//            .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth + 10.dp)
//            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
//    ) {
//        Text("Release ",fontSize = ManipElevFontSize, fontWeight = FontWeight.Bold)
//        Icon(Icons.Default.CheckCircle, contentDescription = "Release")
//    }
}

// Elevation: consist of 'Lift' & 'Lower' buttons 
@Composable
fun Lift(viewModel: RobotControllerViewModel, isLandscape: Boolean) { // 'Lift' btn
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text ="Lift" ,
        icon = { Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Lift") },
        btnColor = Color(ElevBtnColor) ,
        textColor = Color(TextColor),
        fontSize = ManipElevFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.webSocketManager.sendMessage("Lift")
                viewModel.setDisplayText("Lifting item...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(ElevBtnColor)) // Set the button's background color
            .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth + 10.dp)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
    )
    // TODO: Setup 'landscape' view
//    Button(
//        onClick = {viewModel.webSocketManager.sendMessage("Lift"); viewModel.setDisplayText("Lifting Item...")},
//        enabled = !viewModel.isPowerOn.value,
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color(ElevBtnColor), // button background color (purple)
//            contentColor = Color(TextColor) // text color (white)
//        ), modifier = Modifier
//            .clip(CircleShape) // make the button circular
//            .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth + 10.dp)
//            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
//    ) {
//        Text("Lift ",fontSize = ManipElevFontSize, fontWeight = FontWeight.Bold)
//        Icon(
//            Icons.Default.KeyboardArrowUp,
//            contentDescription = "Lift"
//        )
//    }
}

@Composable
fun Lower(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text ="Lower" ,
        icon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Lower") },
        btnColor = Color(ElevBtnColor) ,
        textColor = Color(TextColor),
        fontSize = ManipElevFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.webSocketManager.sendMessage("Lower")
                viewModel.setDisplayText("Lowering item...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(ElevBtnColor)) // Set the button's background color
            .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth + 10.dp)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
    )
//    Button(
//        onClick = {viewModel.webSocketManager.sendMessage("Lower");viewModel.setDisplayText("Lowering Item...") },
//        enabled = !viewModel.isPowerOn.value,
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color(ElevBtnColor), // button background color (soft green)
//            contentColor = Color(TextColor)
//        ), modifier = Modifier
//            .clip(CircleShape) // Make the button circular
//            .width(if (isLandscape) ManipElevButtonWidth else ManipElevButtonWidth + 10.dp)
//            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
//    ) {
//        Text("Lower ",fontSize = ManipElevFontSize, fontWeight = FontWeight.Bold)
//        Icon(
//            Icons.Default.KeyboardArrowDown,
//            contentDescription = "Lower"
//        )
//    }
}

// Navigation: consists of 'Forward' 'Backward' 'Left' 'Right'
@Composable
fun Forward(viewModel: RobotControllerViewModel,isLandscape : Boolean) {
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text ="Forward ↑" ,
        icon = { /* in any */  },
        btnColor = Color(NavBtnColor) ,
        textColor = Color(TextColor),
        fontSize = NavFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.webSocketManager.sendMessage("Forward")
                viewModel.setDisplayText("Moving Forward...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(NavBtnColor)) // Set the button's background color
            .fillMaxWidth(if (isLandscape) NavButtonMaxWidth else NavButtonMaxWidth + NavButtonMaxWidth)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
            .width(ManipElevButtonWidth)
    )
    // TODO: Double check 'landscape' view mod

//        Button(
//            onClick = {viewModel.webSocketManager.sendMessage("Forward"); viewModel.setDisplayText( "Moving Forward...") },
//            enabled = !viewModel.isPowerOn.value,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(NavBtnColor),
//                contentColor = Color(TextColor)
//            ),
//            modifier = Modifier
//                .fillMaxWidth(if (isLandscape) NavButtonMaxWidth else NavButtonMaxWidth + NavButtonMaxWidth) // Take 20% of the screen width for landscape view
//                .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
//                .width(ManipElevButtonWidth)
//        ) {
//            Text("Forward ↑",
//                fontSize = NavFontSize,
//                fontWeight = FontWeight.Bold)
//        }
}

@Composable
fun Backward(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text ="Backward ↓" ,
        icon = { /* in any */  },
        btnColor = Color(NavBtnColor) ,
        textColor = Color(TextColor),
        fontSize = NavFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.webSocketManager.sendMessage("Backward")
                viewModel.setDisplayText("Moving Backward...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(NavBtnColor)) // Set the button's background color
            .fillMaxWidth(if (isLandscape) NavButtonMaxWidth else NavButtonMaxWidth + NavButtonMaxWidth)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
            .width(ManipElevButtonWidth)
    )
    // TODO: Double check 'landscape' view mode

//        Button(
//            onClick = { viewModel.webSocketManager.sendMessage("Move Backward"); viewModel.setDisplayText("Moving Backward...")},
//            enabled = !viewModel.isPowerOn.value,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(NavBtnColor),
//                contentColor = Color(TextColor)
//            ),
//            modifier = Modifier
//                .fillMaxWidth(if (isLandscape) NavButtonMaxWidth else NavButtonMaxWidth + NavButtonMaxWidth)
//                .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
//                .width(ManipElevButtonWidth)
//        ) {
//            Text("Backward ↓",
//                fontSize = NavFontSize,
//                fontWeight = FontWeight.Bold)
//        }
}

@Composable
fun Left(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text ="← Left" ,
        icon = { /* in any */  },
        btnColor = Color(NavBtnColor) ,
        textColor = Color(TextColor),
        fontSize = NavFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.webSocketManager.sendMessage("Left")
                viewModel.setDisplayText("Moving Left...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(NavBtnColor)) // Set the button's background color
            .fillMaxWidth(if (isLandscape) NavButtonMaxWidth else NavButtonMaxWidth + 0.2f)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
            .width(ManipElevButtonWidth)
    )
    // TODO: Double check 'landscape' view mode

//        Button(
//            onClick = {viewModel.webSocketManager.sendMessage("Move Left"); viewModel.setDisplayText("Moving Left...") },
//            enabled = !viewModel.isPowerOn.value,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(NavBtnColor),
//                contentColor = Color(TextColor)
//            ),
//            modifier = Modifier
//                .fillMaxWidth(if (isLandscape) NavButtonMaxWidth else NavButtonMaxWidth + NavButtonMaxWidth)
//                .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
//                .width(ManipElevButtonWidth)
//        ) {
//            Text("← Left",
//                fontSize = NavFontSize,
//                fontWeight = FontWeight.Bold)
//        }
}

@Composable
fun Right(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text ="Right →" ,
        icon = { /* in any */  },
        btnColor = Color(NavBtnColor) ,
        textColor = Color(TextColor),
        fontSize = NavFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.webSocketManager.sendMessage("Right")
                viewModel.setDisplayText("Moving Right...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(NavBtnColor)) // Set the button's background color
            .fillMaxWidth(if (isLandscape) NavButtonMaxWidth else NavButtonMaxWidth + 0.4f)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
            .width(ManipElevButtonWidth)
    )
    // TODO: Double check 'landscape' view mode

//        Button(
//            onClick = {viewModel.webSocketManager.sendMessage("Move Right"); viewModel.setDisplayText("Moving Right...") },
//            enabled = !viewModel.isPowerOn.value,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(NavBtnColor),
//                contentColor = Color(TextColor)
//            ),
//            modifier = Modifier
//                .fillMaxWidth(if (isLandscape) NavButtonMaxWidth else NavButtonMaxWidth + 0.4f)
//                .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
//                .width(ManipElevButtonWidth)
//        ) {
//            Text("Right →",
//                fontSize = NavFontSize,
//                fontWeight = FontWeight.Bold)
//        }
}


// (Arms) Positioning
@Composable
fun Extend(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text ="Extend" ,
        icon = { /* in any */  },
        btnColor = Color(extendBtnColor) ,
        textColor = Color(TextColor),
        fontSize = NavFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.webSocketManager.sendMessage("Extend")
                viewModel.setDisplayText("Extending...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(NavBtnColor)) // Set the button's background color
            .fillMaxWidth(if (isLandscape) PosButtonMaxWidth else PosButtonMaxWidth + 0.23f)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
            .width(ManipElevButtonWidth)
    )
}

@Composable
fun Retract(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text ="Retract" ,
        icon = { /* in any */  },
        btnColor = Color(retractBtnColor) ,
        textColor = Color(TextColor),
        fontSize = NavFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.webSocketManager.sendMessage("Retract")
                viewModel.setDisplayText("Retracting item...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(NavBtnColor)) // Set the button's background color
            .fillMaxWidth(if (isLandscape) PosButtonMaxWidth else PosButtonMaxWidth + 0.5f)
            .height(if (isLandscape) ManipElevButtonHeight else ManipElevButtonHeight + 10.dp)
            .width(ManipElevButtonWidth)
    )
}

// TODO: shortcut to turn on app when multiple taps detected

//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false},
//            confirmButton = {},
//            title = { Text("Action in Progress") },
//            text = { Text("Button Disabled") } // This is where the message is shown
//        )
//
//        // Automatically dismiss the dialog after 3 seconds
//        LaunchedEffect(Unit) {
//            delay(1000) // Wait for 3 seconds
//            showDialog = false // Dismiss the dialog
//        }
//    }