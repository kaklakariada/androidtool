package org.chris.android.tool.nfc;

import java.util.Arrays;

import org.chris.android.tool.R;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class NfcActivity extends Activity {

    private static final String TAG = "nfc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        // Show the Up button in the action bar.
        setupActionBar();
        Log.d(TAG, "Creating nfc activity");
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        String message = "Got intent " + getIntent() + " with action " + getIntent().getAction();
        displayMessage(message);
        Log.d(TAG, message);
        Bundle extras = getIntent().getExtras();
        for (String key : extras.keySet()) {
            Object value = extras.get(key);
            Log.d(TAG, "Extra " + key + " (" + value.getClass().getName() + ") : " + value);
        }
        byte[] id = extras.getByteArray(NfcAdapter.EXTRA_ID);
        Log.d(TAG, "Got id " + Arrays.toString(id));

        Tag tag = (Tag) extras.get(NfcAdapter.EXTRA_TAG);
        Log.d(TAG, "Tech list: " + Arrays.toString(tag.getTechList()));

        getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        // nfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    String msg = "Got nfc msg #" + i + ":" + msgs[i];
                    Log.d(TAG, msg);
                    displayMessage(msg);
                }
            }
        }
    }

    private void displayMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nfc, menu);
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
}
