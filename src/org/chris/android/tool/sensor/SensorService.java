package org.chris.android.tool.sensor;

import java.util.Arrays;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorService {

    private static final String TAG = "sensorservice";
    private final SensorManager sensorManager;
    private final SensorEventListener sensorListener;
    private final Sensor sensor;

    public SensorService(Context applicationContext, int sensorType) {
        sensorManager = (SensorManager) applicationContext.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);
        sensorListener = new SensorListener();
    }

    public void onPause() {
        sensorManager.unregisterListener(sensorListener);
    }

    public void onResume() {
        sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    private class SensorListener implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d(TAG, "Accuracy changed for sensor " + sensor.getName() + ": " + accuracy);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d(TAG, event.sensor.getName() + ": " + Arrays.toString(event.values));
        }
    }

    public Sensor getSensor() {
        return sensor;
    }
}
