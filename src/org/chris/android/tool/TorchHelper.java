package org.chris.android.tool;

import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class TorchHelper {

    private static final String TAG = "torch";
    private Camera camera;
    private final Context context;

    private TorchHelper(Context context, Camera camera) {
        this.context = context;
        this.camera = camera;
    }

    public static TorchHelper create(Context context, SurfaceView preview) {
        final Camera camera = Camera.open();
        final SurfaceHolder holder = preview.getHolder();
        holder.addCallback(new PreviewCallback(camera));
        return new TorchHelper(context, camera);
    }

    public boolean isFlashAvailable() {
        boolean flashAvailable = hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        return flashAvailable;
    }

    private boolean hasSystemFeature(String feature) {
        boolean hasSystemFeature = context.getPackageManager().hasSystemFeature(feature);
        Log.d(TAG, "System feature " + feature + " available: " + hasSystemFeature);
        return hasSystemFeature;
    }

    public boolean isTorchActive() {
        if (cameraNotAvailable()) {
            Log.w(TAG, "Camera not available");
            return false;
        }
        String flashMode = camera.getParameters().getFlashMode();
        boolean torchActive = Parameters.FLASH_MODE_TORCH.equals(flashMode);
        Log.d(TAG, "Current flash mode: " + flashMode + ", torch active = " + torchActive);
        return torchActive;
    }

    public void switchTorchOn() {
        if (cameraNotAvailable()) {
            Log.w(TAG, "Camera not available");
            return;
        }
        Parameters p = camera.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_TORCH);
        camera.setParameters(p);
        camera.startPreview();
    }

    public void switchTorchOff() {
        if (cameraNotAvailable()) {
            Log.w(TAG, "Camera not available");
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
        Log.d(TAG, "Destroying torch helper: release camera");
        camera.release();
        camera = null;
    }

    private static class PreviewCallback implements Callback {
        private final Camera camera;

        private PreviewCallback(Camera camera) {
            this.camera = camera;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                Log.e(TAG, "Error setting preview display", e);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.stopPreview();
        }
    }
}
