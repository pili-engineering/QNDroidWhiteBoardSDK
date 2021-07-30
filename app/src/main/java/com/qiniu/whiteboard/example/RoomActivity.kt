package com.qiniu.whiteboard.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer



import splitties.alertdialog.appcompat.alertDialog
import splitties.dimensions.dip
import java.io.File
import com.qiniu.droid.whiteboard.QNWhiteBoard
import com.qiniu.whiteboard.example.databinding.ActivityRoomBinding
import com.qiniu.droid.whiteboard.model.FileConfig
import com.qiniu.droid.whiteboard.model.JoinConfig
import com.qiniu.droid.whiteboard.type.WidgetType

/**
 * 白板房间
 *
 * @author qiniu
 * @version 1.0 2018/3/15
 * @since 1.0 2018/3/15
 **/
class RoomActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "RoomActivity"

        /**
         * 传递房间data
         */
        const val ROOM_DATA_TAG = "room_data_tag"
    }

    /**
     * 视图模型
     */
    private val viewModel by viewModels<RoomViewModel>()

    private val mRtcActivity  by viewModels<RtcViewModel>()
    /**
     * 视图绑定
     */
    private val binding by lazy {
        ActivityRoomBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.pageList.adapter = PageListAdapter(this, viewModel.currentPage).apply {
            viewModel.pageList.observe(this@RoomActivity, Observer {
                submitList(it)
            }
            )
        }

        binding.userList.adapter = UserListAdapter().apply {
            viewModel.userList.observe(this@RoomActivity, Observer {
                submitList(it)
            })
        }

        val joinConfig = intent.getParcelableExtra<JoinConfig>(ROOM_DATA_TAG)!!
        QNWhiteBoard.joinRoom(joinConfig)
        mRtcActivity.join(joinConfig.token)
    }

    override fun onContentChanged() {

        binding.insertFile.setOnClickListener {
            insertFilePopupMenu.show()
        }

        binding.select.setOnClickListener {
            viewModel.changeInputType(InputType.SELECT)
        }

        binding.restore.setOnClickListener {
            QNWhiteBoard.recover()
        }

        binding.pageMenu.setOnClickListener {
            viewModel.pageListVisible.value = !viewModel.pageListVisible.value!!
        }

        binding.prePage.setOnClickListener {
            QNWhiteBoard.preBoardPage()
        }

        binding.nextPage.setOnClickListener {
            QNWhiteBoard.nextBoardPage()
        }

        binding.newPage.setOnClickListener {
            QNWhiteBoard.newBoardPage()
        }

        binding.clearBtn.setOnClickListener{
            QNWhiteBoard.cleanBoardPage(QNWhiteBoard.getCurrentPage()?.pageId)
        }

        binding.screenshots.setOnClickListener {
            QNWhiteBoard.screenshots {
                if (it == null) {
                    return@screenshots
                }
                Log.i(TAG, "screenshots ${it.width}x${it.height}")
                runOnUiThread {
                    alertDialog {
                        setView(ImageView(this@RoomActivity).apply {
                            setImageBitmap(it)
                            scaleType = ImageView.ScaleType.CENTER_INSIDE
                        })
                    }.show()
                }
            }
        }

        binding.deleteFile.setOnClickListener {
            viewModel.activeWidget.value?.let {
                QNWhiteBoard.deleteFile(it.id)
            }
        }

        binding.preFilePage.setOnClickListener {
            viewModel.activeWidget.value?.takeIf { it.type == WidgetType.FILE && it.currentPageNumber > 1 }
                ?.let {
                    QNWhiteBoard.jumpFilePage(it.id, it.currentPageNumber - 1)
                }
        }

        binding.nextFilePage.setOnClickListener {
            viewModel.activeWidget.value?.takeIf { it.type == WidgetType.FILE && it.currentPageNumber < it.pageCount }
                ?.let {
                    QNWhiteBoard.jumpFilePage(it.id, it.currentPageNumber + 1)
                }
        }

        binding.pen.setOnClickListener {
            if (viewModel.currentInputType.value != InputType.NORMAL) {
                viewModel.changeInputType(InputType.NORMAL)
            } else {
                normalPenWindow.show(it)
            }
        }

        binding.mark.setOnClickListener {
            if (viewModel.currentInputType.value != InputType.MARK) {
                viewModel.changeInputType(InputType.MARK)
            } else {
                markPenWindow.show(it)
            }
        }

        binding.eraser.setOnClickListener {
            if (viewModel.currentInputType.value != InputType.ERASE) {
                viewModel.changeInputType(InputType.ERASE)
            } else {
                eraserWindow.show(it)
            }
        }

        binding.laser.setOnClickListener {
            if (viewModel.currentInputType.value != InputType.LASER) {
                viewModel.changeInputType(InputType.LASER)
            } else {
                laserWindow.show(it)
            }
        }

        binding.geometry.setOnClickListener {
            if (viewModel.currentInputType.value != InputType.GEOMETRY) {
                viewModel.changeInputType(InputType.GEOMETRY)
            } else {
                geometryWindow.show(it)
            }
        }

        binding.theme.setOnClickListener {
            themeWindow.popupWindow.showAtLocation(
                it.rootView,
                Gravity.END or Gravity.BOTTOM,
                0,
                dip(56)
            )
        }

        binding.members.setOnClickListener {
            viewModel.memberListVisible.value = !viewModel.memberListVisible.value!!
        }
    }

    /**
     * 创建普通笔颜色选择面板
     */
    private val normalPenWindow by lazy {
        PalettePopup(this).apply {
            addColorSelection(NormalPenStyle.colors, viewModel.normalPenStyle.colorIndex) {
                viewModel.normalPenStyle.colorIndex = it
            }
            addSizeSelection(NormalPenStyle.sizes, viewModel.normalPenStyle.sizeIndex) {
                viewModel.normalPenStyle.sizeIndex = it
            }
        }
    }

    /**
     * 创建马克笔颜色选择面板
     */
    private val markPenWindow by lazy {
        PalettePopup(this).apply {
            addColorSelection(MarkPenStyle.colors, viewModel.markPenStyle.colorIndex) {
                viewModel.markPenStyle.colorIndex = it
            }
            addSizeSelection(MarkPenStyle.sizes, viewModel.markPenStyle.sizeIndex) {
                viewModel.markPenStyle.sizeIndex = it
            }
        }
    }

    /**
     * 创建橡皮大小选择面板
     */
    private val eraserWindow by lazy {
        PalettePopup(this).apply {
            addSizeSelection(EraserStyle.sizes, viewModel.eraserStyle.sizeIndex) {
                viewModel.eraserStyle.sizeIndex = it
            }
        }
    }

    /**
     * 创建激光笔图形选择面板
     */
    private val laserWindow by lazy {
        PalettePopup(this).apply {
            addIconSelection(LaserStyle.icons.keys, viewModel.laserStyle.iconKey) {
                viewModel.laserStyle.iconKey = it
            }
        }
    }

    /**
     * 创建几何图形选择面板
     */
    private val geometryWindow by lazy {
        PalettePopup(this).apply {
            addIconSelection(GeometryStyle.icons.keys, viewModel.geometryStyle.iconKey) {
                viewModel.geometryStyle.iconKey = it
            }
            addColorSelection(GeometryStyle.colors, viewModel.geometryStyle.colorIndex) {
                viewModel.geometryStyle.colorIndex = it
            }
            addSizeSelection(GeometryStyle.sizes, viewModel.geometryStyle.sizeIndex) {
                viewModel.geometryStyle.sizeIndex = it
            }
        }
    }

    /**
     * 创建背景主题颜色选择面板
     */
    private val themeWindow by lazy {
        PalettePopup(this).apply {
            val colors = intArrayOf(
                BoardThemeType.WHITE.color(),
                BoardThemeType.BLACK.color(),
                BoardThemeType.GREEN.color(),
                BoardThemeType.TRANSLUCENT.color(),
            )

            addColorSelection(colors, viewModel.theme.value!!.themeType.ordinal) {
                QNWhiteBoard.setBackgroundColor(colors[it])
            }
        }
    }

    /**
     * 插入文件/图片的选项弹窗
     */
    private val insertFilePopupMenu by lazy {
        PopupMenu(this, binding.insertFile).apply {
            inflate(R.menu.insert_file_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.camera -> openCamera()
                    R.id.gallery -> openGallery.launch("image/*")
                    R.id.file -> openFile.launch("*/*")
                }
                true
            }
        }
    }

    /**
     * 照相
     */
    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.imageTempPath = FileUtil.createImagePath(this)
            takePicture.launch(FileUtil.createImageUri(this, viewModel.imageTempPath))
        } else {
            requestPermissionCamera.launch(Manifest.permission.CAMERA)
        }
    }

    /**
     * 拍照
     */
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        val file = File(viewModel.imageTempPath)
        if (file.exists()) {
            QNWhiteBoard.insertFile(FileConfig(file))
        }
    }

    /**
     * 请求相机权限
     */
    private val requestPermissionCamera =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                openCamera()
            }
        }

    /**
     * 选择图片
     */
    private val openGallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it == null) {
            return@registerForActivityResult
        }

        val path = FileUtil.getPathFromUri(this, it)

        if (path != null) {
            Log.v(TAG, "openGallery path:$path")

            QNWhiteBoard.insertFile(FileConfig(File(path)))
        }
    }

    /**
     * 选择文件
     */
    private val openFile = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it == null) {
            return@registerForActivityResult
        }

        val path = FileUtil.getPathFromUri(this, it)

        if (path != null) {
            if( path.endsWith("pdf")){
                Log.v(TAG, "openFile path:$path")
                val padding = 20
                val w=QNWhiteBoard.getViewport().size.displayWidth //画布宽
                val h=QNWhiteBoard.getViewport().size.displayHeight //画布高
                QNWhiteBoard.insertFile(FileConfig(File(path),"",padding.toFloat(),0f,w-padding*2 ,h ) )
            }else{
                Log.v(TAG, "openGallery path:$path")

                QNWhiteBoard.insertFile(FileConfig(File(path)))
            }
        }
    }
}