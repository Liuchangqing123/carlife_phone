#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/time.h>
#include <unistd.h>
#include <netinet/tcp.h>

#include <binder/ProcessState.h>
#include <binder/IServiceManager.h>
#include <binder/IMemory.h>
#include <sys/system_properties.h>
#include <android/input.h>
#include <binder/Parcel.h>

#include "config.h"
#include "tcpsocket.h"
#include "touch.h"
#include "IInputManager.h"
//#include "airplay_main.h"

//2019/04/11 xiaojun
#include <android/log.h>
#define CARLIFE_TOUCH_TAG "CARLIFE_TOUCH"


#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_INFO, CARLIFE_TOUCH_TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_INFO, CARLIFE_TOUCH_TAG, fmt, ##args)
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO, CARLIFE_TOUCH_TAG, fmt, ##args)

//end 
using namespace android;

struct POINTER_COORDS_INFO
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
};

IInputManager* inputManager = NULL;

bool injectEvent(IInputManager* inputManager, int* data)
{
	const int type = data[0];
	if(IInputManager::PARCEL_TOKEN_KEY_EVENT == type)
	{
		const int keyCode = data[1];
		const int action = data[2];
		
		LOGI("[Thread_CarlifeTouch]:key event: code %d, action %d.\n", keyCode, action);
		
		if(AKEY_EVENT_ACTION_UP == action)
		{
			inputManager->injectKeyEvent(keyCode, AKEY_EVENT_ACTION_DOWN, 0, 0);
			inputManager->injectKeyEvent(keyCode, AKEY_EVENT_ACTION_UP, 0, 0);
		}
		
		return true;
	}
	else if(IInputManager::PARCEL_TOKEN_MOTION_EVENT == type)
	{
		const int action = data[1];
		const int ptCount = data[2];
		//const int64_t eventTimeNano = *(int64_t *)&data[3];
		POINTER_COORDS_INFO *pci = (POINTER_COORDS_INFO *)&data[5];
		PointerProperties ptPro[ptCount];
		PointerCoords ptCoords[ptCount];
		
		for(int i = 0; i < ptCount; i++)
		{
			ptPro[i].id = i;
			ptPro[i].toolType = AMOTION_EVENT_TOOL_TYPE_FINGER;
			
//			LOGI("[%d] %d, %d, orientation %.02f, pressure %.02f, size %.02f, toolMajor %.02f, toolMinor %.02f, touchMajor %.02f, touchMinor %.02f, x %.02f, y %.02f\n", \
//						action, ptCount, i, pci->orientation, pci->pressure, pci->size, pci->toolMajor, pci->toolMinor, pci->touchMajor, pci->touchMinor, pci->x, pci->y);
			ptCoords[i].clear();
			
			if(AMOTION_EVENT_ACTION_DOWN == action || AMOTION_EVENT_ACTION_MOVE == action)
	    		ptCoords[i].setAxisValue(AMOTION_EVENT_AXIS_PRESSURE, 1.0f);
	    	else
	    		ptCoords[i].setAxisValue(AMOTION_EVENT_AXIS_PRESSURE, 0.0f);
	    
	    	ptCoords[i].setAxisValue(AMOTION_EVENT_AXIS_SIZE, 1.0f);
			ptCoords[i].setAxisValue(AMOTION_EVENT_AXIS_X, pci->x);
	   	 	ptCoords[i].setAxisValue(AMOTION_EVENT_AXIS_Y, pci->y);
	    
	    	pci ++;
		}
		
		return inputManager->injectMotionEvent(action, ptCount, ptPro, 1, ptCoords);
	}
	else
	{
		LOGI("[injectEvent]: invalid type %d.\n", type);
		return false;
	}
}

#if HID_USE_SOCKET_LINK
void *Thread_CarlifeTouch(void *arg)
{
//	airplayserver *airplay_server = (airplayserver *)arg;//2019/04/11
	bool success = false;
	int sock = 0;
	int sock_n = 0;
	sp<IInputManager> mInputManager = NULL;
	const String16 name("input");
	sp<IServiceManager> sm = NULL;
	sp<IBinder> binder;
	int err;
	
	do
	{
		if(sm == NULL)
		{
			sm = defaultServiceManager();
			if(sm == NULL)
			{
				LOGI("[Thread_CarlifeTouch]: get service mgr failed retry\n");
			}
		}
		else
		{
			binder = sm->getService(name);
			if (binder != 0)
			{
				mInputManager = interface_cast<IInputManager>(binder);
				if(mInputManager == NULL)
				{
					LOGI("[Thread_CarlifeTouch]: get input mgr service failed retry\n");
				}
				else
				{
					success = true;
					break;
				}
			}
		}
		usleep(500 * 1000);
	}while(1);//!airplay_server->stop);//2019/04/11
	
	if(success)
	{
		bool isLocal;
		sock = createServer( DEF_TOUCH_PORT);
		if(sock <= 0)
		{
			LOGI("[Thread_CarlifeTouch]: create server (port:%d) failed!\n", DEF_TOUCH_PORT);
			return NULL;
		}
		
		while(1)//!airplay_server->stop)//2019/04/11
		{
			if(!canRecv(sock, 100))
			{
				continue;
			}
			else
			{
				sock_n = serverAccept(sock, &isLocal);
				LOGI("[Thread_CarlifeTouch]:  ERROR at serverAccept sock_n=%d.\n", sock_n);
				break;
			}
		}

		if(sock_n != 0)
		{
			inputManager = mInputManager.get();		
			if(inputManager)
			{
				int count = 0;
				int buff[MAX_PAYLOAD_LENGTH / 4] = {0};
				struct timeval timeout;
				fd_set readSet;
				FD_ZERO(&readSet);
				while(1)//!airplay_server->stop)//2019/04/11
				{
				    FD_SET(sock_n, &readSet);
					timeout.tv_sec  = 0;
					timeout.tv_usec = 500 * 1000;
					err = select(sock_n + 1, &readSet, NULL, NULL, &timeout );
					if(err == -1)
					{
					    LOGE("[Thread_CarlifeTouch]: client select error: %d\n", errno);
						break;
					}
					else if (err == 0)
					{
						if (isConnectionLost(sock_n))
						{
						    LOGE("[Thread_CarlifeTouch]: client ConnectionLost\n");
							break;
						}
					}
					else
					{
						if (FD_ISSET(sock_n, &readSet))
						{
						    err = recv(sock_n, (void*)&count, sizeof(int), 0);
							if (err > 0)
							{
								if(count > MAX_PAYLOAD_LENGTH || count <= 0)
								{
									LOGI("[Thread_CarlifeTouch]:  ERROR packet count =%d.\n", count);
									break;
								}
								err = recv(sock_n, (void*)buff, count, 0);
								if (err > 0)
								{
									injectEvent(inputManager, buff);
								}
								else
								{
									LOGI("[Thread_CarlifeTouch]:  ERROR recv errNO= %d\n", errno);
									break;
								}
							}
							else
							{
								LOGI("[Thread_CarlifeTouch]:  recv errNO= %d\n", errno);
								break;
							}
						}
					}
				}//end while(1)//!airplay_server->stop)//2019/04/11
			}
				
			if(sock_n != 0)
			{
				closeSocket(sock_n);
			}
		}
	}
	
	if(sock != 0)
	{
		closeSocket(sock);
		sock = 0;
	}
	LOGI("[Thread_CarlifeTouch]:  end!!!!!!\n");
	return NULL;
}
int carlife_creat_touch_srv()
{
	pthread_t tid_touch_srv = 0;

	int	err = pthread_create(&tid_touch_srv, NULL, Thread_CarlifeTouch, NULL);
	if(err != 0){
		LOGE("[carlife_creat_touch_srv]:create server error\n");
		return -1;
	}else{
		LOGE("[carlife_creat_touch_srv]:create server ok\n");
		return 0;
	}

	return 0;
}

#endif

