package fragments;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.FlashMode;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.csci3310.R;

import java.io.File;
import java.io.IOException;

public class PhotoTakingFragment extends Fragment {

    private int REQUEST_CODE_PERMISSIONS = 101;
    private String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA","android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"};
    CameraX.LensFacing lensFacing;



    TextureView textureView;
    Button flashLightButton, lensButton, button;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater
                .inflate(R.layout.layout_phototaking,container
                        ,false);


        textureView = (TextureView) rootView.findViewById(R.id.textureView);
        button = (Button) rootView.findViewById(R.id.imageButton);
        flashLightButton = (Button) rootView.findViewById(R.id.flashLightButton);
        lensButton = (Button) rootView.findViewById(R.id.swapCameraButton);



        if (cameraPermissionGranted()) {
            lensFacing = CameraX.LensFacing.BACK;
            startCamera();
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }



        return rootView;
    }

    private void startCamera() {
        CameraX.unbindAll();


        //TODO: Obtain the File Path name outside the fragment for further usage
        ContextWrapper contextWrapper= new ContextWrapper(getContext());
        String FILE_PATH_NAME = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DCIM).toString();

        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight());

        PreviewConfig pConfig = new PreviewConfig.Builder().setTargetAspectRatio(aspectRatio).setLensFacing(lensFacing).setTargetResolution(screen).build();
        Preview preview = new Preview(pConfig);

        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {

            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                ViewGroup parent = (ViewGroup) textureView.getParent();
                parent.removeView(textureView);
                parent.addView(textureView);

                textureView.setSurfaceTexture(output.getSurfaceTexture());
                updateTransform();
            }
        });


        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setLensFacing(lensFacing).setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY).setFlashMode(FlashMode.ON).build();
        ImageCapture imageCapture = new ImageCapture(imageCaptureConfig);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = new File(FILE_PATH_NAME + System.currentTimeMillis() + ".jpg");
                imageCapture.takePicture(file, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {

                        String msg = "Pic saved! At" + file.getAbsolutePath();
                        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {

                        String msg = "Failed to captured: " + message;
                        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();

                        if(cause != null){
                            cause.printStackTrace();
                        }

                    }
                });

            }
        });

        if (lensFacing==CameraX.LensFacing.FRONT){
            flashLightButton.setText("Front");
        }else {
            flashLightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String buttonText = (String) flashLightButton.getText();
                    ImageCaptureConfig imageCaptureConfig;

                    switch (buttonText) {//TODO: Change to image Button
                        case ("AUTO"):
                            imageCapture.setFlashMode(FlashMode.ON);
                            flashLightButton.setText("ON");
                            break;
                        case ("ON"):
                            imageCapture.setFlashMode(FlashMode.OFF);
                            flashLightButton.setText("OFF");
                            break;
                        case ("OFF"):
                            imageCapture.setFlashMode(FlashMode.AUTO);
                            flashLightButton.setText("AUTO");
                            break;
                        default://TODO: Change to Error message
                            //imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY).setFlashMode(FlashMode.ON).build();
                            flashLightButton.setText("ON");
                            break;
                    }
                }
            });
        }

        lensButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (lensFacing == CameraX.LensFacing.BACK){
                    lensFacing=CameraX.LensFacing.FRONT;
                    startCamera();
                }
                else {
                    lensFacing= CameraX.LensFacing.BACK;
                    startCamera();
                }
            }
        });

        CameraX.bindToLifecycle(this,preview,imageCapture);
    }

    private void updateTransform() {

        android.graphics.Matrix matrix = new android.graphics.Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        float cx = w/2f;
        float cy = h/2f;

        int rotationDgr;
        int rotation = (int) textureView.getRotation();

        switch (rotation){
            case Surface.ROTATION_0:
                rotationDgr=0;
                break;
            case Surface.ROTATION_90:
                rotationDgr=90;
                break;
            case Surface.ROTATION_180:
                rotationDgr=180;
                break;
            case Surface.ROTATION_270:
                rotationDgr=270;
                break;
            default:
                Log.d("Update Transform","Error on getting rotation");
                return;
        }

        matrix.postRotate((float)rotationDgr,cx,cy);
        textureView.setTransform(matrix);
    }

    private boolean cameraPermissionGranted() {

        for(String permission: REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(getContext(),permission)!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }



}
