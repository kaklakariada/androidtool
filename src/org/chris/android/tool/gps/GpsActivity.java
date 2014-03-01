package org.chris.android.tool.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.chris.android.tool.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GpsActivity extends Activity {

    private static final Logger LOG = LoggerFactory.getLogger(GpsActivity.class);
    private LocationManager locationManager;
    private GpsStatus.Listener statusListener;
    private GpsStatus.NmeaListener nmeaListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        statusListener = new GpsStatusListener();
        nmeaListener = new NmeaListener();

        Button requestLocationButton = (Button) findViewById(R.id.request_location_button);
        requestLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                requestLocationUpdate();
            }
        });
    }

    private void requestLocationUpdate() {
        final Looper looper = Looper.getMainLooper();
        final Criteria criteria = new Criteria();
        //criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //criteria.setVerticalAccuracy(Criteria.ACCURACY_FINE);
        final LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                locationChanged(location);
            }

            @Override
            public void onStatusChanged(final String provider, final int status, final Bundle extras) {
                LOG.info("Provider {} changed to status {}, extras {}", provider, GpsStatusType.forId(status), extras);
            }

            @Override
            public void onProviderEnabled(final String provider) {
                LOG.info("Provider {} enabled");
            }

            @Override
            public void onProviderDisabled(final String provider) {
                LOG.info("Provider {} disabled");
            }
        };
        LOG.info("Requesting single location update with criteria {}", criteria);
        locationManager.requestSingleUpdate(criteria, listener, looper);
    }

    private void locationChanged(final Location location) {
        LOG.info("Got location update {}", location);
        int numberOfSatellites = location.getExtras() != null ? location.getExtras().getInt("satellites", 0) : 0;
        final float speed = (location.getSpeed() * 60 * 60) / 1000F;
        LOG.info("Satellites: {}, accuracy: {}m, altitude: {}m, bearing: {} degree, speed: {} km/h",
                numberOfSatellites,
                location.getAccuracy(), location.getAltitude(), location.getBearing(), speed);

        final String latitudeDegree = getLatitude(location, Location.FORMAT_DEGREES);
        final String longitudeDegree = getLongitude(location, Location.FORMAT_DEGREES);

        updateCoordinates(location, R.id.gps_location_degree, R.string.gps_location_degree, Location.FORMAT_DEGREES);
        updateCoordinates(location, R.id.gps_location_minutes, R.string.gps_location_minutes, Location.FORMAT_MINUTES);
        updateCoordinates(location, R.id.gps_location_seconds, R.string.gps_location_seconds, Location.FORMAT_SECONDS);
    }

    private void updateCoordinates(final Location location, int viewId, final int textId, final int format) {
        fillTextView(viewId, textId, getLongitude(location, format), getLatitude(location, format));
    }

    private String getLongitude(Location location, int format) {
        return Location.convert(location.getLongitude(), format);
    }

    private String getLatitude(Location location, int format) {
        return Location.convert(location.getLatitude(), format);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeGpsStatusListener(statusListener);
        locationManager.removeNmeaListener(nmeaListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        LOG.debug("Register gps status listeners");
        locationManager.addGpsStatusListener(statusListener);
        locationManager.addNmeaListener(nmeaListener);
        final Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        final String bestProvider = locationManager.getBestProvider(criteria, true);
        fillTextView(R.id.gps_providers, R.string.gps_providers, locationManager.getAllProviders().toString(),
                locationManager.getProviders(true), bestProvider);
        final LocationProvider provider = locationManager.getProvider(bestProvider);

    }

    private class GpsStatusListener implements GpsStatus.Listener {
        @Override
        public void onGpsStatusChanged(final int event) {
            gpsStatusChanged(GpsStatusType.forId(event));
        }
    }

    private void gpsStatusChanged(final GpsStatusType gpsStatus) {
        GpsStatus currentGpsStatus = locationManager.getGpsStatus(null);
        LOG.debug("Gps status changed: {}, details: {}", gpsStatus, currentGpsStatus);
        gpsStatusChanged(currentGpsStatus, gpsStatus);
    }

    private void gpsStatusChanged(final GpsStatus status, final GpsStatusType gpsStatusType) {
        int numberOfSatellites = 0;
        for (GpsSatellite satellite : status.getSatellites()) {
            numberOfSatellites++;
        }
        fillTextView(R.id.gps_status, R.string.gps_status, gpsStatusType, numberOfSatellites,
                status.getMaxSatellites(), status.getTimeToFirstFix());
    }

    private class NmeaListener implements GpsStatus.NmeaListener {
        @Override
        public void onNmeaReceived(final long timestamp, final String nmea) {
            fillTextView(R.id.gps_nmea, R.string.gps_nmea, nmea);
        }
    }

    private void fillTextView(int textViewId, int stringId, Object... formatArgs) {
        fillTextView(textViewId, getString(stringId, formatArgs));
    }

    private void fillTextView(int textViewId, String value) {
        TextView text = (TextView) findViewById(textViewId);
        text.setText(value);
    }
}
