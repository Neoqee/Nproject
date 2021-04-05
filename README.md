1. 集成mlkit服务进行扫码
2. 添加仿写的自定义view





# 继承MLkit的文字识别

文本识别API使用一个必须下载的非捆绑模型。你可以在app安装的时候或者第一次打开的时候进行下载。



## 开始之前

1. 在你的项目集build.gradle文件中，确保包含了google的maven仓库在buildscript和allProjects部分中。
2. 添加ML Kit Android库的依赖到模块的app级gradle文件中，

添加依赖

```groovy
implementation 'com.google.android.gms:play-services-mlkit-text-recognition:16.1.3'
```

3. **可选且推荐**：你可以配置app在从商店下载时自动下载ML 模型到设备上。添加以下声明到你的AndroidManifest.xml文件中

```xml
<application ...>
  ...
  <meta-data
      android:name="com.google.mlkit.vision.DEPENDENCIES"
      android:value="ocr" />
  <!-- To use multiple models: android:value="ocr,model2,model3" -->
</application>
```



