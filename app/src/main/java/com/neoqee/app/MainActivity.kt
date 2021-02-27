package com.neoqee.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.neoqee.barcodescan.BarcodeScanningActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.barcode).setOnClickListener {
            Intent(this,BarcodeScanningActivity::class.java).apply {
                startActivity(this)
            }
        }

    }
}
