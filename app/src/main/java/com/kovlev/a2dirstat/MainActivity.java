package com.kovlev.a2dirstat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.kovlev.a2dirstat.view.BoxesView;

public class MainActivity extends AppCompatActivity {

    private final int EXT_STOR_REQ_CODE = 123;

    private BoxesView boxesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boxesView = findViewById(R.id.boxesView);

        // Requesting fs reading permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXT_STOR_REQ_CODE);
        } else {
            boxesView.initView();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EXT_STOR_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                boxesView.initView();
            }
        }
    }
}
