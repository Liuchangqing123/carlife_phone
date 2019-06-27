/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\project\\apollo-DuerOS\\CarLife-Android-Vehicle\\src\\com\\baidu\\carlifevehicle\\bluetooth\\IBtPairing.aidl
 */
package com.baidu.carlifevehicle.bluetooth;
public interface IBtPairing extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.baidu.carlifevehicle.bluetooth.IBtPairing
{
private static final java.lang.String DESCRIPTOR = "com.baidu.carlifevehicle.bluetooth.IBtPairing";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.baidu.carlifevehicle.bluetooth.IBtPairing interface,
 * generating a proxy if needed.
 */
public static com.baidu.carlifevehicle.bluetooth.IBtPairing asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.baidu.carlifevehicle.bluetooth.IBtPairing))) {
return ((com.baidu.carlifevehicle.bluetooth.IBtPairing)iin);
}
return new com.baidu.carlifevehicle.bluetooth.IBtPairing.Stub.Proxy(obj);
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
com.baidu.carlifevehicle.bluetooth.IBtPairingCallback _arg0;
_arg0 = com.baidu.carlifevehicle.bluetooth.IBtPairingCallback.Stub.asInterface(data.readStrongBinder());
boolean _result = this.registerHfpCallback(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_unregisterHfpCallback:
{
data.enforceInterface(descriptor);
com.baidu.carlifevehicle.bluetooth.IBtPairingCallback _arg0;
_arg0 = com.baidu.carlifevehicle.bluetooth.IBtPairingCallback.Stub.asInterface(data.readStrongBinder());
boolean _result = this.unregisterHfpCallback(_arg0);
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
case TRANSACTION_getAddress:
{
data.enforceInterface(descriptor);
java.lang.String _result = this.getAddress();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getName:
{
data.enforceInterface(descriptor);
java.lang.String _result = this.getName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getPincode:
{
data.enforceInterface(descriptor);
java.lang.String _result = this.getPincode();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_isEnabled:
{
data.enforceInterface(descriptor);
boolean _result = this.isEnabled();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_enable:
{
data.enforceInterface(descriptor);
boolean _result = this.enable();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_disable:
{
data.enforceInterface(descriptor);
boolean _result = this.disable();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getHfpConnectionState:
{
data.enforceInterface(descriptor);
int _result = this.getHfpConnectionState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getConnectedDeviceAddress:
{
data.enforceInterface(descriptor);
java.lang.String _result = this.getConnectedDeviceAddress();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_disconnect:
{
data.enforceInterface(descriptor);
boolean _result = this.disconnect();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getBondedDevicesAddress:
{
data.enforceInterface(descriptor);
java.lang.String _result = this.getBondedDevicesAddress();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_setPin:
{
data.enforceInterface(descriptor);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.setPin(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setPairingConfirmation:
{
data.enforceInterface(descriptor);
boolean _arg0;
_arg0 = (0!=data.readInt());
boolean _result = this.setPairingConfirmation(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.baidu.carlifevehicle.bluetooth.IBtPairing
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
@Override public boolean registerHfpCallback(com.baidu.carlifevehicle.bluetooth.IBtPairingCallback callback) throws android.os.RemoteException
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
@Override public boolean unregisterHfpCallback(com.baidu.carlifevehicle.bluetooth.IBtPairingCallback callback) throws android.os.RemoteException
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
@Override public java.lang.String getAddress() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAddress, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getPincode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPincode, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//Return TRUE if bluetooth adapter is ready for user currently 

@Override public boolean isEnabled() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isEnabled, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//Turning on local bluetooth adapter

@Override public boolean enable() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_enable, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//Turning off local bluetooth adapter

@Override public boolean disable() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_disable, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getHfpConnectionState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getHfpConnectionState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getConnectedDeviceAddress() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getConnectedDeviceAddress, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//Disconnect with remote device in case of connecting with other device rather other MD
//bluetooth adapter should get rid of its current connection

@Override public boolean disconnect() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_disconnect, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getBondedDevicesAddress() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getBondedDevicesAddress, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// Set pin during pairing when pairing method is PIN CODE mode
// This method is not required when Head Unit take the charge for typing pin code as retrieved an incoming pairing request

@Override public boolean setPin(java.lang.String pin) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pin);
mRemote.transact(Stub.TRANSACTION_setPin, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// This method is not required when Head Unit take the charge for accept or reject incoming pairing request.

@Override public boolean setPairingConfirmation(boolean accept) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((accept)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setPairingConfirmation, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
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
static final int TRANSACTION_getVersion = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getAddress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getPincode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_isEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_enable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_disable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getHfpConnectionState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_getConnectedDeviceAddress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_disconnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_getBondedDevicesAddress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_setPin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_setPairingConfirmation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
}
public boolean registerHfpCallback(com.baidu.carlifevehicle.bluetooth.IBtPairingCallback callback) throws android.os.RemoteException;
public boolean unregisterHfpCallback(com.baidu.carlifevehicle.bluetooth.IBtPairingCallback callback) throws android.os.RemoteException;
public java.lang.String getVersion() throws android.os.RemoteException;
public java.lang.String getAddress() throws android.os.RemoteException;
public java.lang.String getName() throws android.os.RemoteException;
public java.lang.String getPincode() throws android.os.RemoteException;
//Return TRUE if bluetooth adapter is ready for user currently 

public boolean isEnabled() throws android.os.RemoteException;
//Turning on local bluetooth adapter

public boolean enable() throws android.os.RemoteException;
//Turning off local bluetooth adapter

public boolean disable() throws android.os.RemoteException;
public int getHfpConnectionState() throws android.os.RemoteException;
public java.lang.String getConnectedDeviceAddress() throws android.os.RemoteException;
//Disconnect with remote device in case of connecting with other device rather other MD
//bluetooth adapter should get rid of its current connection

public boolean disconnect() throws android.os.RemoteException;
public java.lang.String getBondedDevicesAddress() throws android.os.RemoteException;
// Set pin during pairing when pairing method is PIN CODE mode
// This method is not required when Head Unit take the charge for typing pin code as retrieved an incoming pairing request

public boolean setPin(java.lang.String pin) throws android.os.RemoteException;
// This method is not required when Head Unit take the charge for accept or reject incoming pairing request.

public boolean setPairingConfirmation(boolean accept) throws android.os.RemoteException;
}
