package com.neoqee.app

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import dagger.hilt.android.HiltAndroidApp

// 添加hilt的app注解 所有使用Hilt的都需要添加 作为依赖的入口点
@HiltAndroidApp
class App : Application(), CameraXConfig.Provider {

    override fun onCreate() {
        super.onCreate()
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }

}