
//TOUCH 网络传输协议
//1.TCP Localhost(127.0.0.1) @ port:9300 长连接
//2.传输数据结构
struct type tag_POINTER_COORDS_INFO//系统定义结构
{
	float orientation;
	float pressure;
	float size;
	float toolMajor;
	float toolMinor;
	float touchMajor;
	float touchMinor;
	float x;
	float y;
}POINTER_COORDS_INFO;

struct type tag_TOUCHEVENTHEAD//触摸/按键事件头
{
	INT   nEventPackSize;   //4BYTE 一个事件包长度 
	INT   cEventType;       //4BYTE 类别，1：按键，2：触控
	INT   cEventKey;        //4BYTE 键值，cEventType==1 时为键值；cEventType==2 为Action ,不知道如何定义
	INT   cEventAction;     //4BYTE 动作，cEventType==1 时为动作，0=按压，1=弹起；cEventType==2 为触控坐标数
	INT   EventTimestamp[2]; //时间搓
	POINTER_COORDS_INFO  ptEvent[cEventAction];//坐标点队列
};

