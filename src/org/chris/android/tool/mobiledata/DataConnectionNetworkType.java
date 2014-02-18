package org.chris.android.tool.mobiledata;

import android.telephony.TelephonyManager;
import android.util.SparseArray;

public enum DataConnectionNetworkType {

    _1xRTT(TelephonyManager.NETWORK_TYPE_1xRTT), CDMA(TelephonyManager.NETWORK_TYPE_CDMA), EDGE(
            TelephonyManager.NETWORK_TYPE_EDGE), EHRPD(TelephonyManager.NETWORK_TYPE_EHRPD), EVDO_0(
            TelephonyManager.NETWORK_TYPE_EVDO_0), EVDO_A(TelephonyManager.NETWORK_TYPE_EVDO_A), EVDO_B(
            TelephonyManager.NETWORK_TYPE_EVDO_B), GPRS(TelephonyManager.NETWORK_TYPE_GPRS), HSDPA(
            TelephonyManager.NETWORK_TYPE_HSDPA), HSPA(TelephonyManager.NETWORK_TYPE_HSPA), HSPAP(
            TelephonyManager.NETWORK_TYPE_HSPAP), HSUPA(TelephonyManager.NETWORK_TYPE_HSUPA), IDEN(
            TelephonyManager.NETWORK_TYPE_IDEN), LTE(TelephonyManager.NETWORK_TYPE_LTE), UMTS(
            TelephonyManager.NETWORK_TYPE_UMTS), UNKNOWN(TelephonyManager.NETWORK_TYPE_UNKNOWN);
    private final int state;
    private final static SparseArray<DataConnectionNetworkType> states = new SparseArray<DataConnectionNetworkType>();

    static {
        for (DataConnectionNetworkType val : values()) {
            states.put(val.state, val);
        }
    }

    private DataConnectionNetworkType(int state) {
        this.state = state;
    }

    public static DataConnectionNetworkType forId(int id) {
        return states.get(id);
    }

    public String getLabel() {
        return name();
    }
}
