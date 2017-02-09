package sunxl8.android_lib.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class OrientationSensorUtils {
    private static final String TAG = "OrientationSensorUtils";
    private static List<WeakReference<OrientationInfoListener>> mOrientationInfoListener = new ArrayList();
    private static Sensor mSensor = null;
    private static SensorEventListener mSensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            OrientationSensorUtils.notifySensor(event);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private static SensorManager mSensorManager = null;

    public interface OrientationInfoListener {
        void onOrientationInfoChange(float f, float f2, float f3);
    }

    public static synchronized void requestSensor(Context context) {
        synchronized (OrientationSensorUtils.class) {
            Log.i(TAG, "requestSensor");
            if (context != null && mSensorManager == null) {
                mSensorManager = (SensorManager) context.getApplicationContext().getSystemService("sensor");
                mSensor = mSensorManager.getDefaultSensor(3);
                mSensorManager.registerListener(mSensorEventListener, mSensor, 1);
            }
        }
    }

    public static void addOrientationInfoListener(OrientationInfoListener l) {
        mOrientationInfoListener.add(new WeakReference(l));
    }

    public static void removeOrientationInfoListener(OrientationInfoListener l) {
        for (WeakReference<OrientationInfoListener> w : mOrientationInfoListener) {
            if (w.get() == l) {
                mOrientationInfoListener.remove(w);
                return;
            }
        }
    }

    public static synchronized void releaseSensor() {
        synchronized (OrientationSensorUtils.class) {
            Log.i(TAG, "releaseSensor");
            if (mSensorManager != null) {
                mSensorManager.unregisterListener(mSensorEventListener, mSensor);
            }
            mSensorManager = null;
            mSensor = null;
            mOrientationInfoListener.clear();
        }
    }

    private static void notifySensor(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        List<WeakReference<OrientationInfoListener>> list = new ArrayList();
        list.addAll(mOrientationInfoListener);
        for (WeakReference<OrientationInfoListener> w : list) {
            OrientationInfoListener l = (OrientationInfoListener) w.get();
            if (l == null) {
                mOrientationInfoListener.remove(w);
            } else {
                l.onOrientationInfoChange(x, y, z);
            }
        }
    }
}
