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
    private final SensorListener sensorListener;
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

    public void listenForSensorUpdates(SensorServiceListener listener) {
        sensorListener.setDelegate(listener);
    }

    private class SensorListener implements SensorEventListener {
        private SensorServiceListener delegate;
        private int updateCount = 0;

        private void setDelegate(SensorServiceListener delegate) {
            this.delegate = delegate;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            LOG.debug("Accuracy changed for sensor {}: {}", sensor.getName(), accuracy);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            updateCount++;
            if(delegate != null) {
                delegate.sensorUpdated(updateCount, event.accuracy, event.timestamp, event.values);
            }
            LOG.debug("{}: {}", event.sensor.getName(), Arrays.toString(event.values));
        }
    }

    public Sensor getSensor() {
        return sensor;
    }

    public interface SensorServiceListener {
        void sensorUpdated(int updateCount, int accurracy, long timestamp,  float[] values);
    }
}
