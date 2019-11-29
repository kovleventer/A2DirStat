package com.kovlev.a2dirstat.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.kovlev.a2dirstat.R;
import com.kovlev.a2dirstat.algo.Algorithm;
import com.kovlev.a2dirstat.algo.NaiveAlgorithm;
import com.kovlev.a2dirstat.view.BoxesView;

/**
 * Fragment that holds a BoxesView
 */
public class BoxesViewFragment extends Fragment {

    private BoxesView.OnBoxSelectedListener listener;

    private BoxesView boxesView;

    private final int EXT_STOR_REQ_CODE = 123;

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    private Algorithm algorithm = new NaiveAlgorithm();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_boxes, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        boxesView = view.findViewById(R.id.boxesView);
        boxesView.setOnBoxSelectedListener(listener);

        // Requesting fs reading permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXT_STOR_REQ_CODE);
        } else {
            boxesView.initView(algorithm);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If permission is granted for reading the filesystem
        // Otherwise the app will not work (obviously)
        if (requestCode == EXT_STOR_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                boxesView.initView(algorithm);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof BoxesView.OnBoxSelectedListener){
            this.listener = (BoxesView.OnBoxSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement BoxesView.OnBoxSelectedListener");
        }
    }
}
