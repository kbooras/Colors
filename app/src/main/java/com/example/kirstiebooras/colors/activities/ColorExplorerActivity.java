package com.example.kirstiebooras.colors.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kirstiebooras.colors.OnItemSelectedListener;
import com.example.kirstiebooras.colors.R;
import com.example.kirstiebooras.colors.fragments.HueFragment;
import com.example.kirstiebooras.colors.fragments.NamedColorFragment;
import com.example.kirstiebooras.colors.fragments.SaturationFragment;
import com.example.kirstiebooras.colors.fragments.ValueFragment;
import com.example.kirstiebooras.colors.Gradient;

/**
 * Activity for the Color Explorer, where users select a color range based on hue, saturation,
 * and value. They are then shown a list of named colors in the selected range.
 * Created by kirstiebooras on 4/15/15.
 */
public class ColorExplorerActivity extends FragmentActivity implements OnItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final String GRADIENT_SELECTED = "gradientSelected";
    private Gradient mGradientSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_explorer);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                Log.v(TAG, "savedInstanceState != null");
                mGradientSelected = savedInstanceState.getParcelable(GRADIENT_SELECTED);
                return;
            }
            HueFragment fragment = new HueFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
            mGradientSelected = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(GRADIENT_SELECTED, mGradientSelected);
    }

    @Override
    public void onHueSelected(Gradient selected) {
        Log.v(TAG, "onHueSelected");
        mGradientSelected = selected;

        // Display the SaturationFragment
        SaturationFragment fragment = new SaturationFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onSaturationSelected(Gradient selected) {
        Log.v(TAG, "onSaturationSelected");
        mGradientSelected = selected;

        // Display the ValueFragment
        ValueFragment fragment = new ValueFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onValueSelected(Gradient selected) {
        Log.v(TAG, "onValueSelected");
        mGradientSelected = selected;

        // Display the ResultFragment
        NamedColorFragment fragment = new NamedColorFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onStartAgain() {
        Log.v(TAG, "onStartAgain");
        mGradientSelected = null;

        // Display the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public Gradient getGradientSelected() {
        return mGradientSelected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color, menu);
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
}
