package org.nssae.demo;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmark;
import com.google.mediapipe.solutioncore.CameraInput;
import com.google.mediapipe.solutioncore.SolutionGlSurfaceView;
import com.google.mediapipe.solutioncore.VideoInput;
import com.google.mediapipe.solutions.hands.Hands;
import com.google.mediapipe.solutions.hands.HandsOptions;
import com.google.mediapipe.solutions.hands.HandsResult;

import org.vosk.demo.R;

import java.util.Arrays;
import java.util.List;


/** Main activity of MediaPipe Hands app. */
public class CameraActivity extends ComponentActivity {
    private static final String TAG = "MainActivity";
    public CommandRecognition cr;
    private Hands hands;
    // Run the pipeline and the model inference on GPU or CPU.
    private static final boolean RUN_ON_GPU = true;

    private enum InputSource {
        UNKNOWN,
        IMAGE,
        VIDEO,
        CAMERA,
    }
    private InputSource inputSource = InputSource.UNKNOWN;

    // Image demo UI and image loader components.
    private ActivityResultLauncher<Intent> imageGetter;
    private HandsResultImageView imageView;
    // Video demo UI and video loader components.
    private VideoInput videoInput;
    private ActivityResultLauncher<Intent> videoGetter;
    // Live camera demo UI and camera components.
    private CameraInput cameraInput;

    private SolutionGlSurfaceView<HandsResult> glSurfaceView;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cr = new CommandRecognition();
        cr.createCommandRecognition();
        cr.context = this.getBaseContext();

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
        }
        setupLiveDemoUiComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (inputSource == InputSource.CAMERA) {
            // Restarts the camera and the opengl surface rendering.
            cameraInput = new CameraInput(this);
            cameraInput.setNewFrameListener(textureFrame -> hands.send(textureFrame));
            glSurfaceView.post(this::startCamera);
            glSurfaceView.setVisibility(View.VISIBLE);
        } else if (inputSource == InputSource.VIDEO) {
            videoInput.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (inputSource == InputSource.CAMERA) {
            glSurfaceView.setVisibility(View.GONE);
            cameraInput.close();
        } else if (inputSource == InputSource.VIDEO) {
            videoInput.pause();
        }
    }


    /** Sets up the UI components for the live demo with camera input. */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setupLiveDemoUiComponents() {
        Button startCameraButton = findViewById(R.id.button_start_camera);
        startCameraButton.setOnClickListener(
                v -> {
                    if (inputSource == InputSource.CAMERA) {
                        return;
                    }
                    stopCurrentPipeline();
                    setupStreamingModePipeline();
                });
    }

    /** Sets up core workflow for streaming mode. */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setupStreamingModePipeline() {
        this.inputSource = InputSource.CAMERA;
        // Initializes a new MediaPipe Hands solution instance in the streaming mode.
        hands =
                new Hands(
                        this,
                        HandsOptions.builder()
                                .setStaticImageMode(false)
                                .setMaxNumHands(2)
                                .setRunOnGpu(RUN_ON_GPU)
                                .build());
//        hands.setErrorListener((message, e) -> Log.e(TAG, "MediaPipe Hands error:" + message));

        cameraInput = new CameraInput(this);
        cameraInput.setNewFrameListener(textureFrame -> hands.send(textureFrame));

        // Initializes a new Gl surface view with a user-defined HandsResultGlRenderer.
        glSurfaceView =
                new SolutionGlSurfaceView<>(this, hands.getGlContext(), hands.getGlMajorVersion());
        glSurfaceView.setSolutionResultRenderer(new HandsResultGlRenderer());
        glSurfaceView.setRenderInputImage(true);
        hands.setResultListener(
                handsResult -> {
//                    logWristLandmark(handsResult, /*showPixelValues=*/ false);
                    logGestures(handsResult);
                    glSurfaceView.setRenderData(handsResult);
                    glSurfaceView.requestRender();
                });

        // The runnable to start camera after the gl surface view is attached.
        // For video input source, videoInput.start() will be called when the video uri is available.
        glSurfaceView.post(this::startCamera);

        // Updates the preview layout.
        FrameLayout frameLayout = findViewById(R.id.preview_display_layout);
        //imageView.setVisibility(View.GONE);
        frameLayout.removeAllViewsInLayout();
        frameLayout.addView(glSurfaceView);
        glSurfaceView.setVisibility(View.VISIBLE);
        frameLayout.requestLayout();
    }

    private void startCamera() {
        cameraInput.start(
                this,
                hands.getGlContext(),
                CameraInput.CameraFacing.FRONT,
                glSurfaceView.getWidth(),
                glSurfaceView.getHeight());
    }

    private void stopCurrentPipeline() {
        if (cameraInput != null) {
            cameraInput.setNewFrameListener(null);
            cameraInput.close();
        }
        if (videoInput != null) {
            videoInput.setNewFrameListener(null);
            videoInput.close();
        }
        if (glSurfaceView != null) {
            glSurfaceView.setVisibility(View.GONE);
        }
        if (hands != null) {
            hands.close();
        }
    }

    List<Boolean> falseFive = Arrays.asList(false, false, false, false, false);

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void logGestures(HandsResult result) {
        if (result.multiHandLandmarks().isEmpty()) {
            cr.slipperyFingers = falseFive;
            return;
        }
        List<NormalizedLandmark> fLmarks = result.multiHandLandmarks().get(0).getLandmarkList();
        List<Boolean> fingers = falseFive;

//        double finger_x0 = fLmarks.get(0).getX();
//        double finger_y0 = fLmarks.get(0).getY();
//        double finger_x, finger_y, finger_xm, finger_ym;

        for (int i = 1; i <= 5; i++) {
//            finger_x = Math.pow(fLmarks.get(i*4).getX() - finger_x0, 2);
//            finger_y = Math.pow(fLmarks.get(i*4).getY() - finger_y0, 2);
//            finger_xm = Math.pow(fLmarks.get(i*4-2).getX() - finger_x0, 2);
//            finger_ym = Math.pow(fLmarks.get(i*4-2).getY() - finger_y0, 2);
//            fingers.set(i-1, Math.sqrt(finger_x + finger_y) < Math.sqrt(finger_xm + finger_ym));
            fingers.set(i-1, distance(fLmarks.get(i*4), fLmarks.get(0)) < distance(fLmarks.get(i*4-2), fLmarks.get(0)));
        }
        if (!fingers.get(0)) {
//            finger_x = Math.pow(fLmarks.get(4).getX() - fLmarks.get(17).getX(), 2);
//            finger_y = Math.pow(fLmarks.get(4).getY() - fLmarks.get(17).getY(), 2);
//            finger_xm = Math.pow(fLmarks.get(2).getX() - fLmarks.get(17).getX(), 2);
//            finger_ym = Math.pow(fLmarks.get(2).getY() - fLmarks.get(17).getY(), 2);
//            fingers.set(0, Math.sqrt(finger_x + finger_y) < Math.sqrt(finger_xm + finger_ym));
            fingers.set(0, distance(fLmarks.get(4), fLmarks.get(17)) < distance(fLmarks.get(2), fLmarks.get(17)));
        }

        cr.slipperyFingers = fingers;
        cr.recognizeCommand();
    }

    public double distance(NormalizedLandmark p1 , NormalizedLandmark p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }
}
