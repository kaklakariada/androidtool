package org.chris.android.tool.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class SensorService {

    private static final Logger LOG = LoggerFactory.getLogger(SensorService.class);
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
            LOG.debug("Accuracy changed for sensor {}: {}", sensor.getName(), accuracy);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            LOG.debug("{}: {}", event.sensor.getName(), Arrays.toString(event.values));
        }
    }

    public Sensor getSensor() {
        return sensor;
    }
}
