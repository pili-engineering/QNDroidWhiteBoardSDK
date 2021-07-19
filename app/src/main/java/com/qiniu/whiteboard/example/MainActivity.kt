package com.qiniu.whiteboard.example

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import com.qiniu.droid.whiteboard.model.JoinConfig
import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import splitties.activities.start

class MainActivity : AppCompatActivity() {

    private val QRCODE_RESULT_REQUEST_CODE = 1

    private var mRoomTokenEditText: EditText? = null

    var token1 =
        "QxZugR8TAhI38AiJ_cptTl3RbzLyca3t-AAiH-Hh:GwzC49F8kBR4uyfSVYNODT2dPGM=:eyJhcHBJZCI6ImQ4ZHJlOHcxcCIsImV4cGlyZUF0IjoxNjI3NjU1MzgyLCJwZXJtaXNzaW9uIjoidXNlciIsInJvb21OYW1lIjoicXdlMTIiLCJ1c2VySWQiOiIxMjMifQ=="
    var token2 =
        "QxZugR8TAhI38AiJ_cptTl3RbzLyca3t-AAiH-Hh:Tj2or5C0y3suQ_WXqnZUB7BtrXU=:eyJhcHBJZCI6ImQ4ZHJlOHcxcCIsImV4cGlyZUF0IjoxNjI3NzQxODE5LCJwZXJtaXNzaW9uIjoidXNlciIsInJvb21OYW1lIjoicXdlMTIiLCJ1c2VySWQiOiJzeGRhc2QifQ=="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRoomTokenEditText = findViewById(R.id.room_token_edit_text)
        findViewById<View>(R.id.buttonToken1).setOnClickListener { v ->
            mRoomTokenEditText?.setText(token1)
            joinRoom(v)
        }
        findViewById<View>(R.id.buttonToken2).setOnClickListener { v ->
            mRoomTokenEditText?.setText(token2)
            joinRoom(v)
        }
    }

    fun joinRoom(view: View?) {

        val token = mRoomTokenEditText?.text?.toString()?:""
        if(TextUtils.isEmpty(token)){
            return
        }
        // 加入房间
        val params = JoinConfig(
            mRoomTokenEditText?.text?.toString()?:""
        ).apply {
            widthHeightThan=0.5
        }
        start<RoomActivity> {
            putExtra(RoomActivity.ROOM_DATA_TAG, params)
        }
    }

    fun clickToScanQRCode(view: View?) {
        // 扫码也用到了相机权限
        if (!isPermissionOK()) {
            Toast.makeText(this, "Some permissions is not approved !!!", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, CaptureActivity::class.java)
        startActivityForResult(intent, QRCODE_RESULT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            // 处理扫描结果
            if (null != data) {
                val bundle = data.extras ?: return
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    val result = bundle.getString(CodeUtils.RESULT_STRING)
                    mRoomTokenEditText!!.setText(result)
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun isPermissionOK(): Boolean {
        val checker = PermissionChecker(this)
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checker.checkPermission()
    }
}