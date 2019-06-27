
#ifndef	__H_HQUEUE_H__
#define	__H_HQUEUE_H__


/***********************************************************/
#define	HQ_PUT_WAIT		0x00000001
#define	HQ_GET_WAIT		0x00000002
#define	HQ_NO_EVENT		0x00000004
#define BOOL int

/***********************************************************/
typedef struct h_queue
{
	unsigned int	queue_mode;
	unsigned int	unit_num;
	unsigned int	unit_size;
	unsigned int	front;
	unsigned int	rear;
	unsigned int	queue_buffer;
	unsigned int	count_put_full;

	void *			queue_putMutex;	
	void *			queue_nnulEvent;
	void *			queue_nfulEvent;
}HQUEUE;


#ifdef __cplusplus
extern "C" {
#endif

/***********************************************************/
HQUEUE    * hqCreate(unsigned int unit_num,unsigned int unit_size,unsigned int queue_mode);
void 	 	hqDelete(HQUEUE * phq);

BOOL 	 	hqBufPut(HQUEUE * phq,char * buf);
BOOL 	 	hqBufGet(HQUEUE * phq,char * buf);

BOOL 	 	hqBufIsEmpty(HQUEUE * phq);
BOOL		hqBufIsFull(HQUEUE * phq);

char 	  * hqBufGetWait(HQUEUE * phq);
void 	 	hqBufGetWaitPost(HQUEUE * phq);

BOOL 	 	hqBufPeek(HQUEUE * phq,char * buf);

#ifdef __cplusplus
}
#endif

#endif



