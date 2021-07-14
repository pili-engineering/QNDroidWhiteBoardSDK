
## com.qiniu.droid.rtc.insex

```java
module com.qiniu.droid.droid.whiteBoard

using java.lang.String
using java.lang.Double
using java.lang.Boolean

```


# 白板用户接口
```
class QNWhiteBoard  {

    //初始化白板SDK
     static void init(@NonNull Context context)

    //初始化白板SDK，
     static void init(@NonNull Context context, boolean debug)

    //添加一个白板事件监听器
     static void addListener(@NonNull QNWhiteBoardListener listener)

    //移除一个白板事件监听器
     static void removeListener(@NonNull QNWhiteBoardListener listener)

    //清空所有白板监听器
     static void clearListener()

    //进入白板
     static void joinRoom(@NonNull JoinConfig config)

   //离开白板
     static void leaveRoom()

    //白板截图
     static void screenshots(@NonNull QNScreenshotsCallback listener)

    //设置白板的默认初始输入模式配置
     static void setDefaultInputMode(@NonNull InputConfig config)

    //设置白板断线自动重连次数，默认为10次，设为0表示不自动重连
     static void setRetry(int count)

    //改变白板的输入模式
     static void setInputMode(@NonNull InputConfig config)

    //设置白板背景色
     static void setBackgroundColor(@ColorInt int color)

    //垂直滚动白板显示区
     static void scroll(float offsetY)

    //新建白板页
     static void newBoardPage()

    //插入新白板页，同时白板会跳转到新插入的页面
     static void insertBoardPage(@NonNull String pageId)

    //跳转到目标白板页
     static void jumpBoardPage(@NonNull String pageId)

    //前进到上一页
     static void preBoardPage()

    //前进到下一页
     static void nextBoardPage()

    //删除白板页
     static void deleteBoardPage(@NonNull String pageId)

    //向当前白板页中插入文件
     static void insertFile(@NonNull FileConfig config)

    //文件翻页
     static void jumpFilePage(@NonNull String widgetId, int pageNo)

    //删除文件
     static void deleteFile(@NonNull String widgetId)

    //获取白板当前状态
    @NonNull
     static BoardStatus getStatus()

    //获取当前加入的房间信息
     static Room getRoom()

    //获取当前房间中的个人信息
     static RoomMember getMe()

    //获取当前房间中的用户列表，包括自己
     static List<RoomMember> getUsers()

    //获取当前白板的全部页信息列表
     static List<WhiteBoardPage> getPageList()

    //获取当前白板页信息
     static WhiteBoardPage getCurrentPage()

    //获取当前白板页信息
     static int getBackgroundColor()

    //获取当前使用的白板输入模式
     static InputConfig getInputConfig()

    //获取当前被激活操作的widget
     static ActiveWidgetInfo getActiveWidget()

    //是否存在可还原的笔迹（擦除还原，仅对笔迹有效）
     static boolean canRecovery()

    //获取当前白板的窗口尺寸信息，包括白板的大小和偏移
     static WhiteBoardViewport  getViewport()

    //获取当前白板的窗口尺寸信息，包括白板的大小和偏移
     static void recover()
}
```

 # 白板事件回调


```
interface QNWhiteBoardListener {

   //成功加入白板房间，进入成功后的第一个事件
    void onJoinSuccess(@NonNull Room room, @NonNull RoomMember me)

    //加入房间失败
    void onJoinFailed(int errorCode)

   //白板正在自动重连，
    void onReconnecting(int time)

    //自动重连成功
    void onReconnected()

    //房间彻底断开连接，
    void onDisconnected()

    //白板房间状态变化时触发
    void onBoardStatusChanged(@NonNull BoardStatus status)

    //当前已经在房间中的用户列表，包括自己，加入房间后触发一次，自动重连成功后也会触发
    void onUserList(@NonNull List<RoomMember> users)

    //有其它用户加入了房间
    void onUserJoin(@NonNull RoomMember user)

    //有其它用户离开了房间
    void onUserLeave(@NonNull RoomMember user)

    //白板页信息列表，在首次进入房间和白板页列表结构变化时触发，
    void onBoardPageList(@NonNull List<WhiteBoardPage> list)

    //白板当前页变化，在首次加入房间后和翻页时触发
    void onCurrentBoardPageChanged(@NonNull WhiteBoardPage page)

    //某一个白板页信息变化
    void onBoardPageInfoChanged(@NonNull WhiteBoardPage page)

    //在白板的虚拟大小发生变化时触发，首次进入白板也会触发
    void onBoardSizeChanged(@NonNull WhiteBoardViewport viewport)

    //白板内发生滚动时触发，首次进入白板也会触发
    void onBoardScroll(@NonNull WhiteBoardViewport viewport)

    //白板背景色变化时触发，首次进入白板也会触发
    void onBackgroundColorChanged(@ColorInt int backgroundColor)

    //有新的widget被激活，也就是用户手势触碰到的widget
    void onWidgetActive(@Nullable ActiveWidgetInfo info)

    //有新的widget被激活，也就是用户手势触碰到的widget
    void onFilePageChanged(@NonNull ActiveWidgetInfo info)

    //当widget被执行了某些关键动作时触发，比如新增和删除文件等
    void onWidgetActionEvent(@NonNull WidgetActionEvent event)

    //笔迹回收站状态变化，当在擦除模式擦除笔迹或还原笔迹时触发。
    void onRecoveryStateChanged(boolean isEmpty)
}
```



# 加入房间相关

## 加入房间所需的参数

```
 final class JoinConfig {

    /**
     * 与appId，roomId，userId关联生成的标识符
     */
    @NonNull
     final String token;

    /**
     * 宽高比 [0.5,2.2]
     */
     double widthHeightThan = 0.5;

    /**
     * 角色id
     */
     int roleId = 0;

    /**
     * 用户会话id（可选）
     * 如果用户业务系统中有与[userId]对应的临时id可以作为[sessionId]传递，
     * 如果用户业务系统只有唯一的稳定用户id，则仅传递[userId]即可，[sessionId]会由白板系统自动生成
     */
    @Nullable
     String sessionId;

    /**
     * 用户名，在白板使用的用户名称
     */
    @Nullable
     String nickname;

    /**
     * 头像地址，在白板显示的用户头像
     */
    @Nullable
     String avatar;
```


##  白板房间

```
 final class Room {


    /**
     * 房间id
     */
    @NonNull
     final String roomId;

    /**
     * 房间云盘id
     */
    @NonNull
     final String fileGroupId;
}

```

## 房间成员


```
 final class RoomMember {

    /**
     * 用户业务系统中的稳定用户id
     */
    @NonNull
     final String userId;

    /**
     * 用户会话id
     */
    @NonNull
     final String sessionId;

    /**
     * 角色id
     */
     final int roleId;

    /**
     * 用户昵称
     */
    @Nullable
     final String nickname;

    /**
     * 用户头像
     */
    @Nullable
     final String avatar;

}
```

# 输入相关

## 白板内文件插入
```
 final class FileConfig {

    /**
     * 要插入的文件
     */
    @NonNull
     final File file;

    /**
     * 指定文件的实际名称，留空使用{@link #file}的名称
     */
    @Nullable
     final String name;

    /**
     * 文件插入白板时的初始水平偏移
     */
     final float left;

    /**
     * 文件插入白板时的初始垂直偏移
     */
     final float top;
}

```


## 输入配置信息
```

 final class InputConfig {

    /**
     * 笔输入模式配置
     *
     * @param color     笔颜色，支持透明度
     * @param thickness 粗细，必须大于0
     * @return 笔模式相关配置
     */
    @NonNull
     static InputConfig pen(@ColorInt int color, float thickness)

    /**
     * 激光笔输入模式配置
     *
     * @param laserType 激光笔类型
     * @return 激光笔模式相关配置
     */
    @NonNull
     static InputConfig laserPen(@NonNull LaserType laserType)

    /**
     * 橡皮输入模式配置
     *
     * @param size 橡皮面积
     * @return 橡皮模式相关配置
     */
    @NonNull
     static InputConfig erase(float size)

    /**
     * 选择输入模式配置
     *
     * @return 选择输入模式配置
     */
    @NonNull
     static InputConfig select()

    /**
     * 几何图形输入模式配置
     *
     * @param geometryType 几何图形类型
     * @param color        图形颜色
     * @param thickness    图形粗细
     * @return 几何图形输入模式配置
     */
    @NonNull
     static InputConfig geometry(@NonNull GeometryType geometryType, @ColorInt int color, float thickness)


    /**
     * 输入模式
     */
    @NonNull
     final InputMode mode;

    /**
     * 激光笔类型
     */
    @Nullable
     final LaserType laserType;

    /**
     * 集合图形类型
     */
    @Nullable
     final GeometryType geometryType;

    /**
     * 颜色，支持透明度
     */
    @ColorInt
     final int color;

    /**
     * 大小/粗细
     */
     final float size;
}
```



## widget动作事件

```
 final class WidgetActionEvent {

    /**
     * 动作发出者的sessionId
     */
    @NonNull
     final String sessionId;

    /**
     * widget类型
     */
    @NonNull
     final WidgetType type;

    /**
     * 动作类型
     */
    @NonNull
     final WidgetAction action;

    /**
     * widget名称
     */
    @Nullable
     final String name;
}
```


## 被激活的widget
```
 final class ActiveWidgetInfo {

    /**
     * widgetId
     */
     final String id;

    /**
     * widget类型
     */
     final WidgetType type;

    /**
     * 创建者id
     */
     final String userId;

    /**
     * 文件名
     */
     final String name;

    /**
     * 资源id
     */
     final String resourceId;

    /**
     * 文件路径
     */
     final String path;

    /**
     * 当前页码({@link WidgetType#FILE}时有意义），从1开始
     */
     final int currentPageNumber;

    /**
     * 总页数({@link WidgetType#FILE}时有意义）
     */
     final int pageCount;

```


## 输入模式
```
 enum InputMode {
    /**
     * 笔输入模式
     */
    PEN,

    /**
     * 橡皮输入模式
     */
    ERASE,

    /**
     * 选择输入模式
     */
    SELECT,

    /**
     * 几何图形
     */
    GEOMETRY,
}
```

##  几何图形类型枚举

```
 enum GeometryType {

    /**
     * 矩形
     */
    RECTANGLE(0),

    /**
     * 圆形
     */
    CIRCLE(1),

    /**
     * 线
     */
    LINE(3),

    /**
     * 箭头
     */
    ARROW(6);

}

```


## 激光笔类型
```
 enum LaserType {
    /**
     * 激光笔-点
     */
    LASER_DOT(2),

    /**
     * 激光笔-手
     */
    LASER_HAND(3),

    /**
     * 激光笔-白色箭头
     */
    LASER_ARROWS_WHITE(4),

    /**
     * 激光笔-黑色箭头
     */
    LASER_ARROWS_BLACK(5);

    /**
     * 枚举值
     */
     final int value;

    LaserType(int value) {
        this.value = value;
    }
}
```



## widget动作类型

```
 enum WidgetAction {
    /**
     * 上传/插入新widget
     */
    UPLOAD,

    /**
     * 删除widget
     */
    DELETE,

    /**
     * 加载成功
     */
    SUCCESSFUL,

    /**
     * 加载失败
     */
    FAILED,
}
```



## widget类型，白板中的一切都是widget

```
 enum WidgetType {
    /**
     * 白板
     */
    BOARD(0),

    /**
     * 文件
     */
    FILE(1),

    /**
     * 图片
     */
    IMAGE(2),

    /**
     * 几何图形
     */
    GEOMETRY(3),

    /**
     * 选择框
     */
    SELECTION(5);

    /**
     * 枚举值
     */
     final int value;

    WidgetType(int value) {
        this.value = value;
    }
}

```


# 白板UI尺寸信息相关


## 白板页信息
```

 final class WhiteBoardPage {

    /**
     * 页id
     */
    @NonNull
     final String pageId;

    /**
     * 页号
     */
     final int pageNumber;

    /**
     * 白板缩略图，没有时为空字符串
     */
    @NonNull
     final String thumbnails;
}


```



## 白板尺寸信息，所有数值基于白板内部的虚拟大小和坐标系
```
 final class WhiteBoardSize {

    /**
     * 白板最大宽度
     */
     final int maxWidth;

    /**
     * 白板最大高度
     */
     final int maxHeight;

    /**
     * 白板显示宽度
     */
     final int displayWidth;

    /**
     * 白板显示高度
     */
     final int displayHeight;

}

```



## 白板当前可视区，所有数值基于白板内部的虚拟大小和坐标系

```
 final class WhiteBoardViewport {

     /**
     * 白板尺寸
     */
    @NonNull
     final WhiteBoardSize size;

    /**
     * 当前白板水平偏移
     */
     final float offsetX;

    /**
     * 当前白板垂直偏移
     */
     final float offsetY;
}
```
