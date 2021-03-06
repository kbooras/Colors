package com.example.kirstiebooras.colors.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.kirstiebooras.colors.R;

/**
 * Activity when app is first launched. Users can select to use the Color Explorer or
 * can identify a color.
 * Created by kirstiebooras on 4/20/15.
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button colorExplorer = (Button) findViewById(R.id.colorExplorerButton);
        colorExplorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startColorExplorer();
            }
        });

        Button identifyColor = (Button) findViewById(R.id.identifyColorButton);
        identifyColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIdentifyColor();
            }
        });
    }

    private void startColorExplorer() {
        Log.d(TAG, "startColorExplorer");
        Intent intent = new Intent(this, ColorExplorerActivity.class);
        startActivity(intent);
    }

    private void startIdentifyColor() {
        Log.d(TAG, "startIdentifyColor");
        Intent intent = new Intent(this, IdentifyColorActivity.class);
        startActivity(intent);
    }
}
