package com.example.lihui.flashcontrol;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.hardware.Camera;


import android.util.Log;
import android.widget.ToggleButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Camera mCamera;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            mCamera = Camera.open();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setChecked(isCameraLightOn(mCamera));
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleButton button = (ToggleButton)view;
                Log.d("button","ToggleButton clicked");

                turnLightStatus(mCamera,button.isChecked());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean isCameraLightOn(Camera camera) {
        if (camera == null) {
            return false;
        }
        return camera.getParameters().getFlashMode().equals(camera.getParameters().FLASH_MODE_TORCH);
    }

    public static void turnLightStatus(Camera camera, boolean turnOn) {
        if (camera == null) {
            return;
        }

        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            return;
        }

        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes == null) {
            return;
        }

        String lightFlashMode = turnOn ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF;

        String flashMode = parameters.getFlashMode();
        if (!lightFlashMode.equals(flashMode)) {
            if (flashModes.contains(lightFlashMode)) {
                if (lightFlashMode == Camera.Parameters.FLASH_MODE_TORCH) {
                    camera.startPreview();
                }
                parameters.setFlashMode(lightFlashMode);
                camera.setParameters(parameters);
            }
        }
    }
}
