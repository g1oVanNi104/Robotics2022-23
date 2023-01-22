package org.firstinspires.ftc.teamcode.commands

import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.ParallelDeadlineGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitUntilCommand
import org.firstinspires.ftc.teamcode.commands.drive.ApproachAngle
import org.firstinspires.ftc.teamcode.subsystems.*

class ApproachPoleFromAngle(
    mecanum: Mecanum,
    vision: Vision,
    speed: () -> Double
) : SequentialCommandGroup() {

    init {
        addCommands(
            InstantCommand(vision::startStreamingRearCamera),
            SequentialCommandGroup(
                // Drive normally until a cone has been detected
                // TODO: Default command doesn't update if it is changed
                ParallelDeadlineGroup(
                    WaitUntilCommand { vision.getPoleAngle() != null },
                    mecanum.defaultCommand
                ),
                ApproachAngle(
                    mecanum,
                    vision::getPoleAngle,
                    speed
                )
            ),
            InstantCommand(vision::stopStreamingRearCamera)

        )
        addRequirements(mecanum, vision)
    }
}