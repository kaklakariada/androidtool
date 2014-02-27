package org.chris.android.tool.sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chris.android.tool.R;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SensorListActivity extends ListActivity {

    private static final String TAG = "sensor";

    private static final String COLUMN_SENSOR_NAME = "sensorName";
    private static final String COLUMN_SENSOR_TYPE = "sensorType";
    private static final String COLUMN_SENSOR_VENDOR = "sensorVendor";
    private static final String COLUMN_SENSOR_VERSION = "sensorVersion";
    private static final String COLUMN_SENSOR_DESCRIPTION = "sensorDescription";

    private List<Sensor> sensorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        Log.d(TAG, "Found " + sensorList.size() + " sensors");
        setListAdapter(createListAdapter(sensorList));
    }

    private ListAdapter createListAdapter(List<Sensor> sensorList) {
        final List<? extends Map<String, ?>> data = createDataList(sensorList);
        return new SimpleAdapter(getApplicationContext(), data, android.R.layout.two_line_list_item, new String[] {
                COLUMN_SENSOR_NAME, COLUMN_SENSOR_DESCRIPTION }, new int[] { android.R.id.text1, android.R.id.text2 });
    }

    private List<? extends Map<String, ?>> createDataList(List<Sensor> sensorList) {
        List<Map<String, ?>> data = new ArrayList<Map<String, ?>>(sensorList.size());
        for (final Sensor sensor : sensorList) {
            data.add(createDataMap(sensor));
        }
        return data;
    }

    private Map<String, Object> createDataMap(Sensor sensor) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put(COLUMN_SENSOR_NAME, sensor.getName());
        map.put(COLUMN_SENSOR_VENDOR, sensor.getVendor());
        map.put(COLUMN_SENSOR_VERSION, sensor.getVersion());
        map.put(COLUMN_SENSOR_TYPE, SensorType.forId(sensor.getType()).name());
        map.put(COLUMN_SENSOR_DESCRIPTION, getDescription(sensor));
        return map;
    }

    private String getDescription(Sensor sensor) {
        return getString(R.string.sensor_description, SensorType.forId(sensor.getType()).name(), sensor.getVendor(),
                sensor.getPower());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Sensor sensor = sensorList.get(position);
        if (sensor == null) {
            Log.e(TAG, "Did not find selected sensor");
            return;
        }
        displaySensor(sensor);
    }

    private void displaySensor(Sensor sensor) {
        Intent intent = new Intent(this, SensorDetailActivity.class);
        intent.putExtra(SensorDetailActivity.EXTRA_SENSOR_TYPE, sensor.getType());
        startActivity(intent);
    }
}
