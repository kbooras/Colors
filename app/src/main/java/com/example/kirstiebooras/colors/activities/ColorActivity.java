package com.example.kirstiebooras.colors.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kirstiebooras.colors.R;
import com.example.kirstiebooras.colors.database.ColorDatabaseContract;
import com.example.kirstiebooras.colors.database.QueryFactory;

public class ColorActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ColorActivity";

    private static final int LOADER_ID_GET_FROM_HSV = 1;
    private static final int LOADER_ID_GET_FROM_ID = 2;
    private static final int LOADER_ID_GET_FROM_SUBSTRING = 3;
    private static final int LOADER_ID_GET_FROM_HUE = 4;
    private static final int LOADER_ID_GET_FROM_HUE_RANGE = 5;
    private static final int LOADER_ID_GET_FROM_SATURATION = 6;
    private static final int LOADER_ID_GET_FROM_SATURATION_RANGE = 7;
    private static final int LOADER_ID_GET_FROM_VALUE = 8;
    private static final int LOADER_ID_GET_FROM_VALUE_RANGE = 9;

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        String[] dataColumns = { ColorDatabaseContract.FeedEntry.COLUMN_COLOR_NAME };
        int[] viewIds = { android.R.id.text1 };

        // Todo custom adapter so these have their color set accordingly

        mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null,
                dataColumns, viewIds, 0);
        ListView listView = (ListView) findViewById(R.id.colorList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Display the info about the color
                displayColorInfo(position);
            }
        });

        setTextView(getIntent().getExtras());

        mCallbacks = this;

        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID_GET_FROM_HSV, getIntent().getExtras(), mCallbacks);

    }

    private void displayColorInfo(int position) {
        Cursor c = mAdapter.getCursor();
        c.moveToPosition(position);
        String name = c.getString(c.getColumnIndex(ColorDatabaseContract.FeedEntry.COLUMN_COLOR_NAME));
        int hue = c.getInt(c.getColumnIndex(ColorDatabaseContract.FeedEntry.COLUMN_HUE));
        int saturation = c.getInt(c.getColumnIndex(ColorDatabaseContract.FeedEntry.COLUMN_SATURATION));
        int value = c.getInt(c.getColumnIndex(ColorDatabaseContract.FeedEntry.COLUMN_VALUE));
        Toast.makeText(this,
                String.format(getString(R.string.color_info_toast), name, hue, saturation, value),
                Toast.LENGTH_LONG).show();
    }

    private void setTextView(Bundle args) {
        int hueL = (int) args.getFloat(QueryFactory.ARG_HUE_LOWER);
        int hueU = (int) args.getFloat(QueryFactory.ARG_HUE_UPPER);
        int saturation = (int) (args.getFloat(QueryFactory.ARG_SATURATION_LOWER)*100);
        int value = (int) (args.getFloat(QueryFactory.ARG_VALUE_LOWER)*100);
        TextView textView = (TextView) findViewById(R.id.gradientDetails);
        textView.setText(String.format(getString(R.string.gradient_details), hueL, hueU, saturation, value));
    }

    public void onStartAgain(View view) {
        Log.v(TAG, "onStartAgain");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * For testing purposes.
     */
    private static void printEverythingFromCursor(Cursor cur) {
        if (cur.getCount() != 0) {
            cur.moveToFirst();
            do {
                String row_values = "";
                for (int i = 0; i < cur.getColumnCount(); i++) {
                    row_values = row_values + " || " + cur.getString(i);
                }
                Log.d(TAG, row_values);

            } while (cur.moveToNext());
        }
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader;
        switch (id) {
            case LOADER_ID_GET_FROM_HSV:
                loader = QueryFactory.getColorsFromHueSaturationValue(getBaseContext(), args);
                break;
            case LOADER_ID_GET_FROM_ID:
                loader = QueryFactory.getColorFromId(getBaseContext(), args);
                break;
            case LOADER_ID_GET_FROM_SUBSTRING:
                loader = QueryFactory.getColorsWithSubstring(getBaseContext(), args);
                break;
            case LOADER_ID_GET_FROM_HUE:
                loader = QueryFactory.getColorsWithHue(getBaseContext(), args);
                break;
            case LOADER_ID_GET_FROM_HUE_RANGE:
                loader = QueryFactory.getColorsInHueRange(getBaseContext(), args);
                break;
            case LOADER_ID_GET_FROM_SATURATION:
                loader = QueryFactory.getColorsWithSaturation(getBaseContext(), args);
                break;
            case LOADER_ID_GET_FROM_SATURATION_RANGE:
                loader = QueryFactory.getColorsInSaturationRange(getBaseContext(), args);
                break;
            case LOADER_ID_GET_FROM_VALUE:
                loader = QueryFactory.getColorsWithValue(getBaseContext(), args);
                break;
            case LOADER_ID_GET_FROM_VALUE_RANGE:
                loader = QueryFactory.getColorsInValueRange(getBaseContext(), args);
                break;
            default:
                throw new IllegalArgumentException("Invalid CursorLoader id");
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID_GET_FROM_HSV:
                mAdapter.swapCursor(data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
