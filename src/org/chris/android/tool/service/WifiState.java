package org.chris.android.tool.service;

import android.net.wifi.WifiManager;
import android.util.SparseArray;

import org.chris.android.tool.R;

public enum WifiState {

    DISABLED(WifiManager.WIFI_STATE_DISABLED, R.string.wifi_state_disabled), DISABLING(
            WifiManager.WIFI_STATE_DISABLING, R.string.wifi_state_disabling), ENABLED(WifiManager.WIFI_STATE_ENABLED,
            R.string.wifi_state_enabled), ENABLING(WifiManager.WIFI_STATE_ENABLING, R.string.wifi_state_enabling),
    UNKNOWN(
            WifiManager.WIFI_STATE_UNKNOWN, R.string.wifi_state_unknown);
    private int state;
    private final static SparseArray<WifiState> states = new SparseArray<>();
    private final int labelTextId;

    static {
        for (WifiState val : values()) {
            states.put(val.state, val);
        }
    }

    private WifiState(int state, int labelTextId) {
        this.state = state;
        this.labelTextId = labelTextId;
    }

    public static WifiState forId(int id) {
        return states.get(id);
    }

    public int getLabelTextId() {
        return labelTextId;
    }
}
