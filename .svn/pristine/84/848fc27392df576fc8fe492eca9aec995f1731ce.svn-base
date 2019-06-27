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
#include <sys/prctl.h>
#include <media/AudioRecord.h>
#include <sys/types.h>
#include <sys/socket.h>
#include "log.h"
#include "audio.h"
#include "tcpsocket.h"

using namespace android;

#define ENABLE_AUDIO         1
//#define DEBUG_RECORD_WRITE_FILE 1

#define kCarlifeOutputSize	(1452)
#define SOCKET_AUDIO_LOCALHOST_PORT	(9200)

#if ENABLE_AUDIO
sp<AudioRecord> pAudioRecord = NULL;
uint32_t sampleRate = 48000;
int channel = 2;
size_t framecount = 0;
int file_number = 0;
#endif

#if ENABLE_AUDIO
static int carlife_audio_init()
{
#if defined(IS_ANDROID_24)
	String16 name16 = String16("carlife");
#endif
	LOGI("carlife_audio_init sampleRate: %d, channel: %d\n", sampleRate, channel); 
	AudioRecord::getMinFrameCount(&framecount, sampleRate, AUDIO_FORMAT_PCM_16_BIT, audio_channel_in_mask_from_count(channel));
	LOGI("carlife_audio_init framecount: %d\n", framecount);

#if defined(IS_ANDROID_19)
	pAudioRecord = new AudioRecord(AUDIO_SOURCE_REMOTE_SUBMIX, sampleRate, AUDIO_FORMAT_PCM_16_BIT,
								   audio_channel_in_mask_from_count(channel), framecount);
#elif defined(IS_ANDROID_24)
	pAudioRecord = new AudioRecord(AUDIO_SOURCE_REMOTE_SUBMIX, sampleRate, AUDIO_FORMAT_PCM_16_BIT,
								   audio_channel_in_mask_from_count(channel), name16, framecount);
#endif

	if(pAudioRecord == NULL)
	{
		LOGE("carlife_audio_init create AudioRecord failed\n");
		return -1;			
	}

	if(pAudioRecord->initCheck() != OK)
	{
		LOGE("carlife_audio_init init AudioRecord failed\n");
		return -1;
	}

	LOGD("carlife_audio_init init AudioRecord ok\n");

	return 0;
}
#endif


void *carlife_audio_record_thread(void *arg)
{
#define READ_AUDIO_MAX  2048
	int err = 0;
	char *pReadBuf = NULL;
	int iReadLen = 0;
	int iSendLen = 0;
	int nSock = 0;
	int fIsExit = 0;//TO DO: just for test 2019-04-01

	if(NULL != arg)
	{
		nSock = *((int *)arg);
		LOGI("carlife_audio_record_thread nSock:%d\n", nSock);
	}
	else
	{
		LOGI("carlife_audio_record_thread NULL==arg, return.\n");
		return NULL;
	}


#if DEBUG_RECORD_WRITE_FILE
	FILE *file = NULL;
	char file_name[128];
	sprintf(file_name ,"/data/local/tmp/carlife_record%d.pcm", file_number++);
	
	file = fopen(file_name, "wb");

	if (file == NULL)
	{
		LOGI("/data/local/tmp/carlife_record.pcm NULL errno: %d\n", errno);
	}
	else
	{
		LOGI("/data/local/tmp/carlife_record.pcm ok\n");
	}
#endif

	if(pAudioRecord == NULL)
	{
		err = carlife_audio_init();
		if (err != 0)
		{
			LOGE("carlife_audio_record_thread create AudioRecord failed\n");
			return NULL;
		}
	}

	pReadBuf = (char *)malloc(READ_AUDIO_MAX);
	if (pReadBuf == NULL)
	{
		LOGE("carlife_audio_record_thread no memory\n");
		return NULL;
	}
	
	pAudioRecord->start();
	while(1)
	{
		memset(pReadBuf, 0, READ_AUDIO_MAX);

		iReadLen = pAudioRecord->read(pReadBuf, kCarlifeOutputSize);
		if (iReadLen <= 0)
		{
			LOGE("carlife_audio_record_thread read err\n");
			continue;
		}

		iSendLen = htonl(iReadLen);

		tcp_send_data(nSock, &iSendLen, sizeof(int), fIsExit, 0);
		//LOGI("%s iReadLen:%d, %#x\n", __func__, iReadLen, iReadLen);

		tcp_send_data(nSock, pReadBuf, iReadLen, fIsExit, 0);
		
		
	#if DEBUG_RECORD_WRITE_FILE
		if(file)
		{
			fwrite(pReadBuf, 1, iReadLen, file);
		}
	#endif
	}

#if DEBUG_RECORD_WRITE_FILE
	fclose(file);
#endif

	if (pAudioRecord != NULL)
	{
		LOGI("carlife_audio_record_thread pAudioRecord->stop\n");
		pAudioRecord->stop();
		if (pAudioRecord->stopped())
		{
			LOGI("carlife_audio_record_thread pAudioRecord->stop end\n");
		}
	}

	if (pReadBuf)
	{
		free(pReadBuf);
	}

SEND_EXIT:
	
	closeSocket(nSock);

	LOGI("carlife_audio_record_thread exit\n");

	return NULL;
}


void *carlife_creat_audio_srv_thread(void *arg)
{
	bool isLocal;
	int err = 0;
	int sock = 0;
	int sock_n = 0;
	
	pthread_t tid_audio_record;
	memset(&tid_audio_record, 0, sizeof(pthread_t));

	pthread_detach(pthread_self());

	sock = createServer(SOCKET_AUDIO_LOCALHOST_PORT);
	if(sock <= 0)
	{
		LOGI("carlife_creat_audio_srv create server (port:%d) failed!\n", SOCKET_AUDIO_LOCALHOST_PORT);
		return NULL;
	}

	sock_n = serverAccept(sock, &isLocal);
	if(sock_n > 0)
	{
		err = pthread_create(&tid_audio_record, NULL, carlife_audio_record_thread, &sock_n);
		if(err != 0)
		{
			LOGE("carlife_audio_record_thread error\n");
		}
		else
		{
			LOGE("carlife_audio_record_thread ok\n");
		}
	}

	if(tid_audio_record)
	{
		pthread_join(tid_audio_record, NULL);
	}

	pthread_exit(0);

	return NULL;
}


int carlife_creat_audio_srv()
{
	pthread_t tid_audio_srv = 0;

	int	err = pthread_create(&tid_audio_srv, NULL, carlife_creat_audio_srv_thread, NULL);
	if(err != 0)
	{
		LOGE("carlife_creat_audio_srv error\n");
	}
	else
	{
		LOGE("carlife_creat_audio_srv ok\n");
	}

	return 0;
}
