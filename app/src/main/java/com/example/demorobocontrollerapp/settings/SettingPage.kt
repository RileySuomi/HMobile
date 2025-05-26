@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.demorobocontrollerapp.settings

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.Checkbox


////use as 'preview'
//@Preview(showBackground = true)
//@Composable
//fun SettingPreview() {
//    val navController = rememberNavController()
//
//    DemoRoboControllerAppTheme {
//
//        val fakeRepo = DataStoreRepo {
//            override suspend fun putString(key: String, value: String) {}
//            override suspend fun putBoolean(key: String, value: Boolean) {}
//            override suspend fun getString(key: String): Flow<String>{
//                return flow { emit("Mocked string value")}
//            }
//            override suspend fun getBoolean(key: String) : Flow<Boolean> {
//                return flow { emit(false)}
//            }
//
//            override suspend fun clearPReferences(key: String) {
//                TODO("Not yet implemented")
//            }
//        }
//
//        val fakeViewModel = RobotControllerViewModel(fakeRepo)
//
//        DisplaySetting(
//            viewModel = fakeViewModel,
//            onBackPressed = { navController.navigate("home") }
//        )
//    }
//}

@Composable
fun DisplaySetting(viewModel: SettingViewModel = hiltViewModel(), onBackPressed: () -> Unit) {
    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Setting", fontSize = 22.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { onBackPressed()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },

        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(10.dp)
                    .pointerInput(Unit){
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
            ) {
                val settings by viewModel.uiState.collectAsStateWithLifecycle()
                if (settings is SettingsModelUiState.Success) {
                    InternalSettingsSection(
                        items = (settings as SettingsModelUiState.Success).data,
                        viewModel
                    )
                }

            }
        }
    )
}

@Composable
internal fun InternalSettingsSection(
    items: List<SettingsProperty>,
    viewModel: SettingViewModel
) {
    items.forEach {
        if (it.editable) {
            EditableSettingsPair(viewModel, it.dataKey, it.screenName, it.currentValue)
        }
        else {
            SettingPair(it.screenName, it.currentValue)
        }

        HorizontalDivider(color = Color.LightGray)
    }


}

@Composable
internal fun CheckBoxSettingsPair(viewModel: SettingViewModel, key: String, display: String, onOff: Boolean) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(10.dp,0.dp),
            text = display,
            color = Color.Black,
        )
        Checkbox(
            checked = onOff,
            onCheckedChange = {
                val newValue = it.toString()
                viewModel.updateById(key, newValue)
            }
        )
    }
}

@Composable
internal fun EditableSettingsPair(viewModel: SettingViewModel, key: String, display: String, setting: String) {
    var input by remember { mutableStateOf(setting) }
    val textField = FocusRequester()
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = display,
            color = Color.Black,
        )
        TextField(
            value = TextFieldValue(input, selection = TextRange(input.length)),
            onValueChange = { newValue: TextFieldValue ->
                input = newValue.text
                viewModel.updateById(key, input)
                Log.d("Settings", "Updated field $key to value $input")
            },
            modifier = Modifier
                .padding(10.dp, 0.dp, 0.dp, 0.dp)
                .weight(2f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Number,
                showKeyboardOnFocus = true
            ),
            keyboardActions = KeyboardActions(){
                viewModel.updateById(key, input.trim())
                Log.d("Settings", "Updated field $key to value ${input.trim()}")
                textField.freeFocus()
            },
            colors = TextFieldDefaults.colors(
                cursorColor = Color.Black
            ),
            trailingIcon = { Icon(imageVector = Icons.Rounded.CheckCircleOutline, contentDescription = "valid input") }
            //TODO: ask abt validation
        )
    }
}

@Composable
internal fun SettingPair(display: String, setting: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = display,
            color = Color.Black
        )
        Text(
            modifier = Modifier
                .padding(20.dp, 0.dp, 0.dp, 0.dp)
                .weight(2f),
            text = setting,
            color = Color.DarkGray,
        )
    }
}