package org.chris.android.tool.mobiledata;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MobileDataHelper {

    private static final String TAG = "main";
    private final Context context;
    private final TelephonyManager telephonyService;

    public MobileDataHelper(Context context) {
        this.context = context;
        this.telephonyService = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public void listenDataConnectionState(final DataConnectionStateListener listener) {
        telephonyService.listen(new PhoneStateListener() {
            @Override
            public void onDataConnectionStateChanged(int state, int networkType) {
                listener.dataConnectionStateChanged(DataConnectionState.forId(state),
                        DataConnectionNetworkType.forId(networkType));
            }
        }, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
    }

    public boolean isMobileDataEnabled() {
        final Object iConnectivityManager = getConnectivityManager();
        boolean mobileDataEnabled = (Boolean) invokePrivateMethod(iConnectivityManager, "getMobileDataEnabled");
        Log.d(TAG, "Mobile data enabled: " + mobileDataEnabled);
        return mobileDataEnabled;
    }

    public void setMobileDataEnabled(boolean enabled) {
        Log.i(TAG, "Set mobile data enabled to " + enabled);
        final Object iConnectivityManager = getConnectivityManager();
        invokePrivateMethod(iConnectivityManager, "setMobileDataEnabled", new Class<?>[] { Boolean.TYPE }, enabled);
        Log.d(TAG, "success");
    }

    private Object getConnectivityManager() {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return getPrivateFieldValue(conman, "mService");
    }

    private Object getPrivateFieldValue(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Error getting field " + fieldName + " of target " + target + " of type "
                    + target.getClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error getting field " + fieldName + " of target " + target + " of type "
                    + target.getClass().getName(), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error getting field " + fieldName + " of target " + target + " of type "
                    + target.getClass().getName(), e);
        }
    }

    private Object invokePrivateMethod(Object target, String methodName, Object... args) {
        final Class<?>[] argTypes = getArgumentTypes(args);
        return invokePrivateMethod(target, methodName, argTypes, args);
    }

    private Object invokePrivateMethod(Object target, String methodName, Class<?>[] argTypes, Object... args) {
        try {
            final Method method = target.getClass().getDeclaredMethod(methodName, argTypes);
            method.setAccessible(true);
            Object returnValue = method.invoke(target, args);
            Log.d(TAG, "Got return value " + returnValue + " from method call " + methodName);
            return returnValue;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Error invoking method " + methodName + " with arguments "
                    + Arrays.toString(argTypes) + " on object " + target + " of type " + target.getClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error invoking method " + methodName + " with arguments "
                    + Arrays.toString(argTypes) + " on object " + target + " of type " + target.getClass().getName(), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error invoking method " + methodName + " with arguments "
                    + Arrays.toString(argTypes) + " on object " + target + " of type " + target.getClass().getName(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Error invoking method " + methodName + " with arguments "
                    + Arrays.toString(argTypes) + " on object " + target + " of type " + target.getClass().getName(), e);
        }
    }

    private Class<?>[] getArgumentTypes(Object... args) {
        final Class<?>[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Arguments " + Arrays.toString(args) + " have type " + Arrays.toString(argTypes));
        }
        return argTypes;
    }

    public interface DataConnectionStateListener {
        void dataConnectionStateChanged(DataConnectionState state, DataConnectionNetworkType networkType);
    }
}
