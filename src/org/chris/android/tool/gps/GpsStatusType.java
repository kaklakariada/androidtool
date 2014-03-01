package org.chris.android.tool.gps;

import android.location.GpsStatus;
import android.util.SparseArray;

public enum GpsStatusType {

    GPS_EVENT_STARTED(GpsStatus.GPS_EVENT_STARTED),
    GPS_EVENT_STOPPED(GpsStatus.GPS_EVENT_STOPPED),
    GPS_EVENT_FIRST_FIX(GpsStatus.GPS_EVENT_FIRST_FIX),
    GPS_EVENT_SATELLITE_STATUS(GpsStatus.GPS_EVENT_SATELLITE_STATUS);

    private final int state;
    private final static SparseArray<GpsStatusType> states = new SparseArray<GpsStatusType>();

    static {
        for (GpsStatusType val : values()) {
            states.put(val.state, val);
        }
    }

    private GpsStatusType(int state) {
        this.state = state;
    }

    public static GpsStatusType forId(int id) {
        return states.get(id);
    }
}
