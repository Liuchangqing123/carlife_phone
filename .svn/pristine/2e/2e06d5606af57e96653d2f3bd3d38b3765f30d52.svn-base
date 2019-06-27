#ifndef TCP_SOCKET_H
#define TCP_SOCKET_H

bool canRecv(int sock, int timeout);
int createServer(int port);
void closeSocket(int sock);
int serverAccept(int sock, bool *isLocal = NULL);
int clientConnect(char *ip, int port);
bool sendData(int sock, void* data, int length, bool &fIsExit);
bool recvData(int sock, void* data, int length, bool &fIsExit);
bool isConnectionLost(int sock);
bool recvDataWithTimeout(int sock, void* data, int length, int timeout, bool &fIsExit);
bool tcp_send_data(int sock, void* data, int length, int &fIsExit, int timeout);

#endif // TCP_SOCKET_H
