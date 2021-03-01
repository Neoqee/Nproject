package com.neoqee.viewmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TaijiActivity : AppCompatActivity() {

    private lateinit var taijiView: TaijiView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taiji)

        taijiView = findViewById(R.id.taijiView)

        taijiView.createAnimation()

    }

    override fun onStop() {
        super.onStop()
        taijiView.stopAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        taijiView.cleanAnimation()
    }

}
