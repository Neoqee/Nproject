package com.neoqee.viewmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class WeiqiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weiqi)
        val weiqiView = findViewById<WeiqiView>(R.id.weiqiView)
        weiqiView.setOnClickListener {
            Log.i("Neoqee","dianji")
            weiqiView.startAnimation()
        }
    }
}
