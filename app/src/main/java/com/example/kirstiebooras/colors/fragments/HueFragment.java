package com.example.kirstiebooras.colors.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kirstiebooras.colors.R;
import com.example.kirstiebooras.colors.adapters.ColorAdapter;
import com.example.kirstiebooras.colors.Gradient;
import com.example.kirstiebooras.colors.OnItemSelectedListener;

import java.util.ArrayList;

/**
 * Displays gradients with varying hues
 * Created by kirstiebooras on 4/15/15.
 */
public class HueFragment extends ListFragment {

    private static final String TAG = "HueFragment";
    private static final String CENTRAL_HUE = "centralHue";
    private static final String NUM_SWATCHES = "numSwatchesHue";
    private static final int SATURATION = 100;
    private static final int VALUE = 100;
    private int mCentralHue;
    private int mNumSwatches;
    private SharedPreferences mSharedPref;
    private ColorAdapter mAdapter;
    private OnItemSelectedListener mListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(TAG, "onAttach");
        if (activity instanceof OnItemSelectedListener) {
            mListener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_listview_button, container, false);

        Button button = (Button) view.findViewById(R.id.configureSwatchesButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createColorSwatchDialog();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onViewCreated");

        // Get persisted central hue and number of swatches
        Context context = getActivity().getBaseContext();
        mSharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mCentralHue = (savedInstanceState != null) ?
                       savedInstanceState.getInt(CENTRAL_HUE) :
                       mSharedPref.getInt(getString(R.string.saved_central_hue), 0);
        mNumSwatches = (savedInstanceState != null) ?
                        savedInstanceState.getInt(NUM_SWATCHES) :
                        mSharedPref.getInt(getString(R.string.saved_hue_num_swatches), 10);

        ArrayList<Gradient> gradients = createGradients();
        Log.v(TAG, "size: " + gradients.size() + "  " + getListView().getCount());
        mAdapter = new ColorAdapter(getActivity().getBaseContext(), gradients);
        setListAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CENTRAL_HUE, mCentralHue);
        outState.putInt(NUM_SWATCHES, mNumSwatches);
    }


    private void createColorSwatchDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_hue_swatches, null);

        final TextView hueText = (TextView) view.findViewById(R.id.centerHue);
        hueText.setText(String.format(getString(R.string.center_hue), mCentralHue));

        final TextView swatchText = (TextView) view.findViewById(R.id.numSwatchesTxt);
        swatchText.setText(String.format(getString(R.string.num_swatches), mNumSwatches));

        final ImageView imageView = (ImageView) view.findViewById(R.id.huePreview);
        float[] hsv = new float[] {mCentralHue, SATURATION, VALUE};
        imageView.setBackgroundColor(Color.HSVToColor(hsv));

        SeekBar seekHue = (SeekBar) view.findViewById(R.id.seekHue);
        seekHue.setProgress(mCentralHue);
        seekHue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCentralHue = progress;
                hueText.setText(String.format(getString(R.string.center_hue), progress));
                float[] hsv = new float[] {mCentralHue, SATURATION, VALUE};
                imageView.setBackgroundColor(Color.HSVToColor(hsv));

                // Save the number of swatches to be used later
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putInt(getString(R.string.saved_central_hue), mCentralHue);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        SeekBar seekSwatches = (SeekBar) view.findViewById(R.id.seekSwatches);
        seekSwatches.setProgress(mNumSwatches);
        seekSwatches.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mNumSwatches = progress;
                swatchText.setText(String.format(getString(R.string.num_swatches), progress));

                // Save the number of swatches to be used later
                SharedPreferences.Editor editor = mSharedPref.edit();
                editor.putInt(getString(R.string.saved_hue_num_swatches), mNumSwatches);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.configure_hue_swatches)
                .setView(view)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(TAG, "updateCentralHue: " + mCentralHue + "updateNumSwatches: " + mNumSwatches);
                        mAdapter.clear();
                        ArrayList<Gradient> gradients = createGradients();
                        Log.v(TAG, "size: " + gradients.size());
                        mAdapter.addAll(gradients);
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private ArrayList<Gradient> createGradients() {
        Log.v(TAG, "createGradients");
        ArrayList<Gradient> gradients = new ArrayList<>(mNumSwatches);

        int interval = 360/mNumSwatches;

        // Create the hues array
        int[] hues = new int[mNumSwatches];
        // Take care of possible negative values
        hues[0] = (mCentralHue < interval/2) ? 360 - interval/2 : (mCentralHue - interval/2) % 360;
        hues[1] = (mCentralHue + interval/2) % 360;
        for (int i = 2; i < mNumSwatches; i++) {
            hues[i] = (hues[i-1] + interval) % 360;
        }

        // Create the gradients from the hues
        int i;
        for (i = 0; i < hues.length - 1; i++) {
            gradients.add(new Gradient((float)hues[i], (float)hues[i+1], SATURATION, VALUE));
        }
        // Make the last gradient using the first hue as the right color so the colors will  be circular
        gradients.add(new Gradient((float) hues[i], (float) hues[0], SATURATION, VALUE));
        return gradients;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.onHueSelected(mAdapter.getGradient(position));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

