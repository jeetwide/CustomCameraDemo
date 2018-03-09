package com.example.jeetendraachtani.customcamerademo;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    Camera camera;
    ShowCamera showCamera;


    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        frameLayout=(FrameLayout)findViewById(R.id.frameLayout);

        //open Camera

        camera=Camera.open();

        showCamera= new ShowCamera(this,camera);
        frameLayout.addView(showCamera);


    }



}
