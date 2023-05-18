package com.example.projectmm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView


class ScanQRActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null
    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.activity_scan_qr)
        val contentFrame = findViewById<ViewGroup>(R.id.content_frame)
        mScannerView = ZXingScannerView(this)
        contentFrame.addView(mScannerView)
    }

    override fun onResume() {
        super.onResume()
        mScannerView?.setResultHandler(this)
        mScannerView?.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView?.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        Log.d("QRCode1", rawResult.text)
        Log.d("QRCode2", rawResult.barcodeFormat.toString())

        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("movie_id", rawResult.text.toInt())
        startActivity(intent)

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        val handler = Handler()
        handler.postDelayed(
            { mScannerView!!.resumeCameraPreview(this@ScanQRActivity) },
            2000
        )
    }
}