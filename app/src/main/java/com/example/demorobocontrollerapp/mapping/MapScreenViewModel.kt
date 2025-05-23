package com.example.demorobocontrollerapp.mapping

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demorobocontrollerapp.data.RobotInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    private val robotRepository: RobotInfoRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    var _bitmap:Bitmap = createBitmap(0,0)
    fun getMapUpdate(){

    }
    fun listenForMap() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.getMapState().collect{
                value -> _bitmap
            }
        }
    }
}