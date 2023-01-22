package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.geometry.Vector2d
import org.firstinspires.ftc.teamcode.subsystems.*
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.drive.localization.OdometryLocalizer

@Config
object RobotConfig {

    /****************************************************
     * Field                                            *
     ****************************************************/

    // TODO CONVERT TO FEET YOU DWEEEB
    const val tileSize = 23.5 // in


    /**
     * Different types of poles.
     */
    enum class PoleType(val height: Double) {
        LOW(0.0),
        MEDIUM(0.0),
        HIGH(0.0)
    }

    /**
     * All poles on the field.
     */
    enum class Pole(x: Int, y: Int, val type: PoleType) {
        TWO_ONE(2, 1, PoleType.LOW),
        TWO_NEG_ONE(2, -1, PoleType.LOW),

        ONE_TWO(1, 2, PoleType.LOW),
        ONE_ONE(1, 1, PoleType.MEDIUM),
        ONE_ZERO(1, 0, PoleType.HIGH),
        ONE_NEG_ONE(1, -1, PoleType.MEDIUM),
        ONE_NEG_TWO(1, -2, PoleType.LOW),

        ZERO_ONE(0, 1, PoleType.HIGH),
        ZERO_NEG_ONE(0, -1, PoleType.HIGH),

        NEG_ONE_TWO(-1, 2, PoleType.LOW),
        NEG_ONE_ONE(-1, 1, PoleType.MEDIUM),
        NEG_ONE_ZERO(-1, 0, PoleType.HIGH),
        NEG_ONE_NEG_ONE(-1, -1, PoleType.MEDIUM),
        NEG_ONE_NEG_TWO(-1, -2, PoleType.LOW),

        NEG_TWO_ONE(-2, 1, PoleType.LOW),
        NEG_TWO_NEG_ONE(-2, -1, PoleType.LOW);

        val vector = Vector2d(x * tileSize, y * tileSize)

        companion object {
            fun getPole(vector: Vector2d): Pole? {
                values().forEach { pole ->
                    if (pole.vector epsilonEquals vector) {
                        return pole
                    }
                }
                return null
            }
        }
    }

    enum class Junction(x: Int, y: Int) {
        TWO_TWO(2, 2),
        TWO_ZERO(2, 0),
        TWO_NEG_TWO(2, -2),

        ZERO_TWO(0, 2),
        ZERO_ZERO(0, 0),
        ZERO_NEG_TWO(0,-2),

        NEG_TWO_TWO(-2, 2),
        NEG_TWO_ZERO(-2, 0),
        NEG_TWO_NEG_TWO(-2, -2);

        val vector = Vector2d(x * tileSize, y * tileSize)

        companion object {
            fun getJunction(vector: Vector2d): Junction? {
                values().forEach { junction ->
                    if (junction.vector.epsilonEquals(vector)) {
                        return junction
                    }
                }
                return null
            }
        }
    }

    /****************************************************
     * Subsystems                                       *
     ****************************************************/

    /**
     * [SampleMecanumDrive]
     */
    @JvmField
    var driveLeftFront: String = "leftFront"

    @JvmField
    var driveLeftRear: String = "leftRear"

    @JvmField
    var driveRightRear: String = "rightRear"

    @JvmField
    var driveRightFront: String = "rightFront"

    /**
     * [OdometryLocalizer]
     */
    const val leftEncoderName = "leftEncoder"
    const val rightEncoderName = "rightEncoder"
    const val frontEncoderName = "frontEncoder"

    var TICKS_PER_REV = 0.0
    var WHEEL_RADIUS = 2.0 // in
    var GEAR_RATIO = 1.0 // output (wheel) speed / input (encoder) speed

    @JvmField
    var X_MULTIPLIER = 1.0 // TODO multipliers for each individual encoder
    @JvmField
    var Y_MULTIPLIER = 1.0
    @JvmField
    var LATERAL_DISTANCE = 10.0 // in; distance between the left and right wheels
    @JvmField
    var FORWARD_OFFSET = 4.0 // in; offset of the lateral wheel

    /**
     * [Lift]
     */
    const val leftLiftName = "leftLift"
    const val rightLiftName = "rightLift"
    const val magnetLimitName = "magnet"

    const val liftHeightOffset = 0.0 // cm The raw height of zero is off the ground
    const val liftMaxExtension = 0.0 // cm Max allowable extension height
    const val poleLiftOffset = 20.0 // cm above the pole the lift should be at

    const val liftDPP = 1.0 // TODO: Find experimentally

    @JvmField
    var liftMaxVel = 20.0 // cm / s  // TODO: Find max values
    @JvmField
    var liftMaxAccel = 20.0 // cm / s2

    @JvmField
    var liftKStatic = 0.0
    @JvmField
    var liftKV = 1.0 / liftMaxVel
    @JvmField
    var liftKA = 0.0
    @JvmField
    var gravityFeedforward = 0.0


    @JvmField
    var liftCoeffs = PIDCoefficients(0.0, 0.0, 0.0) // TODO: Calculate from kV and kA

    val liftTargetErrorTolerance = 1.0 // cm

    const val withinSwitchRange = 5.0 // cm from the bottom to check magnet switch for reset

    /**
     * [Claw]
     */
    const val clawServoName = "claw"
    const val colorSensorName = "color"

    @JvmField
    var clawClosedPosition = 0.85
    @JvmField
    var clawOpenedPosition = 0.73
    @JvmField
    var clawPartiallyOpenedPosition = 0.80


    @JvmField
    var clawMaxVel = 0.7
    @JvmField
    var clawMaxAccel = 4.5

    @JvmField
    var colorGain = 20.0
    @JvmField
    var valueThreshold = 0.15




    /**
     * Passthrough
     */
    const val leftPassthroughName = "leftPassthrough"
    const val rightPassthroughName = "rightPassthrough"

    const val passthroughMinDegree = -45.0 // degrees
    const val passthroughMaxDegree = 180.0 // degrees

    @JvmField
    var passthroughPickUpAngle = -45.0 // degrees
    @JvmField
    var passthroughDepositAngle = 180.0 // degrees
    @JvmField
    var passthroughJunctionAngle = -35.0

    @JvmField
    var passthroughMaxVel = 0.0
    var passthroughMaxAccel = 0.0

    @JvmField
    var passthroughTimeTolerance = 0.2 // Seconds to wait after motion profile supposedly complete

    /**
     * Vision
     */
    const val webcamHeight = 2.0
    const val phoneCamHeight = 0.0



    /****************************************************
     * Commands                                         *
     ****************************************************/

    // Time in seconds the passthrough starts moving before the lift reaches its target height.
    @JvmField
    val poleDepositAnticipationTime = 0.5


    /****************************************************
     * Driver Controls                                  *
     ****************************************************/

    const val teleOpSetPointAdj = 1.0
    const val teleOpDepositAdj = 5.0
}