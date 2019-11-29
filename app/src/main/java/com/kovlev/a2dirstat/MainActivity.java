package com.kovlev.a2dirstat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

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

        // Hack to enable file openings with intents
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

    /**
     * Creates fragments as needed
     * One if the orientation is portrait
     * Two if landscape
     */
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

        // Communication using arguments
        Bundle args = new Bundle();
        args.putString("item", box.getFile().toString());
        secondFragment.setArguments(args);


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer2, secondFragment)
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, secondFragment)
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
                // Show settings
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

    /**
     * Updates the algorithm from sharedpreferences
     * @param sp The SharedPreferences to read from
     */
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
