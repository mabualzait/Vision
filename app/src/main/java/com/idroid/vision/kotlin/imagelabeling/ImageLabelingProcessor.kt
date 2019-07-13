package com.idroid.vision.kotlin.imagelabeling

import android.graphics.Bitmap
import android.util.Log
import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.idroid.vision.common.CameraImageGraphic
import com.idroid.vision.common.FrameMetadata
import com.idroid.vision.common.GraphicOverlay
import com.idroid.vision.kotlin.VisionProcessorBase
import java.io.IOException

/** Custom Image Classifier Demo.  */
class ImageLabelingProcessor(tvText: TextView) : VisionProcessorBase<List<FirebaseVisionImageLabel>>() {

    private val detector: FirebaseVisionImageLabeler = FirebaseVision.getInstance().onDeviceImageLabeler
    private val textView = tvText
    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Text Detector: $e")
        }
    }

    override fun detectInImage(image: FirebaseVisionImage): Task<List<FirebaseVisionImageLabel>> {
        return detector.processImage(image)
    }

    override fun onSuccess(
            originalCameraImage: Bitmap?,
            results: List<FirebaseVisionImageLabel>,
            frameMetadata: FrameMetadata,
            graphicOverlay: GraphicOverlay
    ) {
          graphicOverlay.clear()
          originalCameraImage.let { image ->
              val imageGraphic = CameraImageGraphic(graphicOverlay, image)
              graphicOverlay.add(imageGraphic)
          }
        textView.text = ""
        var i: Int = 0
        for (label in results) {
            if(i==2){break;}
            textView.append(" " + label.text)
            i++;
        }
    }

    override fun onFailure(e: Exception) {
        Log.w(TAG, "Label detection failed.$e")
    }

    companion object {
        private const val TAG = "ImageLabelingProcessor"
    }
}
