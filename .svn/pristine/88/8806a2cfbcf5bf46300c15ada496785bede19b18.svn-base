#ifndef		__SYS_INC_H__
#define		__SYS_INC_H__

#define __WIN32_OS__	0
#define __VXWORKS_OS__	0
#define __LINUX_OS__	1

#if	__WIN32_OS__

#include "stdafx.h"

#include <stdlib.h>
#include <stdio.h>
#include <malloc.h>
#include <string.h>
#include <time.h>
#include <process.h>    /* _beginthread, _endthread */
#include <iphlpapi.h>
#include <assert.h>

#include <mysql.h>

#define sleep(x) Sleep(x)

#elif	__LINUX_OS__

typedef unsigned int	__u32;
typedef unsigned short	__u16;

#include <sys/types.h>
#include <sys/ipc.h>
//#include <sys/sem.h>
#include <semaphore.h>
#include <signal.h>

#include <sys/ioctl.h>
#include <sys/socket.h>
#include <linux/netlink.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <netinet/udp.h>
#include <netinet/tcp.h>

#include <arpa/inet.h>
#include <netdb.h>

#include <net/if.h>
#include <sys/stat.h>
#include <sys/resource.h>
#include <sys/prctl.h>
#include <sys/epoll.h>
#include <sys/time.h>
#include <sys/vfs.h>

//#include <expat.h>
//#include <iconv.h>

#include <ctype.h>
#include <unistd.h>
#include <stdarg.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <errno.h>
#include <string.h>
#include <time.h>
#include <assert.h>
#include <pthread.h>
#include <mntent.h>

#include <dlfcn.h>
#include <dirent.h>

//#include <mysql/mysql.h>

#ifndef BOOL_DEF
typedef int                     BOOL;
#define BOOL_DEF
#endif

typedef unsigned int	UINT;
typedef int				SOCKET;

#define TRUE	1
#define	FALSE	0

#define	closesocket close

#define HANDLE	sem_t*

#endif


#include "sys_log.h"

//#include "rc4.h"
//#include "hqueue.h"
//#include "ppstack.h"
//#include "linked_list.h"
//#include "rfc_md5.h"
//#include "digcalc.h"
//#include "ihash.h"
/*************************************************************************/
#ifdef __cplusplus
extern "C" {
#endif

void * sys_os_create_mutex();
void * sys_os_create_sig();

void sys_os_destroy_sig_mutx(void * ptr);

int sys_os_mutex_enter(void * p_sem);
void sys_os_mutex_leave(void * p_sem);

int sys_os_sig_wait(void * p_sig);
int sys_os_sig_wait_timeout(void * p_sig, unsigned int ms);
void sys_os_sig_sign(void * p_sig);

pthread_t sys_os_create_thread(void * thread_func, void * argv);

unsigned int sys_os_get_ms();
char * sys_os_get_socket_error();

int cnt_sprintf(const char * fmt, va_list argptr);

int base64_encode(unsigned char *source, unsigned int sourcelen, char *target, unsigned int targetlen);
int base64_decode(char *source, unsigned char *target, unsigned int targetlen);
void str_b64decode(char* str);

#ifdef __cplusplus
}
#endif

#endif	//	__SYS_INC_H__
