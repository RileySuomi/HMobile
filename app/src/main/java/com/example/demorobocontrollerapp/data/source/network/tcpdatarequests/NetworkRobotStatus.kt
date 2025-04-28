package com.example.demorobocontrollerapp.data.source.network.tcpdatarequests

data class NetworkMovementInstruction(
    val speed: Float,
    val angle: Float,

)

data class NetworkGrabberInstruction(
    val height: Int,
    val grabStatus: GrabberInstruction
)

data class RobotUpdate(
    val battery: Float,
    val temp: Float,
)

data class GrabberInformation(
    val grabberHeight: Int,
    val pincerStatus: PincerStatus,
    val movingStatus: ElevatorStatus,
)

data class MovementResponse(
    val packetResponse: String,
    val speed: Float,
)

data class DebugPacket(
    val parameters: List<String>,
)

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