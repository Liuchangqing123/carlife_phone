#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <pthread.h>
#include <dirent.h>
#include <signal.h>
#include <unistd.h>
#include <netdb.h>
#include <fcntl.h>  
#include <linux/netlink.h>
#include <sys/system_properties.h>
#include <sys/types.h>
#include <sys/stat.h>
#include "log.h"
#include "screen_record.h"
#include "audio.h"
/////////////////2019/04/11 xiaojun
#include "touch/config.h"
#include "touch/touch.h"
//end
#define LOCKFILE  "/data/local/tmp/carlife_main.pid"
#define SVN_CODE_NUMBER 1

int start_running(void)
{
	struct flock lock;
	char pids[10];
	int lfd;
	int ret = -1;
	
	ret = lfd = open(LOCKFILE, O_WRONLY | O_CREAT, 0644);
	if(ret == -1) 
	{
		LOGE("could not open lockfile");
		return ret;
	}
	
	lock.l_type = F_WRLCK;
	lock.l_whence = SEEK_SET;
	lock.l_start = 0;
	lock.l_len = 0;
	lock.l_pid = 0;
	ret = fcntl(lfd, F_GETLK, &lock);
	if (ret == -1)
	{
		LOGE("fcntl error\n");
		return ret;
	}

	close(lfd);
	LOGE("lock.l_type: %d\n", lock.l_type);
	
	if (lock.l_type != F_UNLCK) 
	{
		LOGE("another instance is already running (pid %d). exiting.\n", lock.l_pid);
		return 0;
	}
	unlink(LOCKFILE);
	
	/*	
	ret = daemon(0, 1);
	if (ret != 0) 
	{
		LOGI("could not run as daemon - exiting"); 
		exit(ret); 
	}
	*/
	
	ret = lfd = open(LOCKFILE, O_WRONLY|O_CREAT|O_TRUNC|O_EXCL, 0644);
	if(ret < 0) 
	{
		LOGE("could not open lockfile1");
		return ret;
	}
	
	lock.l_type = F_WRLCK;
	lock.l_whence = SEEK_SET;
	lock.l_start = 0;
	lock.l_len = 0;
	if ((ret = fcntl(lfd, F_SETLK, &lock)) < 0) 
	{
		LOGE("lockfile locking failed!");
		return ret;
	}
	
	LOGE("getpid: %d\n", getpid());
	sprintf(pids, "%d", getpid());
	if ((size_t)(ret = write(lfd, pids, strlen(pids))) != strlen(pids))
	{
		LOGE("could not write pidfile!");
		return 0;
	}	
	return 1;
}

static void exitHandler(int sig)
{
	LOGD("exitHandler: %d\n", sig);
}

void switch_to_usb_device_mode(void)
{
	system("cat /sys/devices/soc/usbc0/usb_device > /dev/null");
	system("echo 1 > /sys/class/android_usb/android/enable");
	
	LOGD("%s:%d\n", __func__, __LINE__);
}

int main(int argc, char** argv)
{
	struct sigaction act;
	struct sockaddr_nl sa;
	struct iovec iov;
	struct msghdr msg;
	struct timeval timeout;
	fd_set readSet;

	char maybecarplay = 0;
	char recv_buf[1024];

	int on = 1;
	int sock_fd;
	int ret = -1;
	int sz = sizeof(recv_buf);
	int usb_flag = 0;


	unsigned int wait_for_disconnect = 0;
	if(argc == 2 && ((strcmp(argv[1], "-v") == 0) || (strcmp(argv[1], "-V") == 0)))
	{
		printf("%d\n" ,SVN_CODE_NUMBER );
		return 0;
	}
	if (start_running() != 1)
	{
		return 0;
	}

	memset(&act, 0, sizeof(act));
	act.sa_handler = exitHandler;
	sigemptyset(&act.sa_mask);
	sigaddset(&act.sa_mask, SIGTERM);
	//	sigaddset(&act.sa_mask, SIGINT);
	sigaddset(&act.sa_mask, SIGQUIT);
	sigaddset(&act.sa_mask, SIGHUP);
	sigaddset(&act.sa_mask, SIGUSR1);

	sigaction(SIGTERM, &act, NULL);
	//	sigaction(SIGINT, &act, NULL);
	sigaction(SIGQUIT, &act, NULL);

	memset(&act, 0, sizeof(act));
	act.sa_handler = SIG_IGN;
	sigemptyset(&act.sa_mask);
	sigaction(SIGPIPE, &act, NULL);
	sigaction(SIGABRT, &act, NULL);

	sleep(6);

	switch_to_usb_device_mode();

	carlife_creat_video_srv();
	carlife_creat_audio_srv();
	carlife_creat_touch_srv();//2019/04/11 

	do{
		sleep(5);
	}while(1);

	// 
	return 0;
}

