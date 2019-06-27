#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <unistd.h>
#include <sys/select.h>
#include <sys/time.h>
#include <sys/types.h>
#include <sys/select.h>
#include <net/if.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include "log.h"
#include "tcpsocket.h"
#include "hqueue.h"

#define SCREEN_CAPTURE_ENABLE				1

#if SCREEN_CAPTURE_ENABLE

#define LOGT(fmt, args...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##args)

//#define DEBUG_SCREEN                        1

#include <binder/ProcessState.h>
#include <binder/IServiceManager.h>
#include <gui/Surface.h>
#include <gui/SurfaceComposerClient.h>
#include <gui/ISurfaceComposer.h>
#include <ui/DisplayInfo.h>
#include <ui/GraphicBufferMapper.h>
#include <media/openmax/OMX_IVCommon.h>
#include <media/stagefright/foundation/ABuffer.h>
#include <media/stagefright/foundation/ADebug.h>
#include <media/stagefright/foundation/AMessage.h>
#include <media/stagefright/MediaCodec.h>
#include <media/stagefright/MediaErrors.h>
#include <media/ICrypto.h>
#include <android/native_window.h>
#include <sys/system_properties.h>

#define SOCKET_VIDEO_LOCALHOST_PORT 	(8200)
#define USE_FRAME_CONTROL				(1)

using namespace android;

static int setDisplayProjection(const sp<IBinder>& dpy, const DisplayInfo& mainDpyInfo, int width, int height, Rect& rect);
void *carlife_video_encode_thread(void *arg);
#endif

typedef struct
{
	uint8_t *data;
	int   size;
}ST_VIDEO_DATA;

typedef struct
{
	int width;
	int height;
	int framerate;
}ST_VIDEO_RESOLUTION;

typedef struct
{
	HQUEUE *pstVideoQue;
	int connectSocket;
	ST_VIDEO_RESOLUTION stResolution;
}ST_VIDEO_SRV;

#if USE_FRAME_CONTROL	/*用于帧率控制*/

uint64_t getSystemTimeUs()
{
	struct timeval current_time = {0,};
	gettimeofday(&current_time, NULL);
	return (current_time.tv_sec * 1000*1000 + current_time.tv_usec);
}

static bool setupNativeWindow(ANativeWindow* nativeWindow, int width, int height, int format)
{
	bool success = false;
	int err;
	int numBufs = 0;
  int minUndequeuedBufs = 0;
  
	do
	{
			err = native_window_api_connect(nativeWindow, NATIVE_WINDOW_API_CPU);
			if(err != OK) 
			{
	       LOGE("native_window_api_connect returned an error: %s (%d)", strerror(-err), err);
	       break;
	    }
	    
	    err = native_window_set_buffers_geometry(nativeWindow, width, height, format);
	    if(err != OK)
	    {
	    		LOGE("native_window_set_buffers_geometry returned an error: %s (%d)", strerror(-err), err);
	    		break;
	    }
	    
	    err = native_window_set_scaling_mode(nativeWindow, NATIVE_WINDOW_SCALING_MODE_SCALE_TO_WINDOW);
	    if(err != OK)
	    {
	    		LOGE("native_window_set_scaling_mode returned an error: %s (%d)", strerror(-err), err);
	    		break;
	    }
	    
	    err = native_window_set_usage(nativeWindow, GRALLOC_USAGE_SW_WRITE_OFTEN);
	    if(err != OK)
	    {
	    		LOGE("native_window_set_usage returned an error: %s (%d)", strerror(-err), err);
	    		break;
	    }
	    
	    err = nativeWindow->query(nativeWindow, NATIVE_WINDOW_MIN_UNDEQUEUED_BUFFERS, &minUndequeuedBufs);
	    if(err != OK)
	    {
	    		LOGE("query buffers returned an error: %s (%d)", strerror(-err), err);
	    		break;
	    }
	    LOGI("min undequeued buffers %d", minUndequeuedBufs);
	    
	    numBufs = minUndequeuedBufs + 1;
	    if(numBufs < 3)
	    	numBufs = 3;
	    err = native_window_set_buffer_count(nativeWindow, numBufs);
	    if(err != OK)
	    {
	    		LOGE("native_window_set_buffer_count returned an error: %s (%d)", strerror(-err), err);
	    		break;
	    }
	    
	    success = true;
	    
	}while(0);
	
	return success;
}

static bool copyFrameToSurface(ANativeWindow* nativeWindow, uint8_t* data, int width, int height, int64_t renderTime)
{
		bool success = false;
		ANativeWindowBuffer *buf = NULL;
		int err;
		
		do
		{
				err = native_window_dequeue_buffer_and_wait(nativeWindow, &buf);
				if(err != OK)
				{
						LOGE("dequeueBuffer returned error %d", err);
						break;
				}
				
				GraphicBufferMapper &mapper = GraphicBufferMapper::get();
				Rect bounds(width, height);
				
				void *dst;
				err = mapper.lock(buf->handle, GRALLOC_USAGE_SW_WRITE_OFTEN, bounds, &dst);
				if(OK == err)
				{
						const int dst_width = buf->width;
						const int dst_height = buf->height;
						const int stride = buf->stride;
						uint8_t *ptr = (uint8_t *)dst;

						for(int i = 0; i < dst_height; i++)
						{
							memcpy(ptr, data, dst_width * 4);
							ptr += (stride * 4);
							data += (width * 4);
						}
						
						mapper.unlock(buf->handle);
				}
				else
				{
						LOGI("lock err %d", err);
				}
				
				err = native_window_set_buffers_timestamp(nativeWindow, renderTime);
				
				err = nativeWindow->queueBuffer(nativeWindow, buf, -1);
				if(err != OK)
				{
						LOGE("queueBuffer returned error %d", err);
				}
				
				success = true;
				
		}while(0);
		
		
		return success;
}

class FrameWaiter : public CpuConsumer::FrameAvailableListener
{
public:
	FrameWaiter(sp<CpuConsumer> consumer, sp<ANativeWindow> nativeWindow, int framerate):
		mConsumer(consumer), mNativeWindow(nativeWindow)
	{
		mSyncTimeNs = (1000*1000*1000)/(framerate+10);
		mLastTimeNs = 0;
		pthread_mutex_init(&mMutex, NULL);
	}
	
	~FrameWaiter()
	{
		pthread_mutex_destroy(&mMutex);
	}
	
#if defined(IS_ANDROID_18) || defined(IS_ANDROID_19) || defined(IS_ANDROID_21)
	virtual void onFrameAvailable()
#else
	virtual void onFrameAvailable(const BufferItem& item)
#endif
	{
		pthread_mutex_lock(&mMutex);
		
		bool isDrop = false;
		CpuConsumer::LockedBuffer buffer;
		int64_t current = 0;
		
		status_t err = mConsumer->lockNextBuffer(&buffer);
		current = buffer.timestamp;
		
		if(0 == mLastTimeNs)
		{
			mLastTimeNs = current;
		}
		else
		{
			int64_t step = current - mLastTimeNs;
			
			if(step < mSyncTimeNs)
			{
				isDrop = true;
			}
			else
			{
				mLastTimeNs = current;
			}
		#if DEBUG_SCREEN
		    LOGD("Frame Available [%d] %lldms\n", isDrop, step/(1000*1000));
		#endif
		}
		
		if(!isDrop)
		{
				if(mNativeWindow != NULL)
				{
						copyFrameToSurface(mNativeWindow.get(), buffer.data, buffer.stride, buffer.height, buffer.timestamp);
				}
		}
		
		mConsumer->unlockBuffer(buffer);
		memset(&buffer, 0, sizeof(buffer));
		
		pthread_mutex_unlock(&mMutex);
	}
	
private:
	sp<CpuConsumer> mConsumer;
	int64_t mSyncTimeNs;
	int64_t mLastTimeNs;
	sp<ANativeWindow> mNativeWindow;
	Vector<sp<ABuffer> > mBuffers;
	pthread_mutex_t mMutex;
};
#endif


#if SCREEN_CAPTURE_ENABLE/*用于刷屏保持录屏*/
struct keeper_info {

	bool is_stop;
	int screenW, screenH;
	int width, height, frame_rate;
};

void *keeper_screen_thread(void *arg)
{
	keeper_info* info = (keeper_info*)arg;
	
	bool success = false;
	
	sp<SurfaceComposerClient> surfaceClient = NULL;
	sp<SurfaceControl> surfaceControl = NULL;
	ANativeWindow* nativeWindowKeeper = NULL;

	do
	{
		surfaceClient = new SurfaceComposerClient();
		if(surfaceClient == NULL || surfaceClient->initCheck() != OK)
		{
			LOGE("init check failed.");
			break;
		}

		surfaceControl = surfaceClient->createSurface(String8("keeper_surface"), info->width, info->height, PIXEL_FORMAT_RGBA_8888, 0);
  		if(surfaceControl == NULL)
  		{
  			LOGE("create keeper surface failed.");
  			break;
  		}
		
  		SurfaceComposerClient::openGlobalTransaction();
  		surfaceControl->setLayer(INT_MAX);
  		surfaceControl->setPosition(8, 8);
  		surfaceControl->setSize(8, 8);
  		SurfaceComposerClient::closeGlobalTransaction();

		nativeWindowKeeper = surfaceControl->getSurface().get();
  		if(NULL == nativeWindowKeeper)
  		{
  			LOGE("failed to get surface!");
  			break;
  		}

		success = true;

	}while(0);

	if(success)
	{
		const uint64_t sync_us = 1000 * 1000 / 25;
		uint32_t rgb = 0x00000000;
		
		while(!info->is_stop)
		{
			ANativeWindow_Buffer buf;
			uint64_t start_us = getSystemTimeUs();
			uint64_t time_us;
			
			int err = ANativeWindow_lock(nativeWindowKeeper, &buf, NULL);
			if(0 == err)
			{
				uint32_t *dst = (uint32_t *)buf.bits;
				int s = buf.stride;
				int w = buf.width;
				int h = buf.height;
				
				if(0x00010101 == rgb)
				{
					rgb = 0x00000000;
				}
				else
				{
					rgb = 0x00010101;
				}
				
				for(int j = 0; j < h; j++)
				{
					for(int i = 0; i < w; i++)
					{
							*dst++ = rgb;
					}
					
					dst += (s - w);
				}
				
				ANativeWindow_unlockAndPost(nativeWindowKeeper);
			}

			time_us = getSystemTimeUs() - start_us;

			if(time_us < sync_us)
			{
				usleep(sync_us - time_us);
			}
		}
	}
	LOGI("airplay_screen_thread keeper_screen_thread end\n");
	return NULL;
}
#endif

void *carlife_creat_video_srv_thread(void *arg)
{
	bool isLocal;
	int err = 0;
	int sock = 0;
	int sock_n = 0;

	bool bfexit = false;
	int	needRecvLen = 0;
	ST_VIDEO_RESOLUTION *pstResolution = NULL;

	pthread_t tid_video_encode;

	memset(&tid_video_encode, 0, sizeof(pthread_t));
	
	pthread_detach(pthread_self());
	
	ST_VIDEO_SRV *pstCarlifeVideoSrv = NULL;
	pstCarlifeVideoSrv = (ST_VIDEO_SRV *)malloc(sizeof(ST_VIDEO_SRV));
	
	memset(pstCarlifeVideoSrv, 0, sizeof(ST_VIDEO_SRV));

	sock = createServer(SOCKET_VIDEO_LOCALHOST_PORT);
	if(sock <= 0)
	{
		LOGI("carlife_creat_video_srv create server (port:%d) failed!\n", SOCKET_VIDEO_LOCALHOST_PORT);
		return NULL;
	}

	sock_n = serverAccept(sock, &isLocal);
	if(sock_n > 0)
	{
		recvData(sock_n, &needRecvLen, sizeof(int), bfexit);
		needRecvLen = ntohl(needRecvLen);
		LOGI("%s needRecvLen:%d %#x\n", __func__, needRecvLen, needRecvLen);

		char *pcBuff = (char *)malloc(needRecvLen);
		memset(pcBuff, 0, needRecvLen);

		recvData(sock_n, pcBuff, needRecvLen, bfexit);

		pstResolution = (ST_VIDEO_RESOLUTION *)pcBuff;

		pstResolution->height = ntohl(pstResolution->height);
		pstResolution->width = ntohl(pstResolution->width);
		pstResolution->framerate = ntohl(pstResolution->framerate);

		if(pstResolution->framerate <= 0)
		{
			LOGI("default set: pstResolution->framerate:%d %#x\n", pstResolution->framerate, pstResolution->framerate);
			pstResolution->framerate = 25;
		}

		LOGI("%s pstResolution->height:%d %#x\n", __func__, pstResolution->height, pstResolution->height);
		LOGI("%s pstResolution->width:%d %#x\n", __func__, pstResolution->width, pstResolution->width);
		LOGI("%s pstResolution->framerate:%d %#x\n", __func__, pstResolution->framerate, pstResolution->framerate);

		pstCarlifeVideoSrv->pstVideoQue = hqCreate(1024, sizeof(ST_VIDEO_DATA), HQ_GET_WAIT | HQ_PUT_WAIT);
		pstCarlifeVideoSrv->connectSocket = sock_n;
		pstCarlifeVideoSrv->stResolution.height = pstResolution->height;
		pstCarlifeVideoSrv->stResolution.width = pstResolution->width;
		pstCarlifeVideoSrv->stResolution.framerate = pstResolution->framerate;

		free(pcBuff);
		
		err = pthread_create(&tid_video_encode, NULL, carlife_video_encode_thread, pstCarlifeVideoSrv);
		if(err != 0)
		{
			LOGE("carlife_video_encode_thread error\n");
		}
		else
		{
			LOGE("carlife_video_encode_thread ok\n");
		}

	}

	pthread_exit(0);
	
	return NULL;
}

int carlife_creat_video_srv()
{
	pthread_t tid_video_srv = 0;

	int	err = pthread_create(&tid_video_srv, NULL, carlife_creat_video_srv_thread, NULL);
	if(err != 0)
	{
		LOGE("carlife_creat_video_srv error\n");
	}
	else
	{
		LOGE("carlife_creat_video_srv ok\n");
	}

	return 0;
}


void *carlife_video_encode_thread(void *arg)
{
	int err, trycount = 0;

	unsigned int ms = 0,ms1=0,ms2=0;
	int one_time = 1;
	struct timeval tv;
	gettimeofday(&tv, NULL);
	ms = tv.tv_sec * 1000 + tv.tv_usec/1000;

	ST_VIDEO_SRV *pstCarlifeVideoSrv = NULL;
	if(NULL != arg)
	{
		pstCarlifeVideoSrv = (ST_VIDEO_SRV *)arg;
	}

#if SCREEN_CAPTURE_ENABLE
	int success = -1;
	int width = pstCarlifeVideoSrv->stResolution.width;
	int height = pstCarlifeVideoSrv->stResolution.height;
	int bitrate = 3*1000*1000;
	int framerate = pstCarlifeVideoSrv->stResolution.framerate;
	int iFrameInterval = framerate*3;
	int screen_width = pstCarlifeVideoSrv->stResolution.width;
	int screen_height = pstCarlifeVideoSrv->stResolution.height;

	AString strMimeType = "video/avc";
	char looper_name[] = "screen_encoder_looper";
	char consumer_name[] = "ScreenCaptureConsumer";
	char display_name[] = "ScreenCaptureDisplay";

	sp<IBinder> mainDpy = SurfaceComposerClient::getBuiltInDisplay(ISurfaceComposer::eDisplayIdMain);

	sp<IBinder> dpy = NULL;
	sp<AMessage> format = new AMessage;
	sp<ALooper> looper = new ALooper;
	sp<MediaCodec> codec = NULL;

	Rect dispRect;
	DisplayInfo mainDpyInfo;

	keeper_info keeper = {0,};
	pthread_t keeper_id = 0;

#ifdef IS_ANDROID_26
	Vector<sp<MediaCodecBuffer> > mOutBuffers;
#else
	Vector<sp<ABuffer> > mOutBuffers;
#endif

#if USE_FRAME_CONTROL
	
	#if defined(IS_ANDROID_19)
		sp<BufferQueue> producer = new BufferQueue();
	#else
		sp<IGraphicBufferProducer> producer;
		sp<IGraphicBufferConsumer> consumer;
	#endif
	
	sp<CpuConsumer> cpuConsumer;
	sp<FrameWaiter> waiter;

	sp<IGraphicBufferProducer> bufferProducer;
	sp<Surface> surface;
	sp<ANativeWindow> nativeWindow;
#else
	sp<IGraphicBufferProducer> bufferProducer;
#endif
	char wm_cmd[64] = {0,};

	android::ProcessState::self()->startThreadPool();

	do
	{
		LOGD("screen capture %dx%d %dfps ============ zgs mod\n", width, height, framerate);

		snprintf(wm_cmd, 64, "wm size %dx%d &", width, height);
		system(wm_cmd);
		
		err = SurfaceComposerClient::getDisplayInfo(mainDpy, &mainDpyInfo);
		if(err != NO_ERROR)
		{
			LOGE("get display info failed: %d\n", err);
			break;
		}
		LOGD("main display is %dx%d @%.2ffps (orientation=%u)\n", mainDpyInfo.w, mainDpyInfo.h, mainDpyInfo.fps, mainDpyInfo.orientation);
		screen_height = mainDpyInfo.h;
		screen_width = mainDpyInfo.w;
		
		// Create encoder
		format->setInt32("width", width);
		format->setInt32("height", height);
#if defined(IS_ANDROID_16) || defined(IS_ANDROID_17) || defined(IS_ANDROID_18) || defined(IS_ANDROID_19)
		format->setString("mime", strMimeType.c_str());
#else
    	format->setString("mime", strMimeType);
#endif
		format->setInt32("color-format", OMX_COLOR_FormatAndroidOpaque);
    	format->setInt32("bitrate", bitrate);
    	format->setFloat("frame-rate", framerate);
    	format->setInt32("i-frame-interval", iFrameInterval);
		// bitrate-mode
		//format->setInt32("bitrate-mode", 0/* CQ:0, VBR:1, CBR:2 */);
		// latency
		// profile level

		looper->setName(looper_name);
		looper->start();
		
		codec = MediaCodec::CreateByType(looper, strMimeType.c_str(), true);
		if(codec == NULL)
		{
			LOGE("create encoder failed!\n");
			break;
		}
		
		err = codec->configure(format, NULL, NULL, MediaCodec::CONFIGURE_FLAG_ENCODE);
		if(err != NO_ERROR)
		{
			LOGE("config encoder failed: %d\n", err);
			break;
		}
	  
	  	err = codec->createInputSurface(&bufferProducer);
	    if(err != NO_ERROR)
	    {
			LOGE("create input surface failed: %d\n", err);
			break;
	    }

#if USE_FRAME_CONTROL
		surface = new Surface(bufferProducer);
		if(surface == NULL)
		{
			break;
		}

		nativeWindow = surface.get();
		setupNativeWindow(nativeWindow.get(), width, height, PIXEL_FORMAT_RGBA_8888);

		#if defined(IS_ANDROID_19)
			cpuConsumer = new CpuConsumer(producer, 1);
		#else
			BufferQueue::createBufferQueue(&producer, &consumer);
			cpuConsumer = new CpuConsumer(consumer, 1);
		#endif

		cpuConsumer->setName(String8(consumer_name));

		#if ENABLE_SCALE
			if(enabled_scale)
				cpuConsumer->setDefaultBufferSize(screen_scale_width, screen_scale_height);
			else
				cpuConsumer->setDefaultBufferSize(screen_width, screen_height);
		#else
			cpuConsumer->setDefaultBufferSize(screen_width, screen_height);
		#endif
		
		cpuConsumer->setDefaultBufferFormat(PIXEL_FORMAT_RGBA_8888);
		waiter = new FrameWaiter(cpuConsumer, surface.get(), framerate);
		cpuConsumer->setFrameAvailableListener(waiter);

		dpy = SurfaceComposerClient::createDisplay(String8(display_name), false /* secure */);

		SurfaceComposerClient::openGlobalTransaction();
		SurfaceComposerClient::setDisplaySurface(dpy, producer);
					
		#if ENABLE_SCALE
			if(enabled_scale)
				setDisplayProjection(dpy, mainDpyInfo, screen_scale_width, screen_scale_height, dispRect);
			else
				setDisplayProjection(dpy, mainDpyInfo, screen_width, screen_height, dispRect);
		#else
			setDisplayProjection(dpy, mainDpyInfo, screen_width, screen_height, dispRect);
		#endif

		SurfaceComposerClient::setDisplayLayerStack(dpy, 0);	// default stack
		SurfaceComposerClient::closeGlobalTransaction();
#else
		dpy = SurfaceComposerClient::createDisplay(String8(display_name), false /* secure */);
		
		SurfaceComposerClient::openGlobalTransaction();
		SurfaceComposerClient::setDisplaySurface(dpy, bufferProducer);
		setDisplayProjection(dpy, mainDpyInfo, width, height, dispRect);
		SurfaceComposerClient::setDisplayLayerStack(dpy, 0);	// default stack
		SurfaceComposerClient::closeGlobalTransaction();
#endif

		gettimeofday(&tv, NULL);
		ms1 = tv.tv_sec * 1000 + tv.tv_usec/1000 ;
		LOGI("start screen time spend ms[%u] ms================\n", ms1 - ms);

		err = codec->start();
		if(err != NO_ERROR)
		{
			LOGE("start encoder failed: %d\n", err);
			break;
		}

		err = codec->getOutputBuffers(&mOutBuffers);
		if(err != NO_ERROR)
		{
			LOGE("get output buffers failed: %d\n", err);
			break;
		}

		keeper.screenW = mainDpyInfo.w;
		keeper.screenH = mainDpyInfo.h;
		keeper.width = width;
		keeper.height = height;
		keeper.frame_rate = framerate;
		keeper.is_stop = 0;

		pthread_create(&keeper_id, NULL, keeper_screen_thread, &keeper);

		LOGD("screen capture start success!\n");
		success = 0;

	}while(0);
	
	if(0 == success)
	{
		const int kTimeout = 250 * 1000; // us
		const int nal_size = 4;

		while(1)
		{
			size_t bufIndex, offset, size;
			int64_t ptsUsec;
			uint32_t flags;

			err = codec->dequeueOutputBuffer(&bufIndex, &offset, &size, &ptsUsec, &flags, kTimeout);
			if(NO_ERROR == err)
			{
				const sp<ABuffer> &dstBuffer = mOutBuffers[bufIndex];
				int length = dstBuffer->size();
				uint8_t *frame = dstBuffer->data();

//=====================================================================================================

				char *pcSendBuf = (char *)frame;
				int SendLen = htonl(dstBuffer->size());
				int fIsExit = 0;//TO DO: just for test 2019-04-30

				tcp_send_data(pstCarlifeVideoSrv->connectSocket, &SendLen, sizeof(int), fIsExit, 10);
				LOGE("[%s:%d] length:%d, SendLen:%#x\n", __func__, __LINE__, length, SendLen);

				tcp_send_data(pstCarlifeVideoSrv->connectSocket, pcSendBuf, length, fIsExit, 10);

//=====================================================================================================

				if((flags & MediaCodec::BUFFER_FLAG_CODECCONFIG) != 0)
				{
					//LOGI("CodeConfig: length=%d.", length);

				}
				else
				{
					uint8_t type = frame[5] & 0x1F;
					//LOGI("Frame: length=%d. type=%d", length, type);
				}

				err = codec->releaseOutputBuffer(bufIndex);
				if(err != NO_ERROR)
				{
					LOGE("unable to release output buffer: %d\n", err);
					break;
				}
				
			}
			else if(INFO_OUTPUT_BUFFERS_CHANGED == err)
			{
				LOGI("output buffers changed\n");
				err = codec->getOutputBuffers(&mOutBuffers);
				if(err != NO_ERROR)
				{
					LOGE("unable to get new output buffers (err=%d)\n", err);
					break;
				}
			}
			else if(INFO_FORMAT_CHANGED == err)
			{
				LOGI("encoder format changed\n");
			}
			else if(INVALID_OPERATION == err)
			{
				LOGE("request for encoder buffer failed\n");
				break;
			}
			else if(-EAGAIN == err)
			{
				usleep(10*1000);
			}
			else
			{
				LOGE("got weird result %d from dequeueOutputBuffer\n", err);
				break;
			}
		}
		
		LOGD("screen capture is stopping...\n");
		codec->stop();
	}
	
	if(dpy != NULL)
	{
		SurfaceComposerClient::destroyDisplay(dpy);
		dpy = NULL;
	}

#if USE_FRAME_CONTROL
	if(nativeWindow.get() != NULL)
	{
		native_window_api_disconnect(nativeWindow.get(), NATIVE_WINDOW_API_CPU);
		nativeWindow.clear();
		nativeWindow = NULL;
	}
	LOGD("nativeWindow cleared.");
	
	if(surface.get() != NULL)
	{
		surface.clear();
		surface = NULL;
	}
	LOGD("surface cleared.");
#endif

	if(codec.get() != NULL)
	{
		codec->release();
		codec.clear();
		codec = NULL;
	}
    LOGD("codec cleared.");


	LOGI("screen capture is done.\n");

#if SCREEN_CAPTURE_ENABLE
	if (keeper_id)
	{
		pthread_join(keeper_id, NULL);
		keeper_id = 0;
		LOGD("%s keeper_thread exit\n", __func__);
	}
#endif

	shutdown(pstCarlifeVideoSrv->connectSocket, SHUT_WR);
	close(pstCarlifeVideoSrv->connectSocket);

	LOGD("connectSocket close");
	LOGD("%s exit\n", __func__);

#endif

ENCODE_EXIT:
	LOGD("carlife_video_encode_thread exit\n");

	return NULL;
}

#if SCREEN_CAPTURE_ENABLE
static bool isDeviceRotated(int orientation) 
{
	return orientation != DISPLAY_ORIENTATION_0 && orientation != DISPLAY_ORIENTATION_180;
}

static int setDisplayProjection(const sp<IBinder>& dpy, const DisplayInfo& mainDpyInfo, int width, int height, Rect& rect)
{
    int err;

    // Set the region of the layer stack we're interested in, which in our
    // case is "all of it".  If the app is rotated (so that the width of the
    // app is based on the height of the display), reverse width/height.
    bool deviceRotated = isDeviceRotated(mainDpyInfo.orientation);
    uint32_t sourceWidth, sourceHeight;
    if (!deviceRotated) 
	{
        sourceWidth = mainDpyInfo.w;
        sourceHeight = mainDpyInfo.h;
    } 
	else 
	{
        LOGD("using rotated width/height");
        sourceHeight = mainDpyInfo.w;
        sourceWidth = mainDpyInfo.h;
    }
    Rect layerStackRect(sourceWidth, sourceHeight);

    // We need to preserve the aspect ratio of the display.
    float displayAspect = (float) sourceHeight / (float) sourceWidth;


    // Set the way we map the output onto the display surface (which will
    // be e.g. 1280x720 for a 720p video).  The rect is interpreted
    // post-rotation, so if the display is rotated 90 degrees we need to
    // "pre-rotate" it by flipping width/height, so that the orientation
    // adjustment changes it back.
    //
    // We might want to encode a portrait display as landscape to use more
    // of the screen real estate.  (If players respect a 90-degree rotation
    // hint, we can essentially get a 720x1280 video instead of 1280x720.)
    // In that case, we swap the configured video width/height and then
    // supply a rotation value to the display projection.
    uint32_t videoWidth, videoHeight;
    uint32_t outWidth, outHeight;

    videoWidth = width;
    videoHeight = height;
    
    if (videoHeight > (uint32_t)(videoWidth * displayAspect)) 
	{
        // limited by narrow width; reduce height
        outWidth = videoWidth;
        outHeight = (uint32_t)(videoWidth * displayAspect);
    }
	else
	{
        // limited by short height; restrict width
        outHeight = videoHeight;
        outWidth = (uint32_t)(videoHeight / displayAspect);
    }
	
    uint32_t offX, offY;
    offX = (videoWidth - outWidth) / 2;
    offY = (videoHeight - outHeight) / 2;
	
    Rect displayRect(offX, offY, offX + outWidth, offY + outHeight);

	rect.left = offX;
	rect.top = offY;
	rect.right = rect.left + outWidth;
	rect.bottom = rect.top + outHeight;
		
    LOGD("Content area is %ux%u at offset x=%d y=%d", outWidth, outHeight, offX, offY);

    SurfaceComposerClient::setDisplayProjection(dpy,
            DISPLAY_ORIENTATION_0,
            layerStackRect, displayRect);
    return NO_ERROR;
}
#endif
