// Created by qiniu on 2021/4/16.

package com.qiniu.whiteboard.example
import android.app.Application
import com.qiniu.droid.whiteboard.QNWhiteBoard
import com.tencent.bugly.crashreport.CrashReport
import com.uuzuche.lib_zxing.activity.ZXingLibrary

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        // 初始化使用的第三方二维码扫描库，与 QNRTC 无关，请忽略

        // 初始化使用的第三方二维码扫描库，与 QNRTC 无关，请忽略
        ZXingLibrary.initDisplayOpinion(applicationContext)
        QNWhiteBoard.init(this, true)
        QNWhiteBoard.setDefaultInputMode(NormalPenStyle().inputConfig)
        CrashReport.initCrashReport(this, "be697c06e2", true);
    }
}