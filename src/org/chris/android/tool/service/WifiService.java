package org.chris.android.tool.service;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WifiService {

    private final WifiManager wifiManager;

    public WifiService(Context context) {
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public boolean isWifiEnabled() {
        return wifiManager.isWifiEnabled();
    }

    public void setWifiEnabled(boolean isChecked) {
        wifiManager.setWifiEnabled(isChecked);
    }

    public WifiState getWifiState() {
        return WifiState.forId(wifiManager.getWifiState());
    }

    public String getSSID() {
        return wifiManager.getConnectionInfo().getSSID();
    }
}
