package org.firstinspires.ftc.teamcode.vision.modules

import org.firstinspires.ftc.teamcode.vision.modulelib.AbstractPipelineModule
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class YCrCbConverter(private val inputFrame: AbstractPipelineModule<Mat>) : AbstractPipelineModule<Mat>() {

    private lateinit var output: Mat

    init {
        addParentModules(inputFrame)
    }


    override fun init(input: Mat) {
        output = input.clone()
    }

    override fun processFrameForCache(input: Mat): Mat {
        Imgproc.cvtColor(inputFrame.processFrame(input), output, Imgproc.COLOR_RGB2YCrCb)
        return input
    }

}