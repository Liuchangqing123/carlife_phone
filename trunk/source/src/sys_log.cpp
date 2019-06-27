#include "sys_inc.h"

#include "sys_log.h"

/***********************************************************************\
 日志文件消息记录函数
\***********************************************************************/
static FILE * g_pLogFile = NULL;
static void * g_pLogMutex = NULL;

int log_init(const char * log_fname)
{
	log_close();
	g_pLogFile = fopen(log_fname,"w+");
	if(g_pLogFile == NULL)
	{
		printf("log init fopen[%s] failed[%s]\r\n",log_fname, strerror(errno));
		return -1;
	}

	g_pLogMutex = sys_os_create_mutex();
	if(g_pLogMutex == NULL)
	{
		printf("log init sem_init failed[%s]\r\n",strerror(errno));
		return -1;
	}
	
	return 0;
}

int log_time_init(const char * fname_prev)
{
	char fpath[256];
	time_t time_now = time(NULL);
	struct tm * st = localtime(&(time_now));
	sprintf(fpath,"%s-%04d%02d%02d_%02d%02d%02d.txt", fname_prev,
		st->tm_year+1900,st->tm_mon+1,st->tm_mday, st->tm_hour,st->tm_min,st->tm_sec);
	
	return log_init(fpath);
}

void log_close()
{
	if(g_pLogFile)
	{
		fclose(g_pLogFile);
		g_pLogFile = NULL;
	}

	if(g_pLogMutex)
	{
		sys_os_destroy_sig_mutx(g_pLogMutex);
		g_pLogMutex = NULL;
	}
}

int _log_print(const char *fmt, va_list argptr)
{
	int slen = 0;

	if(g_pLogFile == NULL || g_pLogMutex == NULL)
		return 0;

	sys_os_mutex_enter(g_pLogMutex);

	slen = vfprintf(g_pLogFile,fmt,argptr);
	fflush(g_pLogFile);

	sys_os_mutex_leave(g_pLogMutex);

	return slen;
}

int log_print(const char * fmt,...)
{
	va_list argptr;

	va_start(argptr,fmt);

	int slen = _log_print(fmt,argptr);

	va_end(argptr);

	return slen;
}

static int _log_lock_print(const char *fmt, va_list argptr)
{
	int slen = vfprintf(g_pLogFile,fmt,argptr);
	fflush(g_pLogFile);
	return slen;
}

int log_lock_start(const char * fmt,...)
{
	va_list argptr;
	va_start(argptr,fmt);
	int slen = 0;

	if(g_pLogFile == NULL || g_pLogMutex == NULL)
		return 0;

	sys_os_mutex_enter(g_pLogMutex);
	slen = _log_lock_print(fmt,argptr);
	va_end(argptr);

	return slen;
}

int log_lock_print(const char * fmt,...)
{
	va_list argptr;
	va_start(argptr,fmt);

	int slen = _log_lock_print(fmt, argptr);

	va_end(argptr);
	
	return slen;

}

int log_lock_end(const char * fmt,...)
{
	va_list argptr;
	va_start(argptr,fmt);

	int slen = _log_lock_print(fmt, argptr);

	va_end(argptr);

	sys_os_mutex_leave(g_pLogMutex);

	return slen;
}
