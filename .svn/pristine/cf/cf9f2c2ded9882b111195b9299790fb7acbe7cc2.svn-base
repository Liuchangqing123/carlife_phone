#include "sys_inc.h"

 void * sys_os_create_mutex()
{
	void * p_mutex = NULL;

#ifdef IOS
    static int index = 0;
    char name[20];
    sprintf(name, "testmutex%d", index++);
    
    p_mutex = sem_open(name,O_CREAT, 0644, 1);
    if(p_mutex == SEM_FAILED)
    {
        log_print("sem_open failed. name=%s\r\n", name);
        return NULL;
    }
    
#elif	__WIN32_OS__

	p_mutex  = CreateMutex(NULL, FALSE, NULL);

#elif (__LINUX_OS__ || __VXWORKS_OS__)

	p_mutex = (sem_t *)malloc(sizeof(sem_t));
	int ret = sem_init((sem_t *)p_mutex,0,1);
	if(ret != 0)
	{
		free(p_mutex);
		return NULL;
	}

#endif
	return p_mutex;
}

 void * sys_os_create_sig()
{
	void * p_sig = NULL;
    
#ifdef IOS
    
    static int index = 0;
    char name[20];
    sprintf(name, "testsig%d", index++);
    
    p_sig = sem_open(name,O_CREAT, 0644, 0);
    if(p_sig == SEM_FAILED)
    {
        log_print("sem_open failed. name=%s\r\n", name);
        return NULL;
    }
    
#elif	__WIN32_OS__

	p_sig = CreateEvent(NULL, FALSE, FALSE, NULL);

#elif (__LINUX_OS__ || __VXWORKS_OS__)

	p_sig = malloc(sizeof(sem_t));
	int ret = sem_init((sem_t *)p_sig,0,0);
	if(ret != 0)
	{
		free(p_sig);
		return NULL;
	}

#endif

	return p_sig;
}

 void sys_os_destroy_sig_mutx(void * ptr)
{
	if(ptr == NULL)
		return;

#ifdef IOS
    
    sem_close((sem_t *)ptr);
    
#elif	__WIN32_OS__

	CloseHandle(ptr);

#elif (__LINUX_OS__ || __VXWORKS_OS__)

	sem_destroy((sem_t *)ptr);
	free(ptr);

#endif
}

 int sys_os_mutex_enter(void * p_sem)
{
	if(p_sem == NULL)
		return -1;

#if	(__VXWORKS_OS__ || __LINUX_OS__)
	int ret = sem_wait((sem_t *)p_sem);
	if(ret != 0)
		return -1;
#elif	__WIN32_OS__
	DWORD ret = WaitForSingleObject(p_sem,INFINITE);
	if(ret == WAIT_FAILED)
		return -1;
#endif
	return 0;
}

 void sys_os_mutex_leave(void * p_sem)
{
	if(p_sem == NULL)
		return;

#if	(__VXWORKS_OS__ || __LINUX_OS__)
	sem_post((sem_t *)p_sem);
#elif	__WIN32_OS__
	ReleaseMutex(p_sem);
#endif
}

 int sys_os_sig_wait(void * p_sig)
{
	if(p_sig == NULL)
		return -1;

#if	(__VXWORKS_OS__ || __LINUX_OS__)
	int ret = sem_wait((sem_t *)p_sig);
	if(ret != 0)
		return -1;
#elif	__WIN32_OS__
	DWORD ret = WaitForSingleObject(p_sig,INFINITE);
	if(ret == WAIT_FAILED)
		return -1;
#endif
	return 0;
}

 int sys_os_sig_wait_timeout(void * p_sig, unsigned int ms)
{
	if(p_sig == NULL) return -1;

#ifdef IOS
    
    while (ms > 0)
    {
        if (sem_trywait((sem_t *)p_sig) == 0)
        {
            return 0;
        }
        
        usleep(1000);
        ms -= 1;
    }
    
    return -1;
    
#elif	(__VXWORKS_OS__ || __LINUX_OS__)
	struct timespec ts;
	struct timeval tt;
	gettimeofday(&tt,NULL);

	tt.tv_sec = tt.tv_sec + ms / 1000;
	tt.tv_usec = tt.tv_usec + (ms % 1000) * 1000;
	tt.tv_sec += tt.tv_usec / (1000 * 1000);
	tt.tv_usec = tt.tv_usec % (1000 * 1000);
	
	ts.tv_sec = tt.tv_sec;
	ts.tv_nsec = tt.tv_usec * 1000;

	int ret = sem_timedwait((sem_t *)p_sig, &ts);
	if(ret == -1 && errno == ETIMEDOUT)
		return -1;
	else
		return 0;

#elif	__WIN32_OS__
	DWORD ret = WaitForSingleObject(p_sig, ms);
	if(ret == WAIT_FAILED)
		return -1;
	else if(ret == WAIT_TIMEOUT)
		return -1;
#endif
	return 0;
}

 void sys_os_sig_sign(void * p_sig)
{
	if(p_sig == NULL)
		return;
    
#if	(__VXWORKS_OS__ || __LINUX_OS__)
	sem_post((sem_t *)p_sig);
#elif	__WIN32_OS__
	SetEvent(p_sig);
#endif
}

 pthread_t sys_os_create_thread(void * thread_func, void * argv)
{
	pthread_t tid = 0;

#if	(__VXWORKS_OS__ || __LINUX_OS__)

//	typedef void *(*start_routine)(void*);

	int ret = pthread_create(&tid,NULL,(void *(*)(void *))thread_func,argv);
//	int ret = pthread_create(&tid,NULL,thread_func,argv);
	if(ret != 0)
	{
		perror("sys_os_create_thread::pthread_create failed!!!");
		exit(EXIT_FAILURE);
	}

#elif	__WIN32_OS__

	HANDLE hret = CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)thread_func, argv, 0, &tid);
	if(hret == NULL || tid == 0)
	{
		DWORD err = GetLastError();
		log_print("sys_os_create_thread::CreateThread hret=%u, tid=%u, err=%u\r\n", hret, tid, err);
	}

#endif

	return tid;
}

 unsigned int sys_os_get_ms()
{
	unsigned int ms = 0;

#if __LINUX_OS__

	struct timeval tv;
	gettimeofday(&tv, NULL);
	ms = tv.tv_sec * 1000 + tv.tv_usec/1000;

#elif __WIN32_OS__

	ms = GetTickCount();

#endif

	return ms;
}

 char * sys_os_get_socket_error()
{
	char * p_estr = NULL;
#if __LINUX_OS__
	p_estr = strerror(errno);
#elif	__WIN32_OS__
	int err = WSAGetLastError();
	static char serr_code_buf[24];
	sprintf(serr_code_buf,"WSAE-%d", err);
	p_estr = serr_code_buf;
#endif
	return p_estr;
}

/***********************************************************************\
 内存检测
\***********************************************************************/
static FILE * pMemLogFile = NULL;
static void * logMemFileMutex = NULL;

 void mem_log_quit()
{
	if(pMemLogFile)
	{
		fclose(pMemLogFile);
		pMemLogFile = NULL;
	}

	if(logMemFileMutex)
	{
		sys_os_destroy_sig_mutx(logMemFileMutex);
		logMemFileMutex = NULL;
	}
}

 int _mem_log_print(char *fmt, va_list argptr)
{
	int slen = 0;

	if(pMemLogFile == NULL)
	{
		pMemLogFile = fopen("./ric_mem_log.txt","wb+");
		logMemFileMutex = sys_os_create_mutex();
		if(logMemFileMutex == NULL)
		{
			printf("log init sem_init failed[%s]\r\n",strerror(errno));
		}
	}

	if(pMemLogFile == NULL)
		return 0;

	sys_os_mutex_enter(logMemFileMutex);

	slen = vfprintf(pMemLogFile,fmt,argptr);
	fflush(pMemLogFile);

	sys_os_mutex_leave(logMemFileMutex);

	return slen;
}

 int mem_log_print(char * fmt,...)
{
#if 1
	va_list argptr;

	va_start(argptr,fmt);
	
	int slen = _mem_log_print(fmt,argptr);
	
	va_end(argptr);
	
	return slen;
#else
	return 0;
#endif
}

 void * xmalloc(size_t size, char * pFileName, int line)
{
	void * ptr = malloc(size);
//	mem_log_print("+++0X%p, %u, file[%s],line[%d]\r\n", ptr, size, pFileName, line);
	return ptr;
}

 void xfree(void * ptr, char * pFileName, int line)
{
//	mem_log_print("---0X%p, file[%s],line[%d]\r\n", ptr, pFileName, line);
	free(ptr);
}


