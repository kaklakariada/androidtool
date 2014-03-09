package org.chris.android.tool.gps;

import android.location.GpsStatus;
import android.location.LocationManager;

import net.sf.marineapi.nmea.event.SentenceEvent;
import net.sf.marineapi.nmea.event.SentenceListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Created by chris on 09.03.14.
 */
public class NmeaReader {

    private static final Logger LOG = LoggerFactory.getLogger(NmeaReader.class);

    private final LocationManager locationManager;
    // private final NmeaListener nmeaListener;

    public NmeaReader(LocationManager locationManager) {
        this.locationManager = locationManager;
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        try {
            pipedInputStream.connect(pipedOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOG.info("Creating nmea listener and sentence reader...");
        // nmeaListener = new NmeaListener(pipedOutputStream);
        LOG.info("Creating sentence reader");
        //SentenceReader sentenceReader = new SentenceReader(pipedInputStream);
        LOG.info("Add listener to sentence reader");
        //sentenceReader.addSentenceListener(new NmeaSentenceListener());
        resume();
    }

    public void resume() {
        LOG.debug("Add nmea listener");
        //    locationManager.addNmeaListener(nmeaListener);
    }

    public void pause() {
        LOG.debug("Remove nmea listener");
        //  locationManager.removeNmeaListener(nmeaListener);
    }

    private static class NmeaSentenceListener implements SentenceListener {

        @Override
        public void readingPaused() {
            LOG.debug("Reading paused");
        }

        @Override
        public void readingStarted() {
            LOG.debug("Reading started");
        }

        @Override
        public void readingStopped() {
            LOG.debug("Reading stopped");
        }

        @Override
        public void sentenceRead(SentenceEvent event) {
            LOG.debug("Sentence read: {}", event.getSentence());
        }
    }


    private static class NmeaListener implements GpsStatus.NmeaListener {
        private OutputStream outputStream;

        public NmeaListener(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void onNmeaReceived(final long timestamp, final String nmea) {
            try {
                outputStream.write(nmea.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error writing to stream", e);
            }
        }
    }
}
