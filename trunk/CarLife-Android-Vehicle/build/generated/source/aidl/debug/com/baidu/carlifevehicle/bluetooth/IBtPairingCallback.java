/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\workspace\\carlife_phone\\trunk\\CarLife-Android-Vehicle\\src\\com\\baidu\\carlifevehicle\\bluetooth\\IBtPairingCallback.aidl
 */
package com.baidu.carlifevehicle.bluetooth;
public interface IBtPairingCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.baidu.carlifevehicle.bluetooth.IBtPairingCallback
{
private static final java.lang.String DESCRIPTOR = "com.baidu.carlifevehicle.bluetooth.IBtPairingCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.baidu.carlifevehicle.bluetooth.IBtPairingCallback interface,
 * generating a proxy if needed.
 */
public static com.baidu.carlifevehicle.bluetooth.IBtPairingCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.baidu.carlifevehicle.bluetooth.IBtPairingCallback))) {
return ((com.baidu.carlifevehicle.bluetooth.IBtPairingCallback)iin);
}
return new com.baidu.carlifevehicle.bluetooth.IBtPairingCallback.Stub.Proxy(obj);
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
case TRANSACTION_onPairingRequest:
{
data.enforceInterface(descriptor);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
this.onPairingRequest(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_onBondStateChange:
{
data.enforceInterface(descriptor);
int _arg0;
_arg0 = data.readInt();
this.onBondStateChange(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onHfpConnectionStateChanged:
{
data.enforceInterface(descriptor);
int _arg0;
_arg0 = data.readInt();
this.onHfpConnectionStateChanged(_arg0);
reply.writeNoException();
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.baidu.carlifevehicle.bluetooth.IBtPairingCallback
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
//mode could be following value:
//	public static final int PAIRING_VARIANT_PIN = 0;
//	public static final int PAIRING_VARIANT_PASSKEY = 1;
//	public static final int PAIRING_VARIANT_PASSKEY_CONFIRMATION = 2;
//address is for remote paired device

@Override public void onPairingRequest(int mode, java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(mode);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_onPairingRequest, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//state could be following value:
//public static final int BOND_NONE = 10;
//public static final int BOND_BONDING = 11; Indicates bonding (pairing) is in progress with the remote device
//public static final int BOND_BONDED = 12;Indicates the remote device is bonded (paired)

@Override public void onBondStateChange(int state) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(state);
mRemote.transact(Stub.TRANSACTION_onBondStateChange, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//state could be below value:
//public static final int STATE_DISCONNECTED  = 0;
//public static final int STATE_CONNECTING    = 1;
//public static final int STATE_CONNECTED     = 2;
//public static final int STATE_DISCONNECTING = 3;

@Override public void onHfpConnectionStateChanged(int state) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(state);
mRemote.transact(Stub.TRANSACTION_onHfpConnectionStateChanged, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onPairingRequest = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onBondStateChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onHfpConnectionStateChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
//mode could be following value:
//	public static final int PAIRING_VARIANT_PIN = 0;
//	public static final int PAIRING_VARIANT_PASSKEY = 1;
//	public static final int PAIRING_VARIANT_PASSKEY_CONFIRMATION = 2;
//address is for remote paired device

public void onPairingRequest(int mode, java.lang.String address) throws android.os.RemoteException;
//state could be following value:
//public static final int BOND_NONE = 10;
//public static final int BOND_BONDING = 11; Indicates bonding (pairing) is in progress with the remote device
//public static final int BOND_BONDED = 12;Indicates the remote device is bonded (paired)

public void onBondStateChange(int state) throws android.os.RemoteException;
//state could be below value:
//public static final int STATE_DISCONNECTED  = 0;
//public static final int STATE_CONNECTING    = 1;
//public static final int STATE_CONNECTED     = 2;
//public static final int STATE_DISCONNECTING = 3;

public void onHfpConnectionStateChanged(int state) throws android.os.RemoteException;
}
