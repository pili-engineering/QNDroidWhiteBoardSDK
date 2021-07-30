package com.qiniu.whiteboard.example

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import com.qiniu.droid.rtc.QNRTCEventListener
import com.qiniu.droid.rtc.QNClientEventListener
import com.qiniu.droid.rtc.QNRTCClient
import com.qiniu.droid.rtc.model.QNAudioDevice
import com.qiniu.droid.rtc.QNConnectionState
import com.qiniu.droid.rtc.QNTrack
import com.qiniu.droid.rtc.QNCustomMessage
import com.qiniu.droid.rtc.QNRTCSetting
import com.qiniu.droid.rtc.QNVideoFormat
import com.qiniu.droid.rtc.QNRTC
import com.qiniu.droid.rtc.QNJoinResultCallback

class RtcViewModel(val app: Application) : AndroidViewModel(app), QNRTCEventListener,
    QNClientEventListener, LifecycleObserver {

    private var mClient: QNRTCClient?=null

    fun join(mRoomToken:String){
        val mSetting = QNRTCSetting()

        // 配置默认摄像头 ID，此处配置为前置摄像头
        mSetting.cameraID = QNRTCSetting.QNCameraFacing.FRONT

        // 相机预览分辨率、帧率配置为 640x480、20fps
        mSetting.cameraPreviewFormat = QNVideoFormat(640, 480, 30)

        // 初始化 QNRTC
        QNRTC.init(app, mSetting, this)

        // 创建 QNRTCClient
        mClient = QNRTC.createClient(this)
        mClient?.join(mRoomToken, object : QNJoinResultCallback {
            override fun onJoined() {
                Log.d("rtc","onJoined")
            }
            override fun onError(errorCode: Int, errorMessage: String?) {
                Log.d("rtc","onJoin Error")
            }
        })
    }

    override fun onCleared() {
        mClient?.leave()
    }

    override fun onPlaybackDeviceChanged(qnAudioDevice: QNAudioDevice) {}
    override fun onCameraError(i: Int, s: String) {}
    override fun onConnectionStateChanged(qnConnectionState: QNConnectionState) {}
    override fun onLeft() {}
    override fun onUserJoined(s: String, s1: String) {}
    override fun onUserReconnecting(s: String) {}
    override fun onUserReconnected(s: String) {}
    override fun onUserLeft(s: String) {}
    override fun onUserPublished(s: String, list: List<QNTrack>) {}
    override fun onUserUnpublished(s: String, list: List<QNTrack>) {}
    override fun onSubscribed(s: String, list: List<QNTrack>) {}
    override fun onMessageReceived(qnCustomMessage: QNCustomMessage) {}
}