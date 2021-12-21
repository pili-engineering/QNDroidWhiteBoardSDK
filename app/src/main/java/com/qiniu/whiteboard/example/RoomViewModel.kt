package com.qiniu.whiteboard.example

import android.app.Application
import com.qiniu.droid.whiteboard.QNWhiteBoard
import com.qiniu.droid.whiteboard.listener.QNAutoRemoveWhiteBoardListener
import com.qiniu.droid.whiteboard.type.BoardStatus
import com.qiniu.droid.whiteboard.type.WidgetType


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.qiniu.droid.whiteboard.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 房间功能
 *
 * @author qiniu
 * @version 1.0 2018/10/18
 * @since 1.0 2018/10/18
 **/
class RoomViewModel(val app: Application) : AndroidViewModel(app) {

    /**
     * 当前房间状态
     */
    val roomStatus = MutableLiveData( QNWhiteBoard.getStatus()).apply {
         QNWhiteBoard.addListener(object :
             QNAutoRemoveWhiteBoardListener {
            override fun onBoardStatusChanged(status: BoardStatus) {
                Log.d(TAG, "onBoardStatusChanged $status")
                value = status
            }
        })
    }

    /**
     * 白板宽高比
     */
    val whiteBoardRatio = MutableLiveData("2048:1440").apply {
         QNWhiteBoard.addListener(object :
             QNAutoRemoveWhiteBoardListener {
            override fun onBoardSizeChanged(viewport: WhiteBoardViewport) {
                Log.d(TAG, "onBoardSizeChanged")
                value = "${viewport.size.displayWidth}:${viewport.size.displayHeight}"
            }
        })
    }

    /**
     * 页信息列表
     */
    val pageList = MutableLiveData<List<WhiteBoardPage>>().apply {
         QNWhiteBoard.addListener(object :
             QNAutoRemoveWhiteBoardListener {
            override fun onBoardPageList(list: List<WhiteBoardPage>) {
                Log.d(TAG, "onBoardPageList count:${list.size}")
                value = list
            }

            override fun onBoardPageInfoChanged(page: WhiteBoardPage) {
                Log.d(TAG, "onBoardPageInfoChanged ${page.pageNumber}")
                value =  QNWhiteBoard.getPageList()
            }
        })
    }

    /**
     * 当前白板页
     */
    val currentPage = MutableLiveData<WhiteBoardPage>().apply {
         QNWhiteBoard.addListener(object :
             QNAutoRemoveWhiteBoardListener {
            override fun onCurrentBoardPageChanged(page: WhiteBoardPage) {
                Log.d(TAG, "onCurrentBoardPageChanged ${page.pageNumber}")
                value = page
            }
        })
    }

    /**
     * 当前在线用户列表
     */
    val userList = MutableLiveData<List<RoomMember>>().apply {
         QNWhiteBoard.addListener(object :
             QNAutoRemoveWhiteBoardListener {
            override fun onUserList(users: List<RoomMember>) {
                Log.d(TAG, "onUserList")
                value = users
            }

            override fun onUserJoin(user: RoomMember) {
                Log.d(TAG, "onUserJoin $user")
                value =  QNWhiteBoard.getUsers()
            }

            override fun onUserLeave(user: RoomMember) {
                Log.d(TAG, "onUserLeave $user")
                value =  QNWhiteBoard.getUsers()
            }
        })
    }

    /**
     * 当前是否可还原笔迹
     */
    val canRecovery = MutableLiveData<Boolean>().apply {
         QNWhiteBoard.addListener(object :
             QNAutoRemoveWhiteBoardListener {
            override fun onRecoveryStateChanged(isEmpty: Boolean) {
                Log.d(TAG, "onRecoveryStateChanged")
                value = !isEmpty
            }
        })
    }

    /**
     * 当前激活的widget，仅保留文件和图片类型
     */
    val activeWidget = MutableLiveData<ActiveWidgetInfo?>().apply {
         QNWhiteBoard.addListener(object :
             QNAutoRemoveWhiteBoardListener {
            override fun onWidgetActive(info: ActiveWidgetInfo?) {
                Log.d(TAG, "onWidgetActive")
                value = info?.takeIf { it.type == WidgetType.FILE || it.type == WidgetType.IMAGE }
            }
        })
    }

    /**
     * 当前白板背景主题
     */
    val theme = MutableLiveData(BoardTheme.white()).apply {
         QNWhiteBoard.addListener(object :
             QNAutoRemoveWhiteBoardListener {
            override fun onBackgroundColorChanged(backgroundColor: Int) {
                Log.d(TAG, "onBackgroundColorChanged")
                value = BoardTheme(backgroundColor)
            }
        })
    }

    /**
     * 页列表是否可见
     */
    val pageListVisible = MutableLiveData(false)

    /**
     * 成员列表是否可见
     */
    val memberListVisible = MutableLiveData(false)

    /**
     * 普通笔配置
     */
    val normalPenStyle = NormalPenStyle()

    /**
     * 马克笔配置
     */
    val markPenStyle = MarkPenStyle()

    /**
     * 橡皮配置
     */
    val eraserStyle = EraserStyle()

    /**
     * 激光笔配置
     */
    val laserStyle = LaserStyle()

    /**
     * 几何图形样式
     */
    val geometryStyle = GeometryStyle()

    /**
     * 当前输入模式
     */
    var currentInputType = MutableLiveData(InputType.NORMAL)

    /**
     * 临时图片路径（拍照用）
     */
    var imageTempPath = ""

    init {

         QNWhiteBoard.addListener(object :
             QNAutoRemoveWhiteBoardListener {
            override fun onJoinSuccess(room: Room, me: RoomMember) {
                Log.d(TAG, "onJoinSuccess")
            }

            override fun onJoinFailed(errorCode: Int,msg:String) {
                Log.d(TAG, "onJoinFailed $errorCode")
                GlobalScope.launch (Dispatchers.Main){
                    Toast.makeText(app,"加入房间失败 ${errorCode} ${msg}",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onReconnecting(times: Int) {
                Log.d(TAG, "onReconnecting $times")
            }

            override fun onReconnected() {
                Log.d(TAG, "onReconnected")
            }

            override fun onDisconnected() {
                Log.d(TAG, "onDisconnected")
            }

            override fun onFilePageChanged(info: ActiveWidgetInfo) {
                Log.d(TAG, "onFilePageChanged $info")
            }

            override fun onWidgetActionEvent(event: WidgetActionEvent) {
                Log.d(TAG, "onWidgetActionEvent $event")
            }

             override fun onPageCleaned(pageId: String) {
                 Log.d(TAG, "onPageCleaned ${pageId}")
             }

             override fun onFileScrolled(info: WidgetScrollInfo) {
                 Log.d(TAG, "onFileScrolled ${info.scrollToBottom} ${info.scrollToTop}   ")
             }
        })
    }

    /**
     * 改变输入模式
     *
     * @param inputType 新的输入模式
     */
    fun changeInputType(inputType: InputType) {
        currentInputType.value = inputType
        val config = when (inputType) {
            InputType.NORMAL -> normalPenStyle.inputConfig
            InputType.MARK -> markPenStyle.inputConfig
            InputType.LASER -> laserStyle.inputConfig
            InputType.ERASE -> eraserStyle.inputConfig
            InputType.SELECT -> InputConfig.select()
            InputType.GEOMETRY -> geometryStyle.inputConfig
        }
         QNWhiteBoard.setInputMode(config)
    }

    override fun onCleared() {
         QNWhiteBoard.leaveRoom()
    }

    companion object {
        private const val TAG = "whiteBord"
    }
}