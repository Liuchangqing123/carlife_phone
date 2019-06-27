#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/time.h>
#include <unistd.h>
#include <netinet/tcp.h>
#include "log.h"

#define MAX_PAYLOAD_LENGTH			4096

bool canRecv(int sock, int timeout)
{
		timeval tv = {0, };
		fd_set fds;
	
		if(timeout >= 1000)
		{
				tv.tv_sec = timeout / 1000;
				tv.tv_usec = (timeout - tv.tv_sec * 1000) * 1000;
		}
		else
		{
				tv.tv_sec = 0;
				tv.tv_usec = timeout * 1000;
		}
	
		FD_ZERO(&fds);
		FD_SET(sock, &fds);
	
		if(select(sock+1, &fds, NULL, NULL, &tv) > 0)
		{
				if(FD_ISSET(sock, &fds))
				{
						return true;
				}
		}
	
		return false;
}

static bool canSend(int sock, int timeout)
{
		timeval tv = {0, };
		fd_set fds;
	
		if(timeout >= 1000)
		{
				tv.tv_sec = timeout / 1000;
				tv.tv_usec = (timeout - tv.tv_sec * 1000) * 1000;
		}
		else
		{
				tv.tv_sec = 0;
				tv.tv_usec = timeout * 1000;
		}
	
		FD_ZERO(&fds);
		FD_SET(sock, &fds);
	
		if(select(sock+1, 0, &fds, NULL, &tv) > 0)
		{				
				if(FD_ISSET(sock, &fds))
				{
						return true;
				}
		}
	
		return false;
}

bool isConnectionLost(int sock)
{
		struct tcp_info info;
		int len = sizeof(info);
		
		getsockopt(sock, IPPROTO_TCP, TCP_INFO, &info, (socklen_t *)&len);
		
		if(TCP_ESTABLISHED == info.tcpi_state)
			return false;
		return true;
}

bool sendData(int sock, void* data, int length, bool &fIsExit)
{
		bool success = false;
		unsigned char *ptr = (unsigned char *)data;
		int count = length;
		int size;

		do
		{
				if(isConnectionLost(sock))
				{
						break;
				}
				
				if(!canSend(sock, 10))
				{
						continue;
				}
				
				if(count > MAX_PAYLOAD_LENGTH)
						size = MAX_PAYLOAD_LENGTH;
				else
						size = count;
					
				int bytes = send(sock, ptr, size, 0);
				if(bytes > 0)
				{
						ptr += bytes;
						count -= bytes;
		
						if(0 == count)
						{
							success = true;
							break;
						}
				}
				else
				{
						break;
				}
		
		} while(!fIsExit);
		
		return success;
}

bool recvData(int sock, void* data, int length, bool &fIsExit)
{
		bool success = false;
		unsigned char *ptr = (unsigned char *)data;
		int count = length;
	
		do
		{
				if(isConnectionLost(sock))
				{
						break;
				}
				
				if(!canRecv(sock, 10))
				{
						continue;
				}
				
				int bytes = recv(sock, ptr, count, 0);
				if(bytes > 0)
				{
						ptr += bytes;
						count -= bytes;
		
						if(0 == count)
						{
							success = true;
							break;
						}
				}
				else
				{
						break;
				}
	
		} while(!fIsExit);
	
		return success;
}

bool recvDataWithTimeout(int sock, void* data, int length, int timeout, bool &fIsExit)
{
		bool success = false;
		unsigned char *ptr = (unsigned char *)data;
		int count = length;
	
		do
		{
				if(isConnectionLost(sock))
				{
						break;
				}
				
				if(!canRecv(sock, timeout))
				{
						break;
				}
				
				int bytes = recv(sock, ptr, count, 0);
				if(bytes > 0)
				{
						ptr += bytes;
						count -= bytes;
		
						if(0 == count)
						{
							success = true;
							break;
						}
				}
				else
				{
						break;
				}
				
		} while(!fIsExit);
	
		return success;
}

int createServer(int port)
{
		bool success = false;
  		int sock = 0;
		const int flag = 1;
		struct sockaddr_in addr = {0,};
		
		do
		{
			sock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
			if(sock <= 0)
			{
					LOGI("createServer socket failed: %d\n", errno);
					break;
			}
	
			if (setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, (const char *)&flag, sizeof(int)) < 0)
			{
					LOGI("createServer setsockopt failed: %d\n", errno);
			}
	
			addr.sin_family = AF_INET;
			addr.sin_addr.s_addr = htonl(INADDR_LOOPBACK);
			addr.sin_port = htons(port);
	
			if(bind(sock, (struct sockaddr *)&addr, sizeof(addr)) != 0)
			{
					LOGI("createServer socket bind: %d\n", errno);
					break;
			}
	
			if(listen(sock, 1) != 0)
			{
					LOGI("createServer socket listen: %d\n", errno);
					break;
			}
	
			success = true;
	
		} while(0);
			
		if(!success)
		{
				if(sock > 0)
				{
						close(sock);
						sock = 0;
				}			
		}
		
		return sock;
}

int clientConnect(char *ip, int port)
{
		bool success = false;
		int sock = 0;
		struct sockaddr_in addr = {0,};
		
		do
		{
			sock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
			if(sock <= 0)
			{
					break;
			}
	
			addr.sin_family = AF_INET;
			addr.sin_addr.s_addr = inet_addr(ip);
			addr.sin_port = htons(port);
	
			if(connect(sock, (struct sockaddr *)&addr, sizeof(struct sockaddr)) != 0)
			{
					break;
			}
	
			success = true;
	
		} while(0);
			
		if(!success)
		{
				if(sock > 0)
				{
						close(sock);
						sock = 0;
				}			
		}
		
		return sock;
}

void closeSocket(int sock)
{
		if(sock > 0)
		{
				shutdown(sock, SHUT_RDWR);
				close(sock);
				sock = 0;
		}
}

int serverAccept(int sock, bool *isLocal)
{
		struct sockaddr_in client = {0,};
		const int client_length = sizeof(client);
		int s = accept(sock, (struct sockaddr *)&client, (socklen_t *)&client_length);
		if(isLocal != NULL)
		{
				char from_ip[256] = {0,};	
				strcpy((char *)from_ip, inet_ntoa(client.sin_addr));
				
				if(0 == strcmp(from_ip, "127.0.0.1"))
				{
						*isLocal = true;
				}
				else
				{
						*isLocal = false;
				}
		}
		
		return s;
}

bool tcp_send_data(int sock, void* data, int length, int &fIsExit, int timeout)
{
		bool success = false;
		unsigned char *ptr = (unsigned char *)data;
		int count = length;
		int size;

		do
		{
				if(timeout > 0)
				{
					if(!canSend(sock, timeout))
					{
							break;
					}
				}
				
				size = count;
				
				int bytes = send(sock, ptr, size, 0);
				if(bytes > 0)
				{
						ptr += bytes;
						count -= bytes;
						
						if(0 == count)
						{
							success = true;
							break;
						}
				}
				else
				{
						break;
				}
				
		} while(!fIsExit);
		
		return success;
}
// EOF
