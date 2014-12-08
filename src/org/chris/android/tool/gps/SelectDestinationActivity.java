package org.chris.android.tool.gps;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;

import org.chris.android.tool.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SelectDestinationActivity extends Activity {

    private static final Logger LOG = LoggerFactory.getLogger(SelectDestinationActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_destination);

        EditText searchText = (EditText) findViewById(R.id.destination_search_text);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
                // ignore
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                new SearchLocationTask().execute(s.toString());
            }

            @Override
            public void afterTextChanged(final Editable s) {
                // ignore
            }
        });
    }

    private class SearchLocationTask extends AsyncTask<String, Void, List<Address>> {
        @Override
        protected List<Address> doInBackground(final String... params) {
            return searchLocations(params[0]);
        }

        protected void onPostExecute(List<Address> result) {
            showAddressListPopup(result);
        }
    }

    private List<Address> searchLocations(final String locationName) {
        final Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            final List<Address> addresses = geocoder.getFromLocationName(locationName, 5);
            LOG.info("Found {} addresses for location '{}'", addresses.size(), locationName);
            return addresses;
        } catch (IOException e) {
            LOG.error("Error getting location");
            return Collections.emptyList();
        }
    }

    public void showAddressListPopup(final List<Address> addresses) {
        ListPopupWindow popup = new ListPopupWindow(this);
        popup.setAnchorView(findViewById(R.id.destination_search_text));

        ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, addresses);
        popup.setAdapter(adapter);

        popup.show();
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

}
