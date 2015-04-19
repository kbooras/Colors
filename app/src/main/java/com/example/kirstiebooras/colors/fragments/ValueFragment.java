package com.example.kirstiebooras.colors.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kirstiebooras.colors.R;
import com.example.kirstiebooras.colors.adapters.ColorAdapter;
import com.example.kirstiebooras.colors.Gradient;
import com.example.kirstiebooras.colors.activities.MainActivity;
import com.example.kirstiebooras.colors.OnItemSelectedListener;

import java.util.ArrayList;

/**
 * Displays gradients of the selected hue and saturation with varying values
 * Created by kirstiebooras on 4/15/15.
 */
public class ValueFragment extends ListFragment {

    private static final String TAG = "ValueFragment";
    private static final String NUM_SWATCHES_VALUE = "numSwatchesValue";
    private int mNumSwatches;
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NUM_SWATCHES_VALUE, mNumSwatches);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG, "onViewCreated");

        mNumSwatches = (savedInstanceState != null) ? savedInstanceState.getInt(NUM_SWATCHES_VALUE) : 10;
        mAdapter = new ColorAdapter(getActivity().getBaseContext(), createGradients());
        setListAdapter(mAdapter);
    }

    private void createColorSwatchDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_num_swatches, null);

        final TextView textView = (TextView) view.findViewById(R.id.numSwatchesTxt);
        textView.setText(String.format(getString(R.string.num_swatches), mNumSwatches));

        SeekBar seek = (SeekBar) view.findViewById(R.id.seek);
        seek.setProgress(mNumSwatches);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mNumSwatches = progress;
                textView.setText(String.format(getString(R.string.num_swatches), progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.choose_number_of_swatches)
                .setView(view)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(TAG, "updateNumSwatches: " + mNumSwatches);
                        mAdapter.clear();
                        mAdapter.addAll(createGradients());
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private ArrayList<Gradient> createGradients() {
        ArrayList<Gradient> gradients = new ArrayList<>(mNumSwatches);

        Gradient gradient =((MainActivity) getActivity()).getGradientSelected();

        double interval = 1.0/mNumSwatches;

        // Create gradients at varying saturation levels (100% to 0%)
        double value = 1.0;
        for (int i = 0; i < mNumSwatches; i++) {
            gradients.add(new Gradient(gradient.getLeftHue(), gradient.getRightHue(),
                    gradient.getSaturation(), (float)value));
            value -= interval;
        }

        return gradients;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.onValueSelected(mAdapter.getGradient(position));
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
