package org.chris.android.tool.mobiledata;

import org.chris.android.tool.R;

import android.telephony.TelephonyManager;
import android.util.SparseArray;

public enum DataConnectionState {

    DISCONNECTED(TelephonyManager.DATA_DISCONNECTED, R.string.mobile_data_state_disconnected, false), CONNECTED(
            TelephonyManager.DATA_CONNECTED, R.string.mobile_data_state_connected, true), CONNECTING(
            TelephonyManager.DATA_CONNECTING, R.string.mobile_data_state_connecting, true), SUSPENDED(
            TelephonyManager.DATA_SUSPENDED, R.string.mobile_data_state_suspended, true);
    private final int state;
    private final static SparseArray<DataConnectionState> states = new SparseArray<DataConnectionState>();
    private final boolean connected;
    private final int labelTextId;

    static {
        for (DataConnectionState val : values()) {
            states.put(val.state, val);
        }
    }

    private DataConnectionState(int state, int labelTextId, boolean connected) {
        this.state = state;
        this.labelTextId = labelTextId;
        this.connected = connected;
    }

    public static DataConnectionState forId(int id) {
        return states.get(id);
    }

    public boolean isConnected() {
        return connected;
    }

    public int getLabelTextId() {
        return labelTextId;
    }
}
