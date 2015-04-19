package com.example.kirstiebooras.colors.fragments;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kirstiebooras.colors.Gradient;
import com.example.kirstiebooras.colors.OnItemSelectedListener;
import com.example.kirstiebooras.colors.R;
import com.example.kirstiebooras.colors.activities.MainActivity;
import com.example.kirstiebooras.colors.database.ColorDatabaseContract;
import com.example.kirstiebooras.colors.database.QueryFactory;

public class NamedColorFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "NamedColorFragment";

    private static final int LOADER_ID_GET_FROM_HSV = 1;
    private static final int LOADER_ID_GET_FROM_ID = 2;
    private static final int LOADER_ID_GET_FROM_SUBSTRING = 3;
    private static final int LOADER_ID_GET_FROM_HUE = 4;
    private static final int LOADER_ID_GET_FROM_HUE_RANGE = 5;
    private static final int LOADER_ID_GET_FROM_SATURATION = 6;
    private static final int LOADER_ID_GET_FROM_SATURATION_RANGE = 7;
    private static final int LOADER_ID_GET_FROM_VALUE = 8;
    private static final int LOADER_ID_GET_FROM_VALUE_RANGE = 9;

    private SimpleCursorAdapter mAdapter;
    private Context mContext;

    private OnItemSelectedListener mListener;

    @Override
    public void onAttach(Activity activity) {
        Log.v(TAG, "onAttach");
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            mListener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_named_color, container, false);

        Gradient gradient =((MainActivity) getActivity()).getGradientSelected();
        String leftHue = String.valueOf(gradient.getLeftHue());
        String rightHue = String.valueOf(gradient.getRightHue());
        String saturation = String.valueOf(gradient.getSaturation());
        String value = String.valueOf(gradient.getValue());

        TextView textView = (TextView) view.findViewById(R.id.gradientDetails);
        textView.setText(String.format(getString(R.string.gradient_details), leftHue, rightHue,
                saturation, value));

        Button button = (Button) view.findViewById(R.id.startAgainButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStartAgain();
            }
        });

        String[] dataColumns = { ColorDatabaseContract.FeedEntry.COLUMN_COLOR_NAME };
        int[] viewIds = { android.R.id.text1 };

        mContext = getActivity().getBaseContext();
        mAdapter = new SimpleCursorAdapter(mContext,
                android.R.layout.simple_list_item_1, null,
                dataColumns, viewIds, 0);

        setListAdapter(mAdapter);

        Bundle args = createBundle();
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID_GET_FROM_HSV, args, this);

        return view;
    }

    // Create the bundle to be used by the loader
    private Bundle createBundle() {
        Bundle args = new Bundle();
        Gradient selected = ((MainActivity)getActivity()).getGradientSelected();
        args.putFloat(QueryFactory.ARG_HUE_LOWER, selected.getLeftHue());
        args.putFloat(QueryFactory.ARG_HUE_UPPER, selected.getRightHue());
        args.putFloat(QueryFactory.ARG_SATURATION_LOWER, selected.getSaturation());
        args.putFloat(QueryFactory.ARG_SATURATION_UPPER, selected.getSaturation());
        args.putFloat(QueryFactory.ARG_VALUE_LOWER, selected.getValue());
        args.putFloat(QueryFactory.ARG_VALUE_UPPER, selected.getValue());
        return args;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor c = mAdapter.getCursor();
        c.moveToPosition(position);

        String name = c.getString(c.getColumnIndex(ColorDatabaseContract.FeedEntry.COLUMN_COLOR_NAME));
        int hue = c.getInt(c.getColumnIndex(ColorDatabaseContract.FeedEntry.COLUMN_HUE));
        int saturation = c.getInt(c.getColumnIndex(ColorDatabaseContract.FeedEntry.COLUMN_SATURATION));
        int value = c.getInt(c.getColumnIndex(ColorDatabaseContract.FeedEntry.COLUMN_VALUE));

        Toast.makeText(mContext, String.format(getString(R.string.color_info_toast), name, hue,
                saturation, value), Toast.LENGTH_LONG).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader;
        switch (id) {
            case LOADER_ID_GET_FROM_HSV:
                loader = QueryFactory.getColorsFromHueSaturationValue(mContext, args);
                break;
            case LOADER_ID_GET_FROM_ID:
                loader = QueryFactory.getColorFromId(mContext, args);
                break;
            case LOADER_ID_GET_FROM_SUBSTRING:
                loader = QueryFactory.getColorsWithSubstring(mContext, args);
                break;
            case LOADER_ID_GET_FROM_HUE:
                loader = QueryFactory.getColorsWithHue(mContext, args);
                break;
            case LOADER_ID_GET_FROM_HUE_RANGE:
                loader = QueryFactory.getColorsInHueRange(mContext, args);
                break;
            case LOADER_ID_GET_FROM_SATURATION:
                loader = QueryFactory.getColorsWithSaturation(mContext, args);
                break;
            case LOADER_ID_GET_FROM_SATURATION_RANGE:
                loader = QueryFactory.getColorsInSaturationRange(mContext, args);
                break;
            case LOADER_ID_GET_FROM_VALUE:
                loader = QueryFactory.getColorsWithValue(mContext, args);
                break;
            case LOADER_ID_GET_FROM_VALUE_RANGE:
                loader = QueryFactory.getColorsInValueRange(mContext, args);
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
}
