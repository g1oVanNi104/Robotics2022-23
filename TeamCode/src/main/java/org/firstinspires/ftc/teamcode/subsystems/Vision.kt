package org.firstinspires.ftc.teamcode.subsystems

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.RobotConfig.phoneCamHeight
import org.firstinspires.ftc.teamcode.RobotConfig.webcamHeight
import org.firstinspires.ftc.teamcode.teleops.AprilTagDemo
import org.firstinspires.ftc.teamcode.vision.AprilTagDetectionPipeline
import org.firstinspires.ftc.teamcode.vision.GeneralConePipeline
import org.firstinspires.ftc.teamcode.vision.PolePipeline
import org.opencv.core.Point
import org.openftc.apriltag.AprilTagDetection
import org.openftc.easyopencv.OpenCvCamera.AsyncCameraOpenListener
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvInternalCamera
import org.openftc.easyopencv.OpenCvPipeline
import java.lang.Thread.sleep


/**
 * Manages all the pipelines and cameras.
 */
class Vision(
    hwMap: HardwareMap
) : SubsystemBase() {

    val cameraMonitorViewId: Int = hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName())
    val webCam = OpenCvCameraFactory.getInstance().createWebcam(hwMap.get(WebcamName::class.java, "Webcam 1"), cameraMonitorViewId)
    val phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId)

    enum class RearPipeline(var pipeline: OpenCvPipeline) {
        APRIL_TAG(AprilTagDetectionPipeline(0.166, 578.272, 578.272, 402.145, 221.506)),
        POLE(PolePipeline(PolePipeline.DisplayMode.ALL_CONTOURS, 70.42, 43.3, webcamHeight))
    }

    val conePipeline = GeneralConePipeline(GeneralConePipeline.DisplayMode.ALL_CONTOURS, 67.0, 52.9, phoneCamHeight)

    init {
        name = "Vision Subsystem"

        // Open cameras asynchronously and load the pipelines
        phoneCam.openCameraDeviceAsync(object : AsyncCameraOpenListener {
            override fun onOpened() {

            }

            override fun onError(errorCode: Int) {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        })
    }



    /**
     * Starts streaming the front camera.
     */
    fun startStreamingFrontCamera() {
        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
    }

    /**
     * Stops streaming the front camera.
     */
    fun stopStreamingFrontCamera() {
        phoneCam.stopStreaming()

    }

    /**
     * Starts streaming the rear camera.
     */
    fun startStreamingRearCamera() {
        webCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)

    }

    /**
     * Stops streaming the rear camera.
     */
    fun stopStreamingRearCamera() {
        webCam.stopStreaming()

    }

    /**
     * Sets the rear pipeline.
     * @param pipeline     From the [RearPipeline] pipeline options.
     */
    fun setRearPipeline(pipeline: RearPipeline) {
        webCam.setPipeline(pipeline.pipeline)
    }

    /**
     * @return Coordinate of the closest cone in the robot tangent space.
     */
    fun getConeAngle(): Double? {
        val results = conePipeline.singleConeResults
        if (results.isNotEmpty()) {
            var closestResult = results[0]
            var smallestDistance = closestResult.distance
            for (result in results) {
                //iterate through find closest result
                if (result.distance<smallestDistance){
                    smallestDistance=result.distance
                    closestResult=result
                }
            }
            return closestResult.angle
        }
        return null
    }

    fun getPoleAngle(): Double? {
        val results = (RearPipeline.POLE.pipeline as PolePipeline).poleResults
        if (results.isNotEmpty()) {
            var closestResult = results[0]
            var smallestDistance = closestResult.distance
            for (result in results) {
                //iterate through find closest result
                if (result.distance<smallestDistance){
                    smallestDistance=result.distance
                    closestResult=result
                }
            }
            return closestResult.angle
        }
        return null
    }

    //enum

    fun getParkingStation(): Double? {
//        val camera = webCam
//        val aprilTagDetectionPipeline = RearPipeline.APRIL_TAG.pipeline
//            val detections: ArrayList<AprilTagDetection> = aprilTagDetectionPipeline.getDetectionsUpdate()
//
//            // If there's been a new frame...
//            if (detections != null) {
//                telemetry.addData("FPS", camera.getFps())
//                telemetry.addData("Overhead ms", camera.getOverheadTimeMs())
//                telemetry.addData("Pipeline ms", camera.getPipelineTimeMs())
//
//                // If we don't see any tags
//                if (detections.size == 0) {
//                    numFramesWithoutDetection++
//
//                    // If we haven't seen a tag for a few frames, lower the decimation
//                    // so we can hopefully pick one up if we're e.g. far back
//                    if (numFramesWithoutDetection >= THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION) {
//                        aprilTagDetectionPipeline.setDecimation(DECIMATION_LOW)
//                    }
//                } else {
//                    numFramesWithoutDetection = 0
//
//                    // If the target is within 1 meter, turn on high decimation to
//                    // increase the frame rate
//                    if (detections[0].pose.z < THRESHOLD_HIGH_DECIMATION_RANGE_METERS) {
//                        aprilTagDetectionPipeline.setDecimation(DECIMATION_HIGH)
//                    }
//                    for (detection in detections) {
//                        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id))
//                        telemetry.addLine(
//                            String.format(
//                                "Translation X: %.2f feet",
//                                detection.pose.x * AprilTagDemo.FEET_PER_METER
//                            )
//                        )
//                        telemetry.addLine(
//                            String.format(
//                                "Translation Y: %.2f feet",
//                                detection.pose.y * AprilTagDemo.FEET_PER_METER
//                            )
//                        )
//                        telemetry.addLine(
//                            String.format(
//                                "Translation Z: %.2f feet",
//                                detection.pose.z * AprilTagDemo.FEET_PER_METER
//                            )
//                        )
//                        telemetry.addLine(
//                            String.format(
//                                "Rotation Yaw: %.2f degrees",
//                                Math.toDegrees(detection.pose.yaw)
//                            )
//                        )
//                        telemetry.addLine(
//                            String.format(
//                                "Rotation Pitch: %.2f degrees",
//                                Math.toDegrees(detection.pose.pitch)
//                            )
//                        )
//                        telemetry.addLine(
//                            String.format(
//                                "Rotation Roll: %.2f degrees",
//                                Math.toDegrees(detection.pose.roll)
//                            )
//                        )
//                    }
//                }
//                telemetry.update()
//            }
//            sleep(20)
//        }
//
//
//

        return 0.0
    }

    /**
     * @return List of pixel info for landmarks form pipeline
     */
    fun getLandmarkInfo(): List<Point> {
        TODO("Implement")
    }

    /**
     * @return Returns global position from pixel space
     */
    fun pixelToGlobalPosition(pixel: Point, realHeight: Double, position: Pose2d) : Vector2d {
        TODO("Implement")
    }

}