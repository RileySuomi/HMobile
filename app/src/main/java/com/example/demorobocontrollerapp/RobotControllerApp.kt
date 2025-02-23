package com.example.demorobocontrollerapp

import android.content.res.Configuration
import android.widget.FrameLayout
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.viewinterop.AndroidView
import com.example.demorobocontrollerapp.ui.theme.DemoRoboControllerAppTheme

// this is mergi check
// General setting
const val TextColor = 0xFFFFFFFF // dark gray // OxFF000000
// const val OffButtonColor = 0xFF929292 // dark-ish gray to signify 'power off'

// Monitor setting
val MonitorFontSize = 32.sp
const val MonitorBgColor = 0xFF212121 //-> dark gray
const val MonitorTextColor = 0xFFF8F8F8 // off-white

// Manipulation & Elevation setting
const val ManipBtnColor = 0xFF022B3A// 0xFF3498DB  // sky blue
const val ElevBtnColor = 0xFFCE7D81 // soft green
val ManipElevFontSize = 21.sp // readability
val ManipElevButtonWidth = 160.dp
val ManipElevButtonHeight = 50.dp

// Navigation setting
val NavFontSize = 21.sp // 'nav' = 'navigation'
const val NavBtnColor = 0xFF1F7A8C // light gray
const val NavButtonMaxWidth = 0.2f

// Positioning Setting (arms)
val extendBtnColor = 0xFFF6A6A1
val retractBtnColor = 0xFFF6A6A1
const val PosButtonMaxWidth = 0.2f

//use as 'preview'
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemoRoboControllerAppTheme {
        //DisplayApp(viewModel = RobotControllerViewModel())
        DisplayApp(viewModel = RobotControllerViewModel(), onSettingPressed = {}) // pass in the 'viewModel' class
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable // The whole app display
//fun DisplayApp(viewModel: RobotControllerViewModel) {
fun DisplayApp(viewModel: RobotControllerViewModel, onSettingPressed: () -> Unit) {
    val configuration = LocalConfiguration.current // check view mode
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Home", fontSize = 22.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                 actions = {
                    IconButton(onClick = {onSettingPressed()}) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },

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
                        ShowMonitor(viewModel) // .value gets it as string
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
                            ShowMonitor(viewModel) // .value makes it a string
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
            viewModel.setDisplayText(
                if (viewModel.isPowerOn.value) "Let's lift with ease!" else "Rest mode!"
            )
//            if (viewModel.isPowerOn.value) { // open connection when power on
//                //viewModel.webSocketManager.connect(context)
//            } else { // close connection when power off
//                //viewModel.webSocketManager.disconnect()
//            }
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

// TODO : This will be camera input, change it to do so once TCP's set up
// Temporarily a monitor: display text/action that's taking place/happening
@Composable
fun ShowMonitor(viewModel: RobotControllerViewModel) {
    val context = LocalContext.current
    val signalingUrl = "wss://10.0.2.2:8765/webrtc"  // Local WebRTC Server URL

    // Create WebRTCClient & EGL Context (Only When Power is ON)
//    val webRTCClient = remember { WebRTCClient(context, signalingUrl) }
//    val eglBase = remember { EglBase.create() }  // Create EGLBase context

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(MonitorBgColor)),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Monitor Box (Camera Stream or Text)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .height(100.dp)
                .padding(16.dp)
                .border(2.dp, Color(MonitorBgColor))
                .background(Color(MonitorBgColor)),
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.isPowerOn.value) {
                // TODO: Show WebRTC Camera Stream when Power is ON
//                AndroidView(
//                    modifier = Modifier.fillMaxSize(),
//                    factory = { context ->
//                        val surfaceViewRenderer = SurfaceViewRenderer(context).apply {
//                            layoutParams = FrameLayout.LayoutParams(
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                            )
//                            init(eglBase.eglBaseContext, null)  // Fix: Use EGL Context
//                            setMirror(true)  // Mirror effect
//                        }
//                        webRTCClient.init(surfaceViewRenderer)  // Attach WebRTC to SurfaceView
//                        surfaceViewRenderer
//                    }
//                )
                // Temporarily until tcp demo is done - Show Text When Power is ON
                Text(
                    text = "<camera live>",
                    fontSize = MonitorFontSize,
                    color = Color(MonitorTextColor),
                    fontWeight = FontWeight.Bold
                )
            } else {
                // Show Text When Power is OFF
                Text(
                    text = "<camera offline>",
                    fontSize = MonitorFontSize,
                    color = Color(MonitorTextColor),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Display Monitor Text Below (For Status Updates)
        Text(
            text = viewModel.displayText.value,
            fontSize = MonitorFontSize,
            color = Color(MonitorTextColor),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
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
                if (viewModel.isPowerOn.value) {
                    // Secure communication with WebSocket
                    viewModel.webSocketManager.sendMessage("Grab")
                    viewModel.setDisplayText("Grabbing item...")
                }
            }
            ,
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