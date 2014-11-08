package org.chris.android.tool.sensor;

import android.hardware.Sensor;
import android.util.SparseArray;

public enum SensorType {

    ORIENTATION(Sensor.TYPE_ORIENTATION, false),

    ACCELEROMETER(Sensor.TYPE_ACCELEROMETER, false),

    AMBIENT_TEMPERATURE(Sensor.TYPE_AMBIENT_TEMPERATURE, false),

    GAME_ROTATION_VECTOR(Sensor.TYPE_GAME_ROTATION_VECTOR, false),

    GEOMAGNETIC_ROTATION_VECTOR(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, false),

    GRAVITY(Sensor.TYPE_GRAVITY, false),

    GYROSCOPE(Sensor.TYPE_GYROSCOPE, false),

    GYROSCOPE_UNCALIBRATED(Sensor.TYPE_GYROSCOPE_UNCALIBRATED, false),

    LIGHT(Sensor.TYPE_LIGHT, false),

    LINEAR_ACCELERATION(Sensor.TYPE_LINEAR_ACCELERATION, false),

    MAGNETIC_FIELD(Sensor.TYPE_MAGNETIC_FIELD, false),

    MAGNETIC_FIELD_UNCALIBRATED(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED, false),

    PRESSURE(Sensor.TYPE_PRESSURE, false),

    PROXIMITY(Sensor.TYPE_PROXIMITY, false),

    RELATIVE_HUMIDITY(Sensor.TYPE_RELATIVE_HUMIDITY, false),

    ROTATION_VECTOR(Sensor.TYPE_ROTATION_VECTOR, false),

    SIGNIFICANT_MOTION(Sensor.TYPE_SIGNIFICANT_MOTION, true),

    STEP_COUNTER(Sensor.TYPE_STEP_COUNTER, false),

    STEP_DETECTOR(Sensor.TYPE_STEP_DETECTOR, true);

    private final int state;
    private final static SparseArray<SensorType> states = new SparseArray<SensorType>();

    private final boolean trigger;

    static {
        for (SensorType val : values()) {
            states.put(val.state, val);
        }
    }

    private SensorType(int state, boolean trigger) {
        this.state = state;
        this.trigger = trigger;
    }

    public static SensorType forId(int id) {
        return states.get(id);
    }

    // public int getLabelTextId() {
    // return labelTextId;
    // }
}
