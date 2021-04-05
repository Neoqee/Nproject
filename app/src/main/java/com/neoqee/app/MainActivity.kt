package com.neoqee.app

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.neoqee.barcodescan.BarcodeScanningActivity
import com.neoqee.viewmodule.CustomViewActivity
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

        Intent(this,CustomViewActivity::class.java).apply {
            startActivity(this)
        }

        // 使用 Intent 过滤器
        // 从 Intent 获取代表所连接设备的 UsbDevice
//        val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)


        // 枚举设备
        val manager = getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList = manager.deviceList
        Log.i("Neoqee","deviceList -> $deviceList")

        val accessoryList = manager.accessoryList
        Log.i("Neoqee","accessoryList -> $accessoryList")

//        val device = deviceList.get("deviceName")
//        deviceList.values.forEach { device ->
//            //your code
//        }

//        manager.requestPermission()

//        val permissionIntent = PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), 0)
//        val filter = IntentFilter(ACTION_USB_PERMISSION)
//        registerReceiver(usbReceiver, filter)


    }

    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

    private val usbReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_USB_PERMISSION == intent.action) {
                synchronized(this) {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        device?.apply {
                            //call method to set up device communication
                            Log.d("Neoqee","已连接设备：${device.deviceName}")
                        }
                    } else {
                        Log.d("Neoqee", "permission denied for device $device")
                    }
                }
            }
        }
    }


}
