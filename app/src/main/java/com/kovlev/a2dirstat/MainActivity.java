package com.kovlev.a2dirstat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.kovlev.a2dirstat.algo.Algorithm;
import com.kovlev.a2dirstat.algo.NaiveAlgorithm;
import com.kovlev.a2dirstat.algo.SortedAlgorithm;
import com.kovlev.a2dirstat.fragments.BoxesViewFragment;
import com.kovlev.a2dirstat.fragments.DetailsFragment;
import com.kovlev.a2dirstat.view.Box;
import com.kovlev.a2dirstat.view.BoxesView;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener, BoxesView.OnBoxSelectedListener {

    private Algorithm algorithm = new NaiveAlgorithm();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setAlgorithm(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    private void initFragments() {

        BoxesViewFragment firstFragment = new BoxesViewFragment();
        firstFragment.setAlgorithm(algorithm);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, firstFragment);
        ft.commitAllowingStateLoss();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            DetailsFragment secondFragment = new DetailsFragment();
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.flContainer2, secondFragment);
            ft2.commitAllowingStateLoss();
        }
    }

    @Override
    public void onBoxSelected(Box box) {
        DetailsFragment secondFragment = new DetailsFragment();

        Bundle args = new Bundle();
        args.putString("item", box.getFile().toString());
        secondFragment.setArguments(args);          // (1) Communicate with Fragment using Bundle


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer2, secondFragment) // replace flContainer
                    //.addToBackStack(null)
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, secondFragment) // replace flContainer
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_id:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_algo))) {
            setAlgorithm(sharedPreferences);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setAlgorithm(SharedPreferences sp) {
        String algoString = sp.getString(getString(R.string.key_algo), "Naive");
        String[] algos = getResources().getStringArray(R.array.algos);
        for (int i = 0; i < algos.length; i++) {
            if (algos[i].equals(algoString)) {
                switch (i) {
                    case 0: algorithm = new NaiveAlgorithm(); break;
                    case 1: algorithm = new SortedAlgorithm(); break;
                }
            }
        }
        initFragments();
    }
}
