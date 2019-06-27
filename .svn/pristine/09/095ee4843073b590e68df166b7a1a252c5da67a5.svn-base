
#include <binder/Parcel.h>
#include <utils/String8.h>
#ifdef DEBUG
#if defined(IS_ANDROID_14) || defined(IS_ANDROID_15)
#include <utils/TextOutput.h>
#elif defined(IS_ANDROID_16) || defined(IS_ANDROID_17) || defined(IS_ANDROID_18)
#include <utils/TextOutput.h>
#elif defined(IS_ANDROID_19) || defined(IS_ANDROID_20) || defined(IS_ANDROID_21) || defined(IS_ANDROID_22) || defined(IS_ANDROID_23)
#include <binder/TextOutput.h>
#endif
#endif

#include <private/binder/Static.h>
#include "IInputManager.h"

#include "config.h"
//2019/04/11 xiaojun
#include <android/log.h>
#define CARLIFE_TOUCH_TAG "CARLIFE_TOUCH"


#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_INFO, CARLIFE_TOUCH_TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_INFO, CARLIFE_TOUCH_TAG, fmt, ##args)
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO, CARLIFE_TOUCH_TAG, fmt, ##args)

//end 


namespace android {

// ----------------------------------------------------------------------------------------------------------

class BpInputManager : public BpInterface<IInputManager>
{
public:
    BpInputManager(const sp<IBinder>& impl)
        : BpInterface<IInputManager>(impl)
    {
    		char prop_sdk[] = "\x0d\x10\x51\x1d\x0a\x16\x13\x1b\x51\x09\x1a\x0d\x0c\x16\x10\x11\x51\x0c\x1b\x14"; // ro.build.version.sdk
    		char prop_manufacturer[] = "\x0d\x10\x51\x0f\x0d\x10\x1b\x0a\x1c\x0b\x51\x12\x1e\x11\x0a\x19\x1e\x1c\x0b\x0a\x0d\x1a\x0d"; // ro.product.manufacturer
    		char prop_model[] = "\x0d\x10\x51\x0f\x0d\x10\x1b\x0a\x1c\x0b\x51\x12\x10\x1b\x1a\x13"; // ro.product.model
    		char samsung[] = "\x0c\x1e\x12\x0c\x0a\x11\x18"; // samsung
    		char huawei[] = "\x37\x2a\x3e\x28\x3a\x36"; // HUAWEI
    		char sm_a3000[] = "\x2c\x32\x52\x3e\x4c\x4f\x4f\x4f"; // SM-A3000
    		int i, len;
    		
    		// ro.build.version.sdk
    		len = strlen(prop_sdk);
    		for(i = 0; i < len; i++)
    		{
    			prop_sdk[i] = prop_sdk[i] ^ DEF_STR_XOR_CODE;
    		}
    		
    		// ro.product.manufacturer
    		len = strlen(prop_manufacturer);
    		for(i = 0; i < len; i++)
    		{
    			prop_manufacturer[i] = prop_manufacturer[i] ^ DEF_STR_XOR_CODE;
    		}
    		
    		// ro.product.model
    		len = strlen(prop_model);
    		for(i = 0; i < len; i++)
    		{
    			prop_model[i] = prop_model[i] ^ DEF_STR_XOR_CODE;
    		}
    		
    		// samsung
    		len = strlen(samsung);
    		for(i = 0; i < len; i++)
    		{
    			samsung[i] = samsung[i] ^ DEF_STR_XOR_CODE;
    		}
    		
    		// HUAWEI
    		len = strlen(huawei);
    		for(i = 0; i < len; i++)
    		{
    			huawei[i] = huawei[i] ^ DEF_STR_XOR_CODE;
    		}
    		
    		// SM-A3000
    		len = strlen(sm_a3000);
    		for(i = 0; i < len; i++)
    		{
    			sm_a3000[i] = sm_a3000[i] ^ DEF_STR_XOR_CODE;
    		}
    		
    		is_samsung_device = false;
    		is_huawei_device = false;
    		
    		memset(version, 0, PROP_VALUE_MAX);
    		memset(manufacturer, 0, PROP_VALUE_MAX);
    		memset(model, 0, PROP_VALUE_MAX);
    		
    		__system_property_get(prop_sdk, version);
    		LOGI("android %s\n", version);
    		__system_property_get(prop_manufacturer, manufacturer);
			LOGI("manufacturer %s\n", manufacturer);
			__system_property_get(prop_model, model);
			LOGI("model %s\n", model);
				
			if( 0 == strncasecmp(samsung, manufacturer, 7) && strncasecmp(sm_a3000, model, 8) != 0)
			{
				is_samsung_device = true;
			}
			else if( 0 == strncasecmp(huawei, manufacturer, 6) )
			{
				is_huawei_device = true;
			}
    	}

		virtual bool injectKeyEvent(int keyCode, int action, int flags, int repeatCount)
		{    		
				const nsecs_t now = systemTime()/1000;
				Parcel data, reply;
				
				data.writeInterfaceToken(IInputManager::getInterfaceDescriptor());
        
		        // Valid !0
		        data.writeInt32(1);
		        
		        // Token
		        data.writeInt32(PARCEL_TOKEN_KEY_EVENT);
		        
		        // KeyEvent
		        data.writeInt32(-1); // Device Id
		      	data.writeInt32(AINPUT_SOURCE_KEYBOARD); // Source
				data.writeInt32(action); // Action
				data.writeInt32(keyCode); // Key Code
				data.writeInt32(repeatCount); // Repeat Count
				data.writeInt32(AMETA_NONE); // Meta State
				data.writeInt32(0); // Scan Code
				data.writeInt32(flags); // Flags
				
				if(is_samsung_device)
						data.writeInt32(0); // Sansung Flags
				else if(is_huawei_device)
						data.writeInt32(0);	// HW Flags
				
				data.writeInt64(now); // Down Time
				data.writeInt64(now); // Event Time
      	
				// Mode
		        data.writeInt32(INJECT_INPUT_EVENT_MODE_ASYNC);
		        
		        remote()->transact(INJECT_INPUT_EVENT, data, &reply);
        
#ifdef DEBUG
        		aout << "INJECT_KEY_EVENT: " << reply << endl;
#endif

		        // fail on exception
		        if (reply.readExceptionCode() != 0)
		        {        	 
		        	 return false;
		        }
		        else
		        {
		        	if(1 == reply.readInt32())
		        	{
		        		return true;
		        	}
		        }
		        
		        return false;
		}
		
		virtual bool injectMotionEvent(int action, const int ptCount, const PointerProperties* ptPro, const int sampleCount, const PointerCoords* ptCoords)
		{
				const nsecs_t now = systemTime();
				
				Parcel data, reply;
				
				data.writeInterfaceToken(IInputManager::getInterfaceDescriptor());
        
		        // Valid
		        data.writeInt32(1);
		        
		        // Token
		        data.writeInt32(PARCEL_TOKEN_MOTION_EVENT);
		        
		        // MotionEvent
		        data.writeInt32(ptCount); // Pointer Count
		        data.writeInt32(sampleCount); // Sample Count
		        
		        data.writeInt32(0); // Device Id
		    	data.writeInt32(AINPUT_SOURCE_TOUCHSCREEN); // Source AINPUT_SOURCE_TOUCHSCREEN
		    	data.writeInt32(action); // Action
#if defined(IS_ANDROID_16) || defined(IS_ANDROID_17) || defined(IS_ANDROID_18) || defined(IS_ANDROID_19) || defined(IS_ANDROID_21) || defined(IS_ANDROID_22)
#else
				data.writeInt32(0); // Action Button
#endif
				if(is_samsung_device)
					data.writeInt32(0); // Add???
			    data.writeInt32(0); // Flags
			    data.writeInt32(0); // Edge Flags
			    data.writeInt32(AMETA_NONE); // Meta State
			    data.writeInt32(0); // Button State
			    data.writeFloat(0.0f); // X Offset
			    data.writeFloat(0.0f); // Y Offset
			    data.writeFloat(1.0f); // X Precision
			    data.writeFloat(1.0f); // Y Precision
			    data.writeInt64(now); // Down Time
        
		        for(int i = 0; i < ptCount; i++)
		       	{
		       			const int id = ptPro[i].id;
		       			const int toolType = ptPro[i].toolType;
		       			
		       			data.writeInt32(id); // Id
		        		data.writeInt32(toolType); // Tool Type
		       	}
		       	
		       	for(int i = 0; i < sampleCount; i++)
		       	{
		       			data.writeInt64(now); // Event Time
		       			
		       			for(int j = 0; j < ptCount; j++)
		       			{
		       					ptCoords[j].writeToParcel(&data);
		       			}
		       	}
		        
		        // Mode
		        data.writeInt32(INJECT_INPUT_EVENT_MODE_ASYNC);
		        
		        remote()->transact(INJECT_INPUT_EVENT, data, &reply);
        
#ifdef DEBUG
        		aout << "INJECT_MOTION_EVENT: " << reply << endl;
#endif

		        // fail on exception
		        if (reply.readExceptionCode() != 0)
		        {        	 
		        	 return false;
		        }
		        else
		        {
		        	if(1 == reply.readInt32())
		        	{
		        		return true;
		        	}
		        }
		        
		        return false;
		}
		
    virtual bool injectInputEvent(InputEvent* ev, int mode) 
    {
		if(NULL == ev)
		{
			LOGI("Input event is null.");
			return false;
		}
    		
        Parcel data, reply;
        const int32_t type = ev->getType();
        
        data.writeInterfaceToken(IInputManager::getInterfaceDescriptor());
        
        // Valid !0
        data.writeInt32(1);
        
        // InputEvent
        if(AINPUT_EVENT_TYPE_KEY == type)
        {
        	// Token
        	data.writeInt32(PARCEL_TOKEN_KEY_EVENT);
        	
        	const KeyEvent* kv = static_cast<const KeyEvent*>(ev);
        	
        	data.writeInt32(kv->getDeviceId()); // Device Id
        	data.writeInt32(kv->getSource()); // Source        	
			data.writeInt32(kv->getAction()); // Action										
			data.writeInt32(kv->getKeyCode()); // KeyCode									
			data.writeInt32(kv->getRepeatCount()); // RepeatCount															
			data.writeInt32(kv->getMetaState()); // MetaState					
			data.writeInt32(kv->getScanCode()); // ScanCode
			data.writeInt32(kv->getFlags()); // Flags							
			data.writeInt64(kv->getDownTime()); // DownTime
			data.writeInt64(kv->getEventTime()); // EventTime					
        }
        else if(AINPUT_EVENT_TYPE_MOTION == type)
        {
        	// Token
        	data.writeInt32(PARCEL_TOKEN_MOTION_EVENT);
        	
        	const MotionEvent* mv = static_cast<const MotionEvent*>(ev);
        	
        	mv->writeToParcel(&data);
        }
        else
        {
        	LOGI("Input event type invalid.");
        	return false;        	
        }
        
        // Mode
        data.writeInt32(mode);
        
        remote()->transact(INJECT_INPUT_EVENT, data, &reply);
        
#ifdef DEBUG
        aout << "INJECT_INPUT_EVENT: " << reply << endl;
#endif

        // fail on exception
        if (reply.readExceptionCode() != 0)
        {        	 
        	 return false;
        }
        else
        {
        	if(1 == reply.readInt32())
        	{
        		return true;
        	}
        }
        
        return false;
    }

};

IMPLEMENT_META_INTERFACE(InputManager, "android.hardware.input.IInputManager");

// -----------------------------------------------------------------------------------------------------------

status_t BnInputManager::onTransact(
    uint32_t code, const Parcel& data, Parcel* reply, uint32_t flags)
{
    return BBinder::onTransact(code, data, reply, flags);
}

}; // namespace android

