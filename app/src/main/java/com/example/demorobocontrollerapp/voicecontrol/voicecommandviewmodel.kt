package com.example.demorobocontrollerapp.voicecontrol

import android.app.Application
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.demorobocontrollerapp.data.RobotInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

private const val TAG = "VoiceCommandViewModel"

@HiltViewModel
class VoiceCommandViewModel @Inject constructor(
    private val robotrepository: RobotInfoRepository,
    application: Application
) : AndroidViewModel(application) {

    // State flows for UI
    private val _recognizedText = MutableStateFlow("Speak a command...")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()

    private val _commandStatus = MutableStateFlow("Ready for voice command")
    val commandStatus: StateFlow<String> = _commandStatus.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    // Speech recognizer
    private var speechRecognizer: SpeechRecognizer? = null
    private lateinit var recognitionListener: RecognitionListener

    init {
        // Initialize speech recognizer if available
        if (SpeechRecognizer.isRecognitionAvailable(getApplication())) {
            initializeSpeechRecognizer()
        } else {
            _commandStatus.value = "Speech recognition not available on this device"
        }
    }

    private fun initializeSpeechRecognizer() {
        Log.d(TAG, "Initializing speech recognizer")
        try {
            // Create speech recognizer
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplication())

            // Set up recognition listener
            recognitionListener = object : RecognitionListener {
                override fun onReadyForSpeech(p0: Bundle?) {
                    Log.d(TAG, "onReadyForSpeech")
                    _recognizedText.value = "Listening..."
                    _isListening.value = true
                }

                override fun onBeginningOfSpeech() {
                    Log.d(TAG, "onBeginningOfSpeech")
                }

                override fun onRmsChanged(rmsdB: Float) {}

                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onEndOfSpeech() {
                    Log.d(TAG, "onEndOfSpeech")
                    _isListening.value = false
                }

                override fun onError(error: Int) {
                    val errorMessage = when(error) {
                        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                        SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                        SpeechRecognizer.ERROR_NETWORK -> "Network error"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                        SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
                        SpeechRecognizer.ERROR_SERVER -> "Server error"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                        else -> "Error code: $error"
                    }

                    Log.e(TAG, "Speech recognition error: $errorMessage")
                    _recognizedText.value = "Error: $errorMessage"
                    _isListening.value = false
                    _commandStatus.value = "Ready for voice command"
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (matches != null && matches.isNotEmpty()) {
                        val text = matches[0]
                        Log.d(TAG, "Speech recognized: $text")
                        _recognizedText.value = "You said: $text"

                        // Process the command
                        processCommand(text)
                    } else {
                        Log.d(TAG, "No recognition results")
                        _recognizedText.value = "Didn't hear anything"
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (matches != null && matches.isNotEmpty()) {
                        val text = matches[0]
                        Log.d(TAG, "Partial result: $text")
                        _recognizedText.value = "Hearing: $text"
                    }
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}
            }

            // Set the recognition listener
            speechRecognizer?.setRecognitionListener(recognitionListener)
            Log.d(TAG, "Speech recognizer initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing speech recognizer: ${e.message}", e)
            _commandStatus.value = "Error: ${e.message}"
        }
    }

    // Called by the UI when the user clicks the mic button
    fun startListening() {
        Log.d(TAG, "startListening called")

        // Initialize if needed
        if (speechRecognizer == null) {
            if (SpeechRecognizer.isRecognitionAvailable(getApplication())) {
                initializeSpeechRecognizer()
            } else {
                _commandStatus.value = "Speech recognition not available"
                return
            }
        }

        _recognizedText.value = "Listening..."
        _commandStatus.value = "Processing voice input..."

        // Create the intent
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak a command")
        }

        try {
            Log.d(TAG, "Starting speech recognition...")
            speechRecognizer?.startListening(intent)
            Log.d(TAG, "Speech recognition started")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting speech recognition: ${e.message}", e)
            _recognizedText.value = "Error: ${e.message}"
            _isListening.value = false
        }
    }

    // Called by the UI when the user clicks the stop button
    fun stopListening() {
        Log.d(TAG, "stopListening called")
        try {
            speechRecognizer?.stopListening()
            _isListening.value = false
            _commandStatus.value = "Ready for voice command"
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping speech recognition: ${e.message}", e)
        }
    }

    fun processCommand(command: String) {
        val lowerCommand = command.lowercase()
        Log.d(TAG, "Processing command: $lowerCommand")

        when {
            // Navigation commands
            lowerCommand.contains("go forward") || lowerCommand.contains("straight ahead") -> {
                _commandStatus.value = "Executing: forward movement"
                viewModelScope.launch(Dispatchers.IO) {
                    robotrepository.sendMovement(1f, 0f)
                }
            }

            lowerCommand.contains("go back") || lowerCommand.contains("go backwards") || lowerCommand.contains("back") -> {
                _commandStatus.value = "Executing: backward movement"
                viewModelScope.launch(Dispatchers.IO) {
                    robotrepository.sendMovement(-1f, 0f)
                }
            }

            lowerCommand.contains("turn right") || lowerCommand.contains("right") -> {
                _commandStatus.value = "Executing: right turn"
                viewModelScope.launch(Dispatchers.IO) {
                    robotrepository.sendMovement(0f, 1f)
                }
            }

            lowerCommand.contains("turn left") || lowerCommand.contains("left") -> {
                _commandStatus.value = "Executing: left turn"
                viewModelScope.launch(Dispatchers.IO) {
                    robotrepository.sendMovement(0f, -1f)
                }
            }

            lowerCommand.contains("stop") || lowerCommand.contains("halt") -> {
                _commandStatus.value = "Executing: stop movement"
                viewModelScope.launch(Dispatchers.IO) {
                    robotrepository.sendMovement(0f, 0f)
                }
            }

            lowerCommand.contains("riley") -> {
                _commandStatus.value = "Best person ever"
            }

            else -> {
                _commandStatus.value = "Command not recognized: $command"
            }
        }
    }

    // Method to update command status from outside
    fun updateCommandStatus(status: String) {
        _commandStatus.value = status
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer?.destroy()
        speechRecognizer = null
    }
}