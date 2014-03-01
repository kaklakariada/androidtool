package org.chris.android.tool;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TorchHelper {

    private static final Logger LOG = LoggerFactory.getLogger(TorchHelper.class);
    private Camera camera;
    private SurfaceHolder holder;
    private final Context context;

    private TorchHelper(Context context, Camera camera, final SurfaceHolder holder) {
        this.context = context;
        this.camera = camera;
        this.holder = holder;
        this.holder.addCallback(new PreviewCallback());
    }

    public static TorchHelper create(Context context, SurfaceView preview) {
        final Camera camera = Camera.open();
        final SurfaceHolder holder = preview.getHolder();
        return new TorchHelper(context, camera, holder);
    }

    public boolean isFlashAvailable() {
        return hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private boolean hasSystemFeature(String feature) {
        boolean hasSystemFeature = context.getPackageManager().hasSystemFeature(feature);
        LOG.debug("System feature {} available: {}", feature, hasSystemFeature);
        return hasSystemFeature;
    }

    public boolean isTorchActive() {
        if (cameraNotAvailable()) {
            LOG.warn("Camera not available");
            return false;
        }
        String flashMode = camera.getParameters().getFlashMode();
        boolean torchActive = Parameters.FLASH_MODE_TORCH.equals(flashMode);
        LOG.debug("Current flash mode: {}, torch active = {}", flashMode, torchActive);
        return torchActive;
    }

    public void switchTorchOn() {
        if (cameraNotAvailable()) {
            LOG.warn("Camera not available");
            return;
        }
        Parameters p = camera.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_TORCH);
        camera.setParameters(p);
        camera.startPreview();
    }

    public void switchTorchOff() {
        if (cameraNotAvailable()) {
            LOG.warn("Camera not available");
            return;
        }
        Parameters p = camera.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(p);
        camera.stopPreview();
    }

    private boolean cameraNotAvailable() {
        return camera == null;
    }

    public void destroy() {
        LOG.debug("Destroying torch helper: release camera");
        camera.release();
        camera = null;
    }

    private class PreviewCallback implements Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (camera == null) {
                return;
            }
            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                LOG.error("Error setting preview display", e);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera == null) {
                return;
            }
            camera.stopPreview();
        }
    }
}
