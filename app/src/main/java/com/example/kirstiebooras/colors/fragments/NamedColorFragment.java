package com.example.kirstiebooras.colors.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kirstiebooras.colors.Gradient;
import com.example.kirstiebooras.colors.OnItemSelectedListener;
import com.example.kirstiebooras.colors.R;
import com.example.kirstiebooras.colors.activities.ColorExplorerActivity;
import com.example.kirstiebooras.colors.database.ColorDatabaseContract;
import com.example.kirstiebooras.colors.database.QueryFactory;

/**
 * Displays a list of named colors in the selected range.
 * Created by kirstiebooras on 4/15/15.
 */
public class NamedColorFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "NamedColorFragment";

    private static final int LOADER_ID_GET_FROM_HSV = 1;
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;
    private SharedPreferences mSharedPref;
    private SimpleCursorAdapter mAdapter;
    private Gradient mGradient;
    private Context mContext;
    private ImageView mImageView;
    private TextView mTextView;

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

        mImageView = (ImageView) view.findViewById(R.id.colorPreview);
        mTextView = (TextView) view.findViewById(R.id.gradientDetails);

        Button startAgainButton = (Button) view.findViewById(R.id.startAgainButton);
        startAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStartAgain();
            }
        });

        Button sortOrderButton = (Button) view.findViewById(R.id.sortOrderButton);
        sortOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSortOrderDialog();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] dataColumns = { ColorDatabaseContract.FeedEntry.COLUMN_COLOR_NAME };
        int[] viewIds = { android.R.id.text1 };

        // Load the named colors in the range into the listView
        mContext = getActivity().getBaseContext();
        mAdapter = new SimpleCursorAdapter(mContext,
                android.R.layout.simple_list_item_1, null,
                dataColumns, viewIds, 0);
        setListAdapter(mAdapter);

        mSharedPref = mContext.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        mCallbacks = this;

        // If this is part of the ColorExplorerActivity, we can immediately set the view details
        if (getActivity() instanceof ColorExplorerActivity) {
            setGradient(((ColorExplorerActivity) getActivity()).getGradientSelected());
        }
    }

    // Sets up the view based on the gradient passed in
    public void setGradient(Gradient gradient) {
        mGradient = gradient;
        setViewDetails();
        initLoader();
    }

    private void setViewDetails() {
        String leftHue = String.valueOf(mGradient.getLeftHue());
        String rightHue = String.valueOf(mGradient.getRightHue());
        String saturation = String.valueOf(mGradient.getSaturation());
        String value = String.valueOf(mGradient.getValue());

        mImageView.setBackground(new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{mGradient.getLeftColor(), mGradient.getRightColor()}));

        mTextView.setText(String.format(getString(R.string.gradient_details), leftHue, rightHue,
                saturation, value));
    }

    private void initLoader() {
        // Get persisted sortOrder and create bundle
        Bundle args = createBundle(mSharedPref.getString(getString(R.string.saved_sort_order), null));
        // Create loader with this bundle
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID_GET_FROM_HSV, args, mCallbacks);
    }

    private void createSortOrderDialog() {
        int selected = mSharedPref.getInt(getString(R.string.saved_sort_order_index), -1);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.choose_sort_order)
                .setSingleChoiceItems(R.array.sortArray, selected, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int position = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        Log.v(TAG, "SortOrder dialog clicked " + position);
                        String sortOrder = null;
                        switch (position) {
                            case 0:
                                sortOrder = "hue, saturation, value";
                                break;
                            case 1:
                                sortOrder = "hue, value, saturation";
                                break;
                            case 2:
                                sortOrder = "saturation, hue, value";
                                break;
                            case 3:
                                sortOrder = "saturation, value, hue";
                                break;
                            case 4:
                                sortOrder = "value, hue, saturation";
                                break;
                            case 5:
                                sortOrder = "value, saturation, hue";
                                break;
                            default:
                                break;
                        }
                        // Save the sortOrder to be used later
                        SharedPreferences.Editor editor = mSharedPref.edit();
                        editor.putString(getString(R.string.saved_sort_order), sortOrder);
                        editor.putInt(getString(R.string.saved_sort_order_index), position);
                        editor.apply();

                        // Update the sortOrder for the cursor
                        Bundle args = createBundle(sortOrder);
                        getLoaderManager().restartLoader(LOADER_ID_GET_FROM_HSV, args, mCallbacks);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    // Create the bundle to be used by the loader
    private Bundle createBundle(String sortOrder) {
        Bundle args = new Bundle();
        args.putFloat(QueryFactory.ARG_HUE_LOWER, mGradient.getLeftHue());
        args.putFloat(QueryFactory.ARG_HUE_UPPER, mGradient.getRightHue());
        args.putFloat(QueryFactory.ARG_SATURATION_LOWER, mGradient.getSaturation());
        args.putFloat(QueryFactory.ARG_SATURATION_UPPER, mGradient.getSaturation());
        args.putFloat(QueryFactory.ARG_VALUE_LOWER, mGradient.getValue());
        args.putFloat(QueryFactory.ARG_VALUE_UPPER, mGradient.getValue());
        args.putString(QueryFactory.ARG_SORT_ORDER, sortOrder);

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

        Toast.makeText(mContext, String.format(getString(R.string.color_details), name, hue,
                saturation, value), Toast.LENGTH_LONG).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader;
        switch (id) {
            case LOADER_ID_GET_FROM_HSV:
                loader = QueryFactory.getColorsFromHueSaturationValue(mContext, args);
                break;
            default:
                throw new IllegalArgumentException("Invalid CursorLoader id");
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
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
}
