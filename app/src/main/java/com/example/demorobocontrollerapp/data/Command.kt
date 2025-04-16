package com.example.demorobocontrollerapp.data

data class Command(
    val commandType: CommandType = CommandType.Invalid,
    val speed: Float? = 0f,
    val angularSpeed: Float? = 0f,
    val height: Int? = 0,
    val grabberStatus: GrabberInstruction = GrabberInstruction.Idle,
    )

enum class CommandType {
    SendMove,
    SendGrab,
    GetMove,
    GetGrab,
    RobotUpdateStatus,
    Invalid
}


enum class GrabberInstruction {
    Close,
    Open,
    Idle
}

enum class ElevatorStatus {
    MovingUp,
    MovingDown,
    Idle
}

enum class PincerStatus {
    Open,
    Close,
    Opening,
    Closing
}