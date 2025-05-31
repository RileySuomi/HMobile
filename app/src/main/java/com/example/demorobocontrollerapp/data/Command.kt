package com.example.demorobocontrollerapp.data

import com.example.demorobocontrollerapp.data.source.local.command.LocalCommand
import kotlin.math.max
import kotlin.math.min

data class Command(
    val commandType: CommandType = CommandType.Invalid,
    val speed: Float = 0f,
    val angularSpeed: Float = 0f,
    val height: Int = 0,
    val grabberStatus: GrabberInstruction = GrabberInstruction.Idle,
    val xCoordinate: Int = 0,
    val yCoordinate: Int = 0
    )

fun LocalToData(c: LocalCommand): Command {
    when (c.type) {
        CommandType.SendMove.value -> {
            return Command(CommandType.fromInt(c.type), speed = c.val1, angularSpeed = c.val2)
        }
        CommandType.SendGrab.value -> {
            return Command(CommandType.fromInt(c.type),
                grabberStatus = GrabberInstruction.fromInt(c.val1.toInt()))
        }
        CommandType.SendLift.value -> {
            return Command(CommandType.fromInt(c.type), height = c.val1.toInt())
        }
        else -> {
            return Command(CommandType.fromInt(c.type))
        }


    }
}

fun ConvertToLocal(c: Command): LocalCommand {
    when (c.commandType) {
        CommandType.SendMove -> {
            return LocalCommand(type = c.commandType.value, val1 = c.speed, val2 = c.angularSpeed)
        }
        CommandType.SendGrab -> {
            when(c.grabberStatus) {
                GrabberInstruction.Idle -> {
                    LocalCommand(type = c.commandType.value, val1 = 0f)
                }
                GrabberInstruction.Open -> {
                    LocalCommand(type = c.commandType.value, val1 = 1f)
                }
                GrabberInstruction.Close -> {
                    LocalCommand(type = c.commandType.value, val1 = 2f)
                }
            }

        }
        CommandType.SendLift -> {
            LocalCommand(type = c.commandType.value, val1 = c.height.toFloat())
        }
        else -> {
            LocalCommand(type = c.commandType.value, val1 = 0f)
        }
    }
    return LocalCommand(type = c.commandType.value, val1 = 0f)

}



enum class CommandType(val value: Int) {
    SendMove(0),
    SendGrab(1),
    SendLift(2),
    SendExtend(3),
    Invalid(4);

    companion object {
        fun fromInt(value: Int) = CommandType.entries.first() {
            it.value == min(4, max(value, 0))
        }
    }
}


enum class GrabberInstruction(val value: Int) {
    Close(0),
    Open(1),
    Idle(2);

    companion object {
        fun fromInt(value: Int) = GrabberInstruction.entries.first() {
            it.value == min(4, max(value, 0))
        }
    }
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