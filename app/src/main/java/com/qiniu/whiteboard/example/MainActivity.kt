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
        "QxZugR8TAhI38AiJ_cptTl3RbzLyca3t-AAiH-Hh:srRQTXJzBogXrXVMq-FUSOdER3c=:eyJhcHBJZCI6ImQ4ZHJlOHcxcCIsImV4cGlyZUF0IjoxNjYxNTcxNzAzLCJwZXJtaXNzaW9uIjoidXNlciIsInJvb21OYW1lIjoiMTIzIiwidXNlcklkIjoiMTIzMzMifQ=="

    //debug "QxZugR8TAhI38AiJ_cptTl3RbzLyca3t-AAiH-Hh:HX9jtooyqlqyq0iN9ERM2H-i1S0=:eyJhcHBJZCI6ImQ4ZHJlOHcxcCIsImV4cGlyZUF0IjoxNjI3NzA0NDA0LCJwZXJtaXNzaW9uIjoidXNlciIsInJvb21OYW1lIjoiMTIzNCIsInVzZXJJZCI6ImFkc2FkYWRhZCJ9"
    var token2 =
        "QxZugR8TAhI38AiJ_cptTl3RbzLyca3t-AAiH-Hh:yvaLyhyuhN54013ESzodkpxJ0BI=:eyJhcHBJZCI6ImQ4ZHJlOHcxcCIsImV4cGlyZUF0IjoxNjYxNTcxNzAzLCJwZXJtaXNzaW9uIjoidXNlciIsInJvb21OYW1lIjoiMTIzIiwidXNlcklkIjoiMTJlcWUifQ=="
    // "QxZugR8TAhI38AiJ_cptTl3RbzLyca3t-AAiH-Hh:9ifJL3qnKSTAwuA1iPmDuOgnkRY=:eyJhcHBJZCI6ImQ4ZHJlOHcxcCIsImV4cGlyZUF0IjoxNjI3NzA0NDYwLCJwZXJtaXNzaW9uIjoidXNlciIsInJvb21OYW1lIjoiMTIzNCIsInVzZXJJZCI6ImFkc2Fkc2Fkc2EifQ=="

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
        // ????????????
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
        // ??????????????????????????????
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
            // ??????????????????
            if (null != data) {
                val bundle = data.extras ?: return
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    val result = bundle.getString(CodeUtils.RESULT_STRING)
                    mRoomTokenEditText!!.setText(result)
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "?????????????????????", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun isPermissionOK(): Boolean {
        val checker = PermissionChecker(this)
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checker.checkPermission()
    }
}
