package com.neoqee.viewmodule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CustomViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)

//        findViewById<Button>(R.id.taijiBtn).setOnClickListener {
//            Intent(this,TaijiActivity::class.java).apply {
//                startActivity(this)
//            }
//        }
//
//        findViewById<Button>(R.id.weiqiBtn).setOnClickListener {
//            Intent(this,WeiqiActivity::class.java).apply {
//                startActivity(this)
//            }
//        }

    }
}
