package com.neoqee.barcodescan

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BarcodeScanningActivity : AppCompatActivity() {

    private val options: BarcodeScannerOptions by lazy { configureBarcodeScanner() }
    private lateinit var surfaceView: SurfaceView
    private lateinit var viewFinder: PreviewView

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanning)

        surfaceView = findViewById(R.id.surfaceView)
        viewFinder = findViewById(R.id.viewFinder)

        cameraExecutor = Executors.newSingleThreadExecutor()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build()
                    imageCapture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    cameraProvider.unbindAll()
                    preview.setSurfaceProvider(viewFinder.surfaceProvider)
//                    cameraProvider.bindToLifecycle(
//                        this,
//                        cameraSelector,
//                        preview,
//                        imageCapture
//                    )
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(this),
                        { imageProxy: ImageProxy ->
                            val rotationDegree = imageProxy.imageInfo.rotationDegrees
                            val mediaImage = imageProxy.image
                            mediaImage?.let {
                                val image = InputImage.fromMediaImage(it, rotationDegree)
                                val scanner = BarcodeScanning.getClient(options)
                                val result = scanner.process(image)
                                    .addOnSuccessListener (cameraExecutor,{ barcodes ->
                                        Log.i("Neoqee", "success")
                                        for (barcode in barcodes) {
                                            val bounds = barcode.boundingBox
                                            val corners = barcode.cornerPoints
                                            val rawValue = barcode.rawValue
                                            Log.i("Neoqee", "rawValue -> ${rawValue.orEmpty()}")
                                        }
                                    })
                                    .addOnCompleteListener(cameraExecutor,{ task ->
                                        Log.i("Neoqee", "complete")
                                        imageProxy.close()
                                    })
                                    .addOnCanceledListener (cameraExecutor,{
                                        Log.i("Neoqee", "cancel")
                                    })
                                    .addOnFailureListener (cameraExecutor,{ e ->
                                        Log.i("Neoqee", "failure")
                                        Log.e("Neoqee", e.toString())
                                        e.printStackTrace()
                                    })
                            }
                        })
                    cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        imageAnalysis,
                        preview
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            ContextCompat.getMainExecutor(this)
        )


    }

    // 1. Configure the barcode scanner
    private fun configureBarcodeScanner(): BarcodeScannerOptions {
        return BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    }

    // 2. Prepare the input image
    private fun getInputImage(
        byteBuffer: ByteBuffer, imageWidth: Int,
        imageHeight: Int, rotationDegree: Int
    ): InputImage {
        return InputImage.fromByteBuffer(
            byteBuffer,
            imageWidth,
            imageHeight,
            rotationDegree,
            InputImage.IMAGE_FORMAT_NV21
        )
    }

}
