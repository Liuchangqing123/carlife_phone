/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\project\\apollo-DuerOS\\CarLife-Android-Vehicle\\src\\com\\baidu\\carlifevehicle\\bluetooth\\IHfpClientCallback.aidl
 */
package com.baidu.carlifevehicle.bluetooth;
public interface IHfpClientCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.baidu.carlifevehicle.bluetooth.IHfpClientCallback
{
private static final java.lang.String DESCRIPTOR = "com.baidu.carlifevehicle.bluetooth.IHfpClientCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.baidu.carlifevehicle.bluetooth.IHfpClientCallback interface,
 * generating a proxy if needed.
 */
public static com.baidu.carlifevehicle.bluetooth.IHfpClientCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.baidu.carlifevehicle.bluetooth.IHfpClientCallback))) {
return ((com.baidu.carlifevehicle.bluetooth.IHfpClientCallback)iin);
}
return new com.baidu.carlifevehicle.bluetooth.IHfpClientCallback.Stub.Proxy(obj);
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
case TRANSACTION_onIncomingCall:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.onIncomingCall(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_onOutgoingCall:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.onOutgoingCall(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_onCallActive:
{
data.enforceInterface(descriptor);
this.onCallActive();
reply.writeNoException();
return true;
}
case TRANSACTION_onCallInactive:
{
data.enforceInterface(descriptor);
this.onCallInactive();
reply.writeNoException();
return true;
}
case TRANSACTION_onConnectionStateChanged:
{
data.enforceInterface(descriptor);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
this.onConnectionStateChanged(_arg0, _arg1);
reply.writeNoException();
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.baidu.carlifevehicle.bluetooth.IHfpClientCallback
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
@Override public void onIncomingCall(java.lang.String phoneNumber, java.lang.String name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(phoneNumber);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_onIncomingCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onOutgoingCall(java.lang.String phoneNumber, java.lang.String name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(phoneNumber);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_onOutgoingCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onCallActive() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onCallActive, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onCallInactive() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onCallInactive, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onConnectionStateChanged(int state, java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(state);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_onConnectionStateChanged, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onIncomingCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onOutgoingCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onCallActive = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onCallInactive = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onConnectionStateChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
public void onIncomingCall(java.lang.String phoneNumber, java.lang.String name) throws android.os.RemoteException;
public void onOutgoingCall(java.lang.String phoneNumber, java.lang.String name) throws android.os.RemoteException;
public void onCallActive() throws android.os.RemoteException;
public void onCallInactive() throws android.os.RemoteException;
public void onConnectionStateChanged(int state, java.lang.String address) throws android.os.RemoteException;
}
