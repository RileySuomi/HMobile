@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.demorobocontrollerapp.controls

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Rotate90DegreesCcw
import androidx.compose.material.icons.filled.Rotate90DegreesCw
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Switch
import com.example.demorobocontrollerapp.helpers.CustomButton
import com.example.demorobocontrollerapp.data.source.local.settings.prefversion.DataStoreRepo
import com.example.demorobocontrollerapp.helpers.GlowingButton
import com.example.demorobocontrollerapp.data.source.network.unused.WebSocketClient
import com.example.demorobocontrollerapp.ui.theme.DemoRoboControllerAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.roundToInt
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.wear.compose.material.SwitchDefaults
import com.example.demorobocontrollerapp.helpers.CustomInputField
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.rpc.Help

//******* Preset 1 ************
// General setting
val TextColor = Color.White // dark gray // OxFF000000
// const val OffButtonColor = 0xFF929292 // dark-ish gray to signify 'power off'

// Monitor setting
val MonitorFontSize = 32.sp
val MonitorBgColor = Color(0xFF212121) //-> dark gray
val MonitorTextColor = Color(0xFFF8F8F8) // off-white

// Manipulation & Elevation setting
val ManipBtnColor = Color(0xFF022B3A)// 0xFF3498DB  // sky blue
val ElevBtnColor = Color(0xFFCE7D81) // soft green

val ButtonFontSize = 21.sp // readability

val XLargeBtnSize = DpSize(170.dp, 65.dp)
val LargeBtnSize = DpSize(110.dp, 50.dp)
val MediumBtnSize = DpSize(92.dp, 65.dp)
val SmallBtnSize = DpSize(80.dp, 50.dp)

val VertButtonSize = DpSize(90.dp, 85.dp)

val RoundButtonSize = DpSize(50.dp, 50.dp)

// Navigation setting
val NavBtnColor = Color(0xFF1F7A8C) // light gray

// Positioning Setting (arms)
val ArmBtnColor = Color(0xFFF6A6A1)




@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable // The whole app display
fun DisplayApp(viewModel: RobotControllerViewModel = hiltViewModel(),
               onSettingPressed: () -> Unit,
               onMapPressed: () -> Unit,
               onVoiceCommandPressed: () -> Unit) {

    val configuration = LocalConfiguration.current // check view mode
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val isAdvancedMode = viewModel.isAdvancedMode.collectAsState()
    val logLines by viewModel.logLines.collectAsState()
    var showHelp by remember { mutableStateOf(false) }

    fun onClick(
        display: String = "",
        message: String? = null
    ) {
        viewModel.setDisplayText(display)
        message?.let{viewModel.webSocketManager.sendMessage(message)}
    }

//    val microphonePermissionState = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)
//    fun onVoiceClick(){
//        if (microphonePermissionState.status.isGranted) {
//            Log.d("DEBUG", "Permission Granted")
//            onClick("Listening...")
//        } else {
//            microphonePermissionState.launchPermissionRequest()
//        }
//    }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Home", fontSize = 22.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = {showHelp = true}) {
                        Icon(
                            imageVector = Icons.Filled.Help,
                            contentDescription = "Open help panel",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {onMapPressed()}) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Go to map"
                        )
                    }
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
            //'Landscape' view design section
            if (isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ){
                    if (isAdvancedMode.value){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.weight(4f)
                            ) {
                                ShowMonitor(viewModel.displayText.value)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                Alignment.CenterVertically,
                            ) {
                                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                    Power(viewModel, !isLandscape)
                                }
                                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                    Advance(
                                        isAdvancedMode.value,
                                        !isLandscape,
                                        onClick = { viewModel.toggleAdvancedMode() }
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(modifier = Modifier.weight(1.5f)) {
                                ScrollableList(logLines)
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                            ) {
                                viewModel.getInstruct()?.let { it1 ->
                                    PacketMenuSpinner(
                                        packetList = viewModel.packetList,
                                        selectIndex = viewModel.currPacket,
                                        packetInstruct = it1,
                                        onSelect = { selectIndex ->
                                            viewModel.onSelectPacket(selectIndex)
                                        }
                                    )
                                }
                                val parameters = viewModel.getParameter()
                                var parameterVal by remember { mutableStateOf(parameters?.let { it1 ->
                                    List(
                                        it1.size) { "" }
                                }) }
                                if (parameters != null) {
                                    parameterVal?.let { it1 ->
                                        ParameterField(
                                            parameters,
                                            it1,
                                            enable = viewModel.isPowerOn.value,
                                            onClick = { inputVal ->
                                                parameterVal = inputVal
                                                val message = "${viewModel.getId()}${
                                                    parameterVal!!.joinToString(", ", "(", ")")
                                                }"
                                                //TODO("clarify exactly how they want the message to be formatted for websocket")
                                                //viewModel.webSocketManager.sendMessage(message)
                                                viewModel.setDisplayText("Message sent")
                                                viewModel.addLogMessage(message)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }else {
                        Spacer(Modifier.weight(.05f))

                        //grabber
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxHeight(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Lift(viewModel, isLandscape)
                                    Lower(viewModel, isLandscape)
                                }
                                Column(
                                    modifier = Modifier.fillMaxHeight(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Extend(viewModel, isLandscape)
                                    Retract(viewModel, isLandscape)
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                Grab(viewModel, isLandscape)
                                Release(viewModel, isLandscape)
                            }
                        }

                        Spacer(Modifier.weight(.05f))

                        //monitor
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.weight(2.5f)
                        ) {
                            ShowMonitor(viewModel.displayText.value)
                        }

                        Spacer(Modifier.weight(.05f))

                        //control, power, toggle
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(.7f),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                Alignment.CenterVertically,
                            ) {
                                Box(Modifier.weight(1f), contentAlignment = Alignment.Center){
                                    Advance(
                                        isAdvancedMode.value,
                                        isLandscape = false,
                                        onClick = { viewModel.toggleAdvancedMode() }
                                    )
                                }
                                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                    Power(viewModel,isLandscape)
                                }
                                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                    // TODO: Landscape
                                    JoyStickToggle(viewModel)
                                    Text(
                                        "J",
                                        fontWeight = FontWeight.Bold,
                                        color = if (viewModel.usingJoystick.value) Color(0xFFF24236) else Color.DarkGray,
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                            .fillMaxWidth(.3f)
                                    )
                                }
                            }

//                            Box(Modifier.weight(.7f)) {
//                                navigationIcon {
//                                    IconButton(onClick = {onVoiceCommandPressed() }) {
//                                        Icon(
//                                            imageVector = Icons.Filled.Mic,
//                                            contentDescription = "Voice Command Screen"
//                                        )
//                                    }
//                                }
//                            }

                            //control
                            Box(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxSize()
                            ) {
                                if (!viewModel.usingJoystick.value) {
                                    Box(modifier = Modifier.align(Alignment.TopCenter)) {
                                        Forward(viewModel, isLandscape)
                                    }
                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Left(viewModel, isLandscape)
                                        Spacer(modifier = Modifier.weight(0.7f))
                                        Right(viewModel, isLandscape)
                                    }
                                    Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                                        Backward(viewModel, isLandscape)
                                    }
                                } else {
                                    JoyStick(viewModel)
                                }

                                Box(
                                    Modifier
                                        .align(Alignment.TopEnd)
                                        .fillMaxSize(0.3f), contentAlignment = Alignment.Center
                                ) {
                                    Turn(
                                        icon = Icons.Filled.Rotate90DegreesCw,
                                        isLandscape = isLandscape,
                                        enable = viewModel.isPowerOn.value,
                                        onPress = {
                                            viewModel.hardRotationRight()
                                            onClick(
                                                "Turning Right 90deg",
                                                "Turn right 90deg"
                                            )

                                        },
                                        onRelease = { onClick() }
                                    )
                                }

                                Box(
                                    Modifier
                                        .align(Alignment.TopStart)
                                        .fillMaxSize(0.3f), contentAlignment = Alignment.Center
                                ) {
                                    Turn(
                                        "left",
                                        icon = Icons.Filled.Rotate90DegreesCcw,
                                        isLandscape = isLandscape,
                                        enable = viewModel.isPowerOn.value,
                                        onPress = {
                                            viewModel.hardRotationLeft()
                                            onClick(
                                                "Turning Left 90deg",
                                                "Turn left 90deg"
                                            )
                                        },
                                        onRelease = { onClick() }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.weight(.05f))
                    }
                }
            }else{
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it) // use the whole screen size
                ) {
                    // Monitor section
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        ShowMonitor(viewModel.displayText.value) // .value makes it a string
                    }

                    // Power button, joystick toggle, advance button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        Alignment.CenterVertically,
                    ) {
                        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            // TODO: Landscape
                            JoyStickToggle(viewModel)
                            Text(
                                "J",
                                fontWeight = FontWeight.Bold,
                                color = if (viewModel.usingJoystick.value) Color(0xFFF24236) else Color.DarkGray,
                                modifier = Modifier.align(Alignment.CenterEnd).fillMaxWidth(.3f)
                            )
                        }
                        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Power(viewModel, isLandscape)
                        }
                        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Advance(
                                isAdvancedMode.value,
                                isLandscape,
                                onClick = { viewModel.toggleAdvancedMode() }
                            )
                        }
                    }

                    if (!isAdvancedMode.value) {
                        // Manipulation section
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(0.4f)
                                .fillMaxWidth()
                        ) {
                            Grab(viewModel, isLandscape)
                            Spacer(modifier = Modifier.width(32.dp)) // add spacing between buttons
                            Release(viewModel, isLandscape)
                        }

                        // Elevation section
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(0.4f)
                                .fillMaxWidth()
                        ) {
                            Lift(viewModel, isLandscape)
                            Spacer(modifier = Modifier.width(32.dp)) // add spacing between buttons
                            Lower(viewModel, isLandscape)
                        }

                        // Retract section
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(0.4f)
                                .fillMaxWidth()
                        ) {
                            Extend(viewModel, isLandscape)
                            Spacer(modifier = Modifier.width(32.dp)) // add spacing between buttons
                            Retract(viewModel, isLandscape)
                        }

                        // Navigation section
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ){
                            if (!viewModel.usingJoystick.value) {
                                Box(
                                    modifier = Modifier.align(Alignment.TopCenter)
                                ) {
                                    Forward(viewModel, isLandscape)
                                }
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Left(viewModel, isLandscape)
                                    Spacer(modifier = Modifier.weight(0.7f))
                                    Right(viewModel, isLandscape)
                                }
                                Box(
                                    modifier = Modifier.align(Alignment.BottomCenter)
                                ) {
                                    Backward(viewModel, isLandscape)
                                }
                            } else {
                                JoyStick(viewModel)
                            }

                            Box(
                                Modifier
                                    .align(Alignment.TopEnd)
                                    .fillMaxSize(0.3f), contentAlignment = Alignment.Center
                            ) {
                                Turn(
                                    icon = Icons.Filled.Rotate90DegreesCw,
                                    isLandscape = isLandscape,
                                    enable = viewModel.isPowerOn.value,

                                    onPress = { onClick("Turning Right 90deg", "Turn right 90deg")
                                              viewModel.hardRotationRight()},
                                    onRelease = {onClick()}
                                )
                            }

                            Box(
                                Modifier
                                    .align(Alignment.TopStart)
                                    .fillMaxSize(0.3f), contentAlignment = Alignment.Center
                            ) {
                                Turn(
                                    "left",
                                    icon = Icons.Filled.Rotate90DegreesCcw,
                                    isLandscape,
                                    viewModel.isPowerOn.value,
                                    onPress = {
                                        onClick("Turning Left 90deg", "Turn left 90deg")
                                        viewModel.hardRotationLeft()
                                              },
                                    onRelease = {onClick()}
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .fillMaxSize(0.3f),
                                contentAlignment = Alignment.Center
                            ) {
                                GlowingButton(
                                    icon = {
                                        IconButton(onClick = {onVoiceCommandPressed()}) {
                                            Icon(
                                            imageVector = Icons.Rounded.Mic,
                                            contentDescription = "Voice Control",
                                            tint = TextColor
                                            )
                                        }
                                    },
                                    btnColor = Color.Red,
                                    onPress = {onVoiceCommandPressed()},
                                    onRelease = {}
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(2.2f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(modifier = Modifier.weight(2f)) {
                                ScrollableList(logLines)
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                            ) {
                                viewModel.getInstruct()?.let { it1 ->
                                    PacketMenuSpinner(
                                        packetList = viewModel.packetList,
                                        selectIndex = viewModel.currPacket,
                                        packetInstruct = it1,
                                        onSelect = { selectIndex ->
                                            viewModel.onSelectPacket(selectIndex)
                                        }
                                    )
                                }
                                val parameters = viewModel.getParameter()
                                var parameterVal by remember { mutableStateOf(parameters?.let { it1 ->
                                    List(
                                        it1.size) { "" }
                                }) }
                                if (parameters != null) {
                                    parameterVal?.let { it1 ->
                                        ParameterField(
                                            parameters,
                                            it1,
                                            enable = viewModel.isPowerOn.value,
                                            onClick = { inputVal ->
                                                parameterVal = inputVal
                                                val message = "${viewModel.getId()}${
                                                    parameterVal!!.joinToString(", ","(",")")
                                                }"
                                                //TODO("clarify exactly how they want the message to be formatted for websocket")
                                                //viewModel.webSocketManager.sendMessage(message)
                                                viewModel.setDisplayText("Message sent")
                                                viewModel.addLogMessage(message)
                                            }
                                        )
                                    }
                                }
                            }
//                                Box(
//                                    modifier = Modifier.weight(1f),
//                                    contentAlignment = Alignment.Center
//                                ){
//                                    InputData(viewModel)
//                                }
                        }
                    }
                    Spacer(Modifier.weight(.1f))
                }
            }

            if (showHelp) {
                Dialog(onDismissRequest = { showHelp = false }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
                    HelpDialog(
                        isLandscape,
                        onClick = {showHelp = false}
                    )
                }
            }
        }
    )
}

@Composable
fun HelpDialog(
    isLandscape: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(0) }
    Box(
        modifier = modifier
            .fillMaxWidth(if (isLandscape) .7f else .9f)
            .fillMaxHeight(if (isLandscape) .85f else .7f),
        contentAlignment = Alignment.Center
    ){
        OutlinedCard(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.outlinedCardElevation(5.dp),
            colors = CardDefaults.outlinedCardColors(Color.LightGray)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 20.dp)
                    .verticalScroll(state)
            ) {
                Text("User Manual", fontWeight = FontWeight.Bold, fontSize = ButtonFontSize, modifier = Modifier.align(Alignment.CenterHorizontally))

                Divider(color = Color.DarkGray)

                Text("Features:", fontWeight = FontWeight.Bold)
                TextFromHtml(
                    Modifier.padding(start = 10.dp),
                    "<p><b>ON/OFF:</b> Toggle to enable control panel</p>" +
                            "<p><b>J Switch:</b> Switch between buttons and joystick for movement</p>"+
                            "<p><b>Advance:</b> Toggle between basic and advanced modes</p>"+
                            "<p><b>Mic:</b> Press and hold then release to send a voice command</p>"
                )
                Text("Directional Controls:", fontWeight = FontWeight.Bold)
                TextFromHtml(
                    Modifier.padding(start = 10.dp),
                    "<p>Available as 4 buttons or joystick</p>" +
                            "<p><b>90° Clockwise Turn:</b> Top right of directional controls</p>"+
                            "<p><b>90° Counterclockwise Turn:</b> Top left of directional controls</p>"
                )
                Text("Grabber Controls:", fontWeight = FontWeight.Bold)
                TextFromHtml(
                    Modifier.padding(start = 10.dp),
                    "<p><b>Lift/Lower:</b> Press and hold to raise/lower arms</p>" +
                            "<p><b>Extend/Retract:</b> Press and hold to extend/retract arms</p>"+
                            "<p><b>Grab/Release:</b> Press and hold to close/open grabber hands</p>"
                )

                Text("Developers", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
                Divider(color = Color.DarkGray)

                Text("Franks", modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("Jonah", modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("Kyler", modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("Minnie", modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("Riley", modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("Shukra", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }

        //close button
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(color = Color.Red, shape = CircleShape)
                .border(2.dp, Color.White, CircleShape),
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Close Help Dialog",
                tint = Color.White
            )
        }
    }
}

@Composable
fun TextFromHtml(
    modifier: Modifier = Modifier,
    htmlText: String
) {
    Text(
        AnnotatedString.fromHtml(
            htmlText,
            linkStyles = TextLinkStyles(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    fontStyle = FontStyle.Italic,
                    color = Color.Blue
                )
            )
        ),
        modifier
    )
}

@Composable
fun ParameterField(
    parameters: List<String>,
    values: List<String>,
    enable: Boolean = true,
    onClick: (List<String>) -> Unit
){
    val inputValues = remember { mutableStateListOf(*values.toTypedArray())}
    Row(
        modifier = Modifier.fillMaxSize()
    ){
        Column (
            modifier = Modifier
                .fillMaxHeight()
                .weight(4f)
                .verticalScroll(rememberScrollState())
        ){
            parameters.forEachIndexed { index, parameter ->
                CustomInputField(
                    inputLabel = "$parameter:",
                    modifier = Modifier.padding(bottom = 10.dp),
                    currentValue = inputValues[index],
                    onValueChange = {inputValues[index] = it}
                )
            }
        }
        Spacer(modifier = Modifier.weight(.2f))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.BottomCenter
        ){
            CustomButton(
                text = "Send",
                isEnabled = enable,
                onClick = {
                    onClick(inputValues)
                }
            )
        }
    }
}

@Composable
fun Turn(
    orientation: String = "right",
    icon: ImageVector,
    isLandscape: Boolean,
    enable: Boolean,
    onPress: () -> Unit,
    onRelease: () -> Unit
) {
    GlowingButton(
        enabled = enable,
        text = if (isLandscape) null else "90",
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = "rotate $orientation 90 degree",
                tint = TextColor
            )
        },
        btnColor = ManipBtnColor,
        textColor = TextColor,
        fontSize = 16.sp,
        onPress = onPress,
        onRelease = onRelease,
        modifier = Modifier
            .clip(CircleShape)
            .background(ManipBtnColor) // Set the button's background color
            .size(if (isLandscape) RoundButtonSize else SmallBtnSize),
        paddingVal = 2.dp
    )
}


//@Composable
//fun VoiceButton(
//    enable: Boolean,
//    onPress: () -> Unit,
//    onRelease: () -> Unit)
//{
//    GlowingButton(
//        enabled = enable,
//        icon = {
//            IconButton(onClick = {onVoiceCommandPressed()}) {
//                Icon(
//                imageVector = Icons.Rounded.Mic,
//                contentDescription = "Voice Control",
//                tint = TextColor
//                )
//            }
//        },
//        btnColor = Color.Red,
//        onPress = onPress,
//        onRelease = onRelease,
//        modifier = Modifier
//            .clip(CircleShape)
//            .background(ManipBtnColor) // Set the button's background color
//            .size(RoundButtonSize),
//        paddingVal = 2.dp
//    )
//}

@Composable
fun Advance(isAdvancedMode: Boolean, isLandscape: Boolean, onClick: () -> Unit){
    val text = if (isLandscape) {if (isAdvancedMode) "N" else "A" } else{if (isAdvancedMode) "Normal" else "Advance"}
    Button(
        modifier = Modifier
            .size(if (isLandscape) RoundButtonSize else SmallBtnSize)
            .clip(RoundedCornerShape(1.dp)),
        onClick = onClick,
        contentPadding = PaddingValues(1.dp),
        colors = ButtonColors(
            containerColor = Color.Magenta,
            contentColor = Color.LightGray,
            disabledContentColor = Color.Green,
            disabledContainerColor = Color.Yellow
        )
    ) {
        Text(
            text,
            fontSize = if (isLandscape) ButtonFontSize else 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
//    CustomButton(
//        text = if (isAdvancedMode) "Normal" else "Advance",
//        isEnabled = true,
//        onClick = {onClick()}
//    )
}

@Composable
fun ParameterField(){}

@Composable
fun PacketMenuSpinner(
    packetList: List<String>,
    selectIndex: Int = 0,
    packetInstruct: String,
    onSelect: (Int) -> Unit
){
    var isExpanded by remember {mutableStateOf(false)}
    val itemPosition = remember {mutableIntStateOf(selectIndex)}
    var isVisible by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .weight(5f)
                .clickable {
                    isExpanded = true
                },
            text = packetList[itemPosition.intValue],
            color = Color.Black
        )
        Icon(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    isExpanded = true
                },
            imageVector = Icons.Filled.ArrowDropUp,
            contentDescription = "Expand packets list",
            tint = Color.Black
        )
        IconButton(
            modifier = Modifier.weight(1f),
            onClick = {isVisible = !isVisible}
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "View Packet Information",
                tint = Color.Black
            )
        }
    }

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = {isExpanded = false},
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
    ){
        packetList.forEachIndexed { index, item ->
            DropdownMenuItem(
                text = {Text(text = item)},
                onClick = {
                    isExpanded = false
                    itemPosition.intValue = index
                    onSelect(index)
                }
            )
        }
    }

    Box(Modifier.fillMaxWidth()){
        if(isVisible){
            Popup(
                alignment = Alignment.CenterEnd,
                onDismissRequest = { isVisible = false }
            ) {
                Box(Modifier.background(Color.DarkGray)) {
                    Text(packetInstruct, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ScrollableList(logLines: List<String>){
    val listState = rememberLazyListState()

    LaunchedEffect(logLines.size) {
        if (logLines.isNotEmpty()) listState.animateScrollToItem(logLines.size - 1)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(color = Color.LightGray)
    ) {
        items(logLines) { logLine ->
            Text(text = logLine)
        }
    }
}

@Composable
fun InputData(viewModel: RobotControllerViewModel){
    var textInput: String by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        TextField(
            value = textInput,
            onValueChange = { newText ->
                textInput = newText
            },
            placeholder = {
                Text(text = "Data input")
            }
        )
        CustomButton(
            text = "Send",
            isEnabled = true,
            PaddingValues(start = 15.dp),
            onClick = {
                if(viewModel.isPowerOn.value) {
                    //WebSocketClient.sendMessage(textInput)
                    viewModel.setDisplayText("Message sent")
                    viewModel.addLogMessage(textInput)
                }
            }
        )
    }
}

// Power button to turn on/off connection
@Composable
fun Power(viewModel: RobotControllerViewModel, isLandscape: Boolean) {
    // Access the current context (Activity or Context)
    val context = LocalContext.current
    val isOn = viewModel.isPowerOn.value
    Button(
        onClick = {
            viewModel.switchPowerStatus()  // Toggle power status
            val localisOn = viewModel.isPowerOn.value


            // Toggle the text based on the power status
            viewModel.setDisplayText(
                if (localisOn) "<camera live>" else "<camera offline>"
            )

            // Connect to WebSocket when power is turned on
            if (localisOn) {
                viewModel.startCommunication()

                //viewModel.webSocketManager.connect(context)  // Call the connect method when power is ON
            } else {
                viewModel.endCommunication()


                //viewModel.webSocketManager.disconnect()  // Optionally, close the connection when power is OFF
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(if (isOn) 0xFFFF5733 else 0xFF4CAF50), // Green if on, red if off
            contentColor = TextColor
        ),
        modifier = Modifier
            .clip(CircleShape) // Make the button circular
            .size(if (isLandscape) SmallBtnSize else LargeBtnSize),
        contentPadding = PaddingValues(8.dp)
    ) {
        Text(
            if (isOn) "OFF" else "ON",
            fontSize = ButtonFontSize,
            fontWeight = FontWeight.Bold
        )
        if (!isLandscape){
            if (isOn) Icon(Icons.Default.Stop, contentDescription = "Off button")
            else Icon(Icons.Default.PlayArrow, contentDescription = "On button")
        }
    }
}

@Composable
fun JoyStickToggle(viewModel: RobotControllerViewModel) {
    Switch(
        checked = viewModel.usingJoystick.value,
        onCheckedChange = {
            viewModel.switchJoystick()
        },
        enabled = !viewModel.isAdvancedMode.collectAsState().value,
        colors = SwitchDefaults.colors(
            checkedThumbColor = if (!viewModel.isAdvancedMode.collectAsState().value) Color(0xFFF24236) else Color.LightGray,
            checkedTrackColor = if (!viewModel.isAdvancedMode.collectAsState().value) Color(0xFFC55B58) else Color.LightGray,
            uncheckedThumbColor = if (!viewModel.isAdvancedMode.collectAsState().value) Color.DarkGray else Color.LightGray,
            uncheckedTrackColor = if (!viewModel.isAdvancedMode.collectAsState().value) Color(0xFF707070) else Color.LightGray,
        )
    )
}

@Composable
fun JoyStick(viewModel: RobotControllerViewModel = hiltViewModel(), filter: PointerEventType? = null) {
    Canvas(modifier = Modifier
        .fillMaxSize()
        .drawWithContent
        {
            drawContent()
            drawCircle(Color.Gray, radius = 100.dp.toPx())
            drawCircle(Color.Black, radius = 30.dp.toPx())

        }
        .pointerInput(filter) {
            detectDragGestures {_, _ -> Log.d("Debug", "Dragging")}
            awaitPointerEventScope {
                val event = awaitPointerEvent()

                if (filter == null || event.type == filter) {
                    Log.d("Debug", "${event.type}, ${event.changes.first().position}")
                }
            }
        }) {

    }
}

//@Composable
//fun JoystickNub(viewModel: RobotControllerViewModel = hiltViewModel()) {
//    val offsetX = remember { mutableStateOf(0f) }
//    val offsetY = remember { mutableStateOf(0f) }
//    Box(
//        Modifier.offset{ IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt())}
//            .size(50.dp)
//            .background(Color.Blue)
//            .pointerInput(Unit) {
//                detectDrageGesturess {}
//            }
//    )
//}
// TODO : This will be camera input, change it to do so ASAP
// Temporarily a monitor: display text/action that's taking place/happening
@Composable
fun ShowMonitor(displayText: String){ //
    Column(
        // background
        modifier = Modifier
            .fillMaxSize() // makes the column take up the full available space
            .background(MonitorBgColor),
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
                .border(2.dp, MonitorBgColor) // to simulate a monitor border
                .background(MonitorBgColor),
            contentAlignment = Alignment.Center // center content
        ) {
            Text(
                displayText,
                fontSize = MonitorFontSize ,
                color = MonitorTextColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center) // center text horizontally & vertically
            )
        }
    }
}

@Composable
fun Grab(viewModel: RobotControllerViewModel , isLandscape: Boolean) {
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text = "Grab",
        icon = {if (!isLandscape) {Icon(Icons.Default.AddCircle, contentDescription = "Grab", tint = TextColor) }},
        btnColor = ManipBtnColor,
        textColor = TextColor,
        fontSize = ButtonFontSize,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.grab()
                //viewModel.webSocketManager.sendMessage("Grab")
                viewModel.setDisplayText("Grabbing item...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(NavBtnColor) // Set the button's background color
            .size(if (isLandscape) MediumBtnSize else XLargeBtnSize),
        paddingVal = 6.dp
    )
}

@Composable
fun Release(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text ="Release" ,
        icon = {if (!isLandscape) {Icon(Icons.Default.CheckCircle, contentDescription = "Grab", tint = TextColor) }},
        btnColor = ManipBtnColor ,
        textColor = TextColor,
        fontSize = ButtonFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.release()
                //viewModel.webSocketManager.sendMessage("Release")
                viewModel.setDisplayText("Releasing item...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(NavBtnColor) // Set the button's background color
            .size(if (isLandscape) MediumBtnSize else XLargeBtnSize),
        paddingVal = 6.dp
    )
}

// Elevation: consist of 'Lift' & 'Lower' buttons
@Composable
fun Lift(viewModel: RobotControllerViewModel, isLandscape: Boolean) { // 'Lift' btn
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        isHorizontal = !isLandscape,
        postText = "Lift" ,
        icon = {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Lift arm",
                tint = TextColor,
                modifier = Modifier.size(if(isLandscape) 25.dp else 45.dp)
            )
        },
        btnColor = ElevBtnColor ,
        textColor = TextColor,
        fontSize = ButtonFontSize ,
        shape = RoundedCornerShape(35.dp),
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.lift()
                //viewModel.webSocketManager.sendMessage("Lift")
                viewModel.setDisplayText("Lifting item...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(if (isLandscape) RoundedCornerShape(32.dp) else CircleShape)
            .background(ElevBtnColor) // Set the button's background color
            .size(if (isLandscape) VertButtonSize else XLargeBtnSize),
        paddingVal = 6.dp
    )
}

@Composable
fun Lower(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        isHorizontal = !isLandscape,
        text = "Lower" ,
        icon = {
            Icon(

                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Lower arm",
                tint = TextColor,
                modifier = Modifier.size(if(isLandscape) 25.dp else 45.dp)
            )
        },
        btnColor = ElevBtnColor ,
        textColor = TextColor,
        fontSize = ButtonFontSize ,
        shape = RoundedCornerShape(35.dp),
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.lower()
                //viewModel.webSocketManager.sendMessage("Lower")
                viewModel.setDisplayText("Lowering item...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(if (isLandscape) RoundedCornerShape(32.dp) else CircleShape)
            .background(ElevBtnColor) // Set the button's background color
            .size(if (isLandscape) VertButtonSize else XLargeBtnSize),
        paddingVal = 6.dp
    )
}

// Navigation: consists of 'Forward' 'Backward' 'Left' 'Right'
@Composable
fun Forward(viewModel: RobotControllerViewModel,isLandscape : Boolean) {
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text = if (isLandscape) "" else "Forward" ,
        icon = {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Move forward",
                tint = TextColor,
                modifier = Modifier.size(45.dp)
            )
        },
        btnColor = NavBtnColor ,
        textColor = TextColor,
        fontSize = ButtonFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.moveUp()
                //viewModel.webSocketManager.sendMessage("Forward")
                viewModel.setDisplayText("Moving Forward...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(NavBtnColor) // Set the button's background color
            .size(if (isLandscape) SmallBtnSize else XLargeBtnSize)
    )
}

@Composable
fun Backward(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text = if (isLandscape) "" else "Backward" ,
        icon = {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Move backward button",
                tint = TextColor,
                modifier = Modifier.size(45.dp)
            )
        },
        btnColor = NavBtnColor ,
        textColor = TextColor,
        fontSize = ButtonFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.moveDown()
                //viewModel.webSocketManager.sendMessage("Backward")
                viewModel.setDisplayText("Moving Backward...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(NavBtnColor) // Set the button's background color
            .size(if (isLandscape) SmallBtnSize else XLargeBtnSize)
    )
}

@Composable
fun Left(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        postText = if (isLandscape) "" else "Left" ,
        icon = {
            Icon(
                imageVector = Icons.Filled.ArrowBackIos,
                contentDescription = "Move left button",
                tint = TextColor
            )
        },
        btnColor = NavBtnColor ,
        textColor = TextColor,
        fontSize = ButtonFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.moveLeft()
//                viewModel.webSocketManager.sendMessage("Left")
                viewModel.setDisplayText("Moving Left...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(NavBtnColor) // Set the button's background color
            .size(if (isLandscape) SmallBtnSize else XLargeBtnSize)
    )
}

@Composable
fun Right(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text = if (isLandscape) "" else "Right " ,
        icon = {
            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = "Move right button",
                tint = TextColor
            )
        },
        btnColor = NavBtnColor ,
        textColor = TextColor,
        fontSize = ButtonFontSize ,
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.moveRight()
                //viewModel.webSocketManager.sendMessage("Right")
                viewModel.setDisplayText("Moving Right...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(NavBtnColor) // Set the button's background color
            .size(if (isLandscape) SmallBtnSize else XLargeBtnSize)
    )
}

// (Arms) Positioning
@Composable
fun Extend(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text ="Extend",
        btnColor = ArmBtnColor ,
        textColor = TextColor,
        fontSize = ButtonFontSize ,
        shape = RoundedCornerShape(35.dp),
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.webSocketManager.sendMessage("Extend")
                viewModel.setDisplayText("Extending arm...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(if (isLandscape) RoundedCornerShape(35.dp) else CircleShape)
            .background(NavBtnColor) // Set the button's background color
            .size(if (isLandscape) VertButtonSize else XLargeBtnSize),
        paddingVal = 6.dp
    )
}

@Composable
fun Retract(viewModel: RobotControllerViewModel, isLandscape: Boolean){
    GlowingButton(
        enabled = viewModel.isPowerOn.value,
        text = "Retract",
        btnColor = ArmBtnColor ,
        textColor = TextColor,
        fontSize = ButtonFontSize ,
        shape = RoundedCornerShape(35.dp),
        onPress = {
            if(viewModel.isPowerOn.value) {
                viewModel.webSocketManager.sendMessage("Retract")
                viewModel.setDisplayText("Retracting arm...")
            }
        },
        onRelease = {
            viewModel.setDisplayText("")
        },
        modifier = Modifier
            .clip(if (isLandscape) RoundedCornerShape(35.dp) else CircleShape)
            .background(NavBtnColor) // Set the button's background color
            .size(if (isLandscape) VertButtonSize else XLargeBtnSize),
        paddingVal = 6.dp
    )
}