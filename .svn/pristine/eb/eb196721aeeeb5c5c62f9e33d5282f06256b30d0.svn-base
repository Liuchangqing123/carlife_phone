// $Id: IInputManager.h 903 2018-03-26 06:46:45Z baizhijing $
#ifndef ANDROID_IINPUTMANAGER_H
#define ANDROID_IINPUTMANAGER_H

#include <binder/IInterface.h>
#include <sys/system_properties.h>

#if defined(IS_ANDROID_14) || defined(IS_ANDROID_15)
#include <ui/Input.h>
#elif defined(IS_ANDROID_16) || defined(IS_ANDROID_17) || defined(IS_ANDROID_18)
#include <androidfw/Input.h>
#else
#include <input/Input.h>
#endif

namespace android {

// ----------------------------------------------------------------------


class IInputManager : public IInterface
{
public:
    DECLARE_META_INTERFACE(InputManager);

	virtual bool injectInputEvent(InputEvent* ev, int mode) = 0;
    virtual bool injectKeyEvent(int keyCode, int action, int flags, int repeatCount) = 0;
    virtual bool injectMotionEvent(int action, const int ptCount, const PointerProperties* ptPro, const int sampleCount, const PointerCoords* ptCoords) = 0;

	// Funcs
	enum {
			GET_INPUT_DEVICE = IBinder::FIRST_CALL_TRANSACTION,
			GET_INPUT_DEVICE_IDS,
			HAS_KEYS,
			TRY_POINTER_SPEED,
			INJECT_INPUT_EVENT,
			GET_KEYBOARD_LAYOUTS,
			GET_KEYBOARD_LAYOUT,
			GET_CURRENT_KEYBOARD_LAYOUT_FOR_INPUT_DEVICE,
			SET_CURRENT_KEYBOARD_LAYOUT_FOR_INPUT_DEVICE,
			GET_KEYBOARD_LAYOUTS_FOR_INPUT_DEVICE,
			ADD_KEYBOARD_LAYOUT_FOR_INPUT_DEVICE,
			REMOVE_KEYBOARD_LAYOUT_FOR_INPUT_DEVICE,
			REGISTER_INPUT_DEVICES_CHANGED_LISTENER,
			VIBRATE,
			CANCEL_VIBRATE,
	};

	// Mode
    enum {
		    INJECT_INPUT_EVENT_MODE_ASYNC = 0,
		    INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT = 1,
		    INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH = 2,
	};
		
	// Token
	enum {
			PARCEL_TOKEN_MOTION_EVENT = 1,
			PARCEL_TOKEN_KEY_EVENT = 2,
	};
		
public:
	bool is_samsung_device;
	bool is_huawei_device;
	char version[PROP_VALUE_MAX];
	char manufacturer[PROP_VALUE_MAX];
	char model[PROP_VALUE_MAX];
};

// ----------------------------------------------------------------------

class BnInputManager : public BnInterface<IInputManager>
{
public:
    virtual status_t    onTransact( uint32_t code,
                                    const Parcel& data,
                                    Parcel* reply,
                                    uint32_t flags = 0);
};

// ----------------------------------------------------------------------

}; // namespace android

#endif // ANDROID_IINPUTMANAGER_H
