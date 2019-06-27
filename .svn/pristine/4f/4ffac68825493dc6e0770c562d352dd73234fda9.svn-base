#ifndef		__H_SYS_LOG_H__
#define		__H_SYS_LOG_H__



#ifdef __cplusplus
extern "C" {
#endif

int log_init(const char * log_fname);
int log_time_init(const char * fname_prev);
void log_close();

int _log_print(const char *fmt, va_list argptr);
int log_print(const char * fmt,...);

int log_lock_start(const char * fmt,...);
int log_lock_print(const char * fmt,...);
int log_lock_end(const char * fmt,...);

#ifdef __cplusplus
}
#endif

#endif	//	__H_SYS_LOG_H__
