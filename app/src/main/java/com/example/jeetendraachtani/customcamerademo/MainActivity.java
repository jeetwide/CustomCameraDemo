package com.example.jeetendraachtani.customcamerademo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    Camera camera;
    ShowCamera showCamera;

    Button btn_capture;

    FrameLayout frameLayout;

    final int Req_CAMERA = 1;
    final int Req_WRITE_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    Req_CAMERA);
            return;
        }

        initialize();



    }

    Camera.PictureCallback mPictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File picture_file = getOutputMediaFile();

            if (picture_file == null) {
                return;
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(picture_file);
                    fos.write(data);
                    fos.close();

                    camera.startPreview();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    };

    private File getOutputMediaFile() {

        checkWritePermission();
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        } else {

            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");

            if (!folder_gui.exists()) {
                folder_gui.mkdirs();
            }
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            File outputFile = new File(folder_gui, ts+".jpg");
            return outputFile;
        }

    }

    private void captureImage(View v) {    //captureImage(View v)

        if (camera != null) {
            camera.takePicture(null, null, mPictureCallBack);
        }
    }

    private void initialize() {
        try {
            frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

            btn_capture = (Button) findViewById(R.id.btn_capture);
            btn_capture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkWritePermission();

                    captureImage(null);
                }
            });

            //open Camera

            camera = Camera.open();

            showCamera = new ShowCamera(this, camera);
            frameLayout.addView(showCamera);
            checkWritePermission();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkWritePermission(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Req_WRITE_EXTERNAL_STORAGE);
            return;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Req_CAMERA: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    initialize();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Camera Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;

            }
            case Req_WRITE_EXTERNAL_STORAGE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");

                    if (!folder_gui.exists()) {
                        folder_gui.mkdirs();
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Storage Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;

            }
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }
}



