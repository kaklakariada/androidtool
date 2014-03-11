package org.chris.android.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import org.chris.android.tool.gps.GpsActivity;
import org.chris.android.tool.mobiledata.DataConnectionNetworkType;
import org.chris.android.tool.mobiledata.DataConnectionState;
import org.chris.android.tool.mobiledata.MobileDataHelper;
import org.chris.android.tool.mobiledata.MobileDataHelper.DataConnectionStateListener;
import org.chris.android.tool.sensor.SensorListActivity;
import org.chris.android.tool.service.WifiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends Activity {

    private static final Logger LOG = LoggerFactory.getLogger(MainActivity.class);
    private TorchHelper torchHelper;
    private MobileDataHelper mobileDataHelper;
    private WifiService wifiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context applicationContext = getApplicationContext();
        torchHelper = TorchHelper.create(applicationContext, (SurfaceView) findViewById(R.id.camera_preview));
        mobileDataHelper = new MobileDataHelper(applicationContext);
        wifiService = new WifiService(applicationContext);

        setupTorchSwitch();
        setupMobileDataSwitch();
        setupWifiSwitch();

        setupActivityButton(R.id.gps_button, GpsActivity.class);
        setupActivityButton(R.id.sensor_button, SensorListActivity.class);
    }

    private void setupActivityButton(int buttonId, final Class<? extends  Activity> activityClass) {
        Button sensorButton = (Button) findViewById(buttonId);
        sensorButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activityClass);
                startActivity(intent);
            }
        });
    }

    private void setupWifiSwitch() {
        Switch wifiSwitch = (Switch) findViewById(R.id.wifi_switch);
        wifiSwitch.setChecked(wifiService.isWifiEnabled());
        wifiSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wifiService.setWifiEnabled(isChecked);
            }
        });
    }

    private void setupMobileDataSwitch() {
        Switch mobileDataSwitch = (Switch) findViewById(R.id.mobile_data_switch);
        mobileDataSwitch.setChecked(mobileDataHelper.isMobileDataEnabled());
        mobileDataSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mobileDataHelper.setMobileDataEnabled(isChecked);
            }
        });
        mobileDataHelper.listenDataConnectionState(new DataConnectionStateListener() {
            @Override
            public void dataConnectionStateChanged(DataConnectionState state, DataConnectionNetworkType networkType) {
                String labelText = getLabelText(state, networkType);
                TextView dataStateLabel = (TextView) findViewById(R.id.mobile_data_state);
                dataStateLabel.setText(labelText);
            }
        });
    }

    private String getLabelText(DataConnectionState state, DataConnectionNetworkType networkType) {
        String labelText;
        if (state == null) {
            labelText = getResources().getString(R.string.mobile_data_state_unknown);
        } else if (state == DataConnectionState.CONNECTED) {
            labelText = networkType.getLabel();
        } else {
            labelText = getResources().getString(state.getLabelTextId());
        }
        return labelText;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        torchHelper.destroy();
    }

    private void setupTorchSwitch() {
        final Switch flashSwitch = (Switch) findViewById(R.id.torch_switch);
        flashSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleTorch(isChecked);
            }
        });
        flashSwitch.setEnabled(torchHelper.isFlashAvailable());
        flashSwitch.setChecked(torchHelper.isTorchActive());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void toggleTorch(boolean torchEnabled) {
        LOG.debug("Toggle torch, view: {}", torchEnabled);

        if (torchEnabled) {
            torchHelper.switchTorchOn();
        } else {
            torchHelper.switchTorchOff();
        }
    }
}
