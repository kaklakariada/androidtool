package org.chris.android.tool.sensor;

import org.chris.android.tool.R;

import android.app.Activity;
import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SensorDetailActivity extends Activity {

    public static final String EXTRA_SENSOR_TYPE = "EXTRA_SENSOR_TYPE";
    private SensorService sensorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);
        // Show the Up button in the action bar.
        setupActionBar();

        int sensorType = getIntent().getIntExtra(EXTRA_SENSOR_TYPE, Integer.MIN_VALUE);
        if (sensorType == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Did not find extra '" + EXTRA_SENSOR_TYPE + "' in intent");
        }
        sensorService = new SensorService(getApplicationContext(), sensorType);
        updateSensorDetails();
    }

    private void updateSensorDetails() {
        Sensor sensor = sensorService.getSensor();
        fillTextView(R.id.sensor_name, R.string.sensor_name, sensor.getName());
        fillTextView(R.id.sensor_power, R.string.sensor_power, sensor.getPower());
        fillTextView(R.id.sensor_type, R.string.sensor_type, SensorType.forId(sensor.getType()).name(),
                sensor.getType());
        fillTextView(R.id.sensor_vendor, R.string.sensor_vendor, sensor.getVendor());
        fillTextView(R.id.sensor_version, R.string.sensor_version, sensor.getVersion());
        fillTextView(R.id.sensor_batch_info, R.string.sensor_batch_info, sensor.getFifoMaxEventCount(),
                sensor.getFifoReservedEventCount());
        fillTextView(R.id.sensor_delay, R.string.sensor_delay, sensor.getMinDelay());
        fillTextView(R.id.sensor_range, R.string.sensor_range, sensor.getResolution(), sensor.getMaximumRange());
    }

    private void fillTextView(int textViewId, int stringId, Object... formatArgs) {
        TextView text = (TextView) findViewById(textViewId);
        text.setText(getString(stringId, formatArgs));
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sensor_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorService.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorService.onResume();
    }
}
