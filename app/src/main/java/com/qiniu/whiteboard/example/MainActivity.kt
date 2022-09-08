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

    var token1 ="QxZugR8TAhI38AiJ_cptTl3RbzLyca3t-AAiH-Hh:SOr3WiFhHjROvoj9hTv6Ym6Lo4Q=:eyJhcHBJZCI6ImZuZjB2cjZnbiIsImV4cGlyZUF0IjoxNjk1NDM2OTA0LCJwZXJtaXNzaW9uIjoidXNlciIsInJvb21OYW1lIjoiMTIzNCIsInVzZXJJZCI6ImFzZGFzZGFzZHNhZGFzIn0="
    var token2 ="QxZugR8TAhI38AiJ_cptTl3RbzLyca3t-AAiH-Hh:Ikb5MSy3pclGs7H_gCfeIbAmDVk=:eyJhcHBJZCI6ImZuZjB2cjZnbiIsImV4cGlyZUF0IjoxNjk1NDM2OTA0LCJwZXJtaXNzaW9uIjoidXNlciIsInJvb21OYW1lIjoiMTIzNCIsInVzZXJJZCI6ImFzd3J3ZXJldyJ9"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRoomTokenEditText = findViewById(R.id.room_token_edit_text)
        findViewById<View>(R.id.buttonToken1).setOnClickListener { v ->
            mRoomTokenEditText?.setText(token1)
            joinRoom(v,  0.5)
        }
        findViewById<View>(R.id.buttonToken2).setOnClickListener { v ->
            mRoomTokenEditText?.setText(token1)
            joinRoom(v, 0.6)
        }
        findViewById<View>(R.id.btnJoin).setOnClickListener { v ->
            joinRoom(v, 0.5)
        }
    }

    fun joinRoom(view: View?,  widthHeight: Double) {
        val token = mRoomTokenEditText?.text?.toString() ?: ""
        if (TextUtils.isEmpty(token)) {
            return
        }
        // 加入房间
        val params = JoinConfig(
            mRoomTokenEditText?.text?.toString() ?: ""
        ).apply {
            title = findViewById<EditText>(R.id.room_name_edit_text).text.toString()
            widthHeightThan = widthHeight
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
