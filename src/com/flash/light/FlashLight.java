package com.flash.light;

import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.widget.Toast;
import android.widget.Button;
import android.graphics.Color;
import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.widget.ToggleButton;
import android.view.View.OnClickListener;
import android.content.pm.PackageManager;
import android.hardware.Camera.Parameters;

public class FlashLight extends Activity implements SurfaceHolder.Callback {

  private Camera mCam;
  private boolean isFlashOn;
  private Parameters params;
  private ToggleButton flashLight;
  private boolean hasCameraFlash;
  private SurfaceView surfaceView;
  private SurfaceHolder surfaceHolder;

  private static final String TAG = FlashLight.class.getSimpleName();

  @Override
  public void onResume() {
    super.onResume();
    getCamera();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mCam.stopPreview();
    mCam.release();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    surfaceView = (SurfaceView)findViewById(R.id.preview);
    surfaceHolder = surfaceView.getHolder();
    surfaceHolder.addCallback(FlashLight.this);
    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    hasCameraFlash = getApplicationContext().getPackageManager()
      .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

    if(!(hasCameraFlash)) {
      Toast.makeText(this, "Camera does not have flash feature.", Toast.LENGTH_LONG).show();
      return;
    }
    else {
      getCamera();
    }

    isFlashOn = false;
    flashLight = (ToggleButton)findViewById(R.id.flashLight);
    flashLight.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View view) {
        try {
          toggle();
          if(isFlashOn) {
            //flashLight.setBackgroundResource(R.drawable.flash_light_on);
          }
          else {
            //flashLight.setBackgroundResource(R.drawable.flash_light_off);
          }
        }
        catch(Exception e) {
          e.printStackTrace();
        }
      }

    });
  }

  public void toggle() {
    try {
      if(!(isFlashOn)) {
        flashLightOn();
      }
      else {
        flashLightOff();
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void getCamera() {
      try {
        mCam = Camera.open();
        params = mCam.getParameters();
      }
      catch(Exception e) {
        e.printStackTrace();
      }
  }

  public void flashLightOn() {
    if(mCam == null) {
      Toast.makeText(this, "Camera not found.", Toast.LENGTH_LONG).show();
      return;
    }
    try {
      if(!(isFlashOn)) {
        params = mCam.getParameters();
        params.setFlashMode(Parameters.FLASH_MODE_TORCH);
        mCam.setParameters(params);
        mCam.startPreview();
        isFlashOn = true;
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void flashLightOff() {
    try {
      if(isFlashOn) {
        params = mCam.getParameters();
        params.setFlashMode(Parameters.FLASH_MODE_OFF);
        mCam.setParameters(params);
        mCam.stopPreview();
        isFlashOn = false;
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { 
    // Empty method
  }

  public void surfaceCreated(SurfaceHolder holder) {
    try {
      mCam.setPreviewDisplay(holder); 
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void surfaceDestroyed(SurfaceHolder holder) {
    mCam.stopPreview();
    mCam.release();
  } 

}
