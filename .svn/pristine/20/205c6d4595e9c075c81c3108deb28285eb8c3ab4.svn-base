/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\project\\apollo-DuerOS\\CarLife-Android-Vehicle\\src\\com\\baidu\\carlifevehicle\\bluetooth\\IHfpClient.aidl
 */
package com.baidu.carlifevehicle.bluetooth;
public interface IHfpClient extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.baidu.carlifevehicle.bluetooth.IHfpClient
{
private static final java.lang.String DESCRIPTOR = "com.baidu.carlifevehicle.bluetooth.IHfpClient";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.baidu.carlifevehicle.bluetooth.IHfpClient interface,
 * generating a proxy if needed.
 */
public static com.baidu.carlifevehicle.bluetooth.IHfpClient asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.baidu.carlifevehicle.bluetooth.IHfpClient))) {
return ((com.baidu.carlifevehicle.bluetooth.IHfpClient)iin);
}
return new com.baidu.carlifevehicle.bluetooth.IHfpClient.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
java.lang.String descriptor = DESCRIPTOR;
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(descriptor);
return true;
}
case TRANSACTION_registerHfpCallback:
{
data.enforceInterface(descriptor);
com.baidu.carlifevehicle.bluetooth.IHfpClientCallback _arg0;
_arg0 = com.baidu.carlifevehicle.bluetooth.IHfpClientCallback.Stub.asInterface(data.readStrongBinder());
boolean _result = this.registerHfpCallback(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_unregisterHfpCallback:
{
data.enforceInterface(descriptor);
com.baidu.carlifevehicle.bluetooth.IHfpClientCallback _arg0;
_arg0 = com.baidu.carlifevehicle.bluetooth.IHfpClientCallback.Stub.asInterface(data.readStrongBinder());
boolean _result = this.unregisterHfpCallback(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_dial:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.dial(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_acceptCall:
{
data.enforceInterface(descriptor);
boolean _result = this.acceptCall();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_rejectCall:
{
data.enforceInterface(descriptor);
boolean _result = this.rejectCall();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_terminateCall:
{
data.enforceInterface(descriptor);
boolean _result = this.terminateCall();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_sendDTMF:
{
data.enforceInterface(descriptor);
byte _arg0;
_arg0 = data.readByte();
boolean _result = this.sendDTMF(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setMic:
{
data.enforceInterface(descriptor);
boolean _arg0;
_arg0 = (0!=data.readInt());
boolean _result = this.setMic(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getMic:
{
data.enforceInterface(descriptor);
boolean _result = this.getMic();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_blockNativeTelephone:
{
data.enforceInterface(descriptor);
boolean _arg0;
_arg0 = (0!=data.readInt());
boolean _result = this.blockNativeTelephone(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getVersion:
{
data.enforceInterface(descriptor);
java.lang.String _result = this.getVersion();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getConnectionState:
{
data.enforceInterface(descriptor);
int _result = this.getConnectionState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.baidu.carlifevehicle.bluetooth.IHfpClient
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public boolean registerHfpCallback(com.baidu.carlifevehicle.bluetooth.IHfpClientCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerHfpCallback, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean unregisterHfpCallback(com.baidu.carlifevehicle.bluetooth.IHfpClientCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterHfpCallback, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean dial(java.lang.String number) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(number);
mRemote.transact(Stub.TRANSACTION_dial, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean acceptCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_acceptCall, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean rejectCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_rejectCall, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean terminateCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_terminateCall, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean sendDTMF(byte code) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByte(code);
mRemote.transact(Stub.TRANSACTION_sendDTMF, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//Set mic status to mute or unmute,(TRUE: mute, FALSE: unmute)

@Override public boolean setMic(boolean enable) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((enable)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setMic, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//Get mic status, this function shall return current status for mic (TRUE: mute or FALSE: unmute)

@Override public boolean getMic() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMic, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//Service version, For example "0.1" as initial version,may changed as APIs modified
//block or unblock native telephone app,TRUE: block, FALSE: unblock

@Override public boolean blockNativeTelephone(boolean enable) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((enable)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_blockNativeTelephone, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getVersion() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getVersion, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getConnectionState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getConnectionState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_registerHfpCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_unregisterHfpCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_dial = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_acceptCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_rejectCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_terminateCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_sendDTMF = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_setMic = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getMic = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_blockNativeTelephone = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_getVersion = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_getConnectionState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
}
public boolean registerHfpCallback(com.baidu.carlifevehicle.bluetooth.IHfpClientCallback callback) throws android.os.RemoteException;
public boolean unregisterHfpCallback(com.baidu.carlifevehicle.bluetooth.IHfpClientCallback callback) throws android.os.RemoteException;
public boolean dial(java.lang.String number) throws android.os.RemoteException;
public boolean acceptCall() throws android.os.RemoteException;
public boolean rejectCall() throws android.os.RemoteException;
public boolean terminateCall() throws android.os.RemoteException;
public boolean sendDTMF(byte code) throws android.os.RemoteException;
//Set mic status to mute or unmute,(TRUE: mute, FALSE: unmute)

public boolean setMic(boolean enable) throws android.os.RemoteException;
//Get mic status, this function shall return current status for mic (TRUE: mute or FALSE: unmute)

public boolean getMic() throws android.os.RemoteException;
//Service version, For example "0.1" as initial version,may changed as APIs modified
//block or unblock native telephone app,TRUE: block, FALSE: unblock

public boolean blockNativeTelephone(boolean enable) throws android.os.RemoteException;
public java.lang.String getVersion() throws android.os.RemoteException;
public int getConnectionState() throws android.os.RemoteException;
}
