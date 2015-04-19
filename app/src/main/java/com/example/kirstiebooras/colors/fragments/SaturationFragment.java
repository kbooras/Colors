package com.example.kirstiebooras.colors.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.kirstiebooras.colors.adapters.ColorAdapter;
import com.example.kirstiebooras.colors.Gradient;
import com.example.kirstiebooras.colors.activities.MainActivity;
import com.example.kirstiebooras.colors.OnItemSelectedListener;

import java.util.ArrayList;


/**
 * Displays gradients of the selected hue with varying saturations
 * Created by kirstiebooras on 4/15/15.
 */
public class SaturationFragment extends ListFragment {

    private static final String TAG = "SaturationFragment";
    private static final float VALUE = 1.0f;
    private ColorAdapter mAdapter;
    private OnItemSelectedListener mListener;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG, "onViewCreated");

        ArrayList<Gradient> colors = createGradients();
        mAdapter = new ColorAdapter(getActivity().getBaseContext(), colors);
        setListAdapter(mAdapter);
    }

    private ArrayList<Gradient> createGradients() {
        ArrayList<Gradient> gradients = new ArrayList<>();

        Gradient gradient =((MainActivity) getActivity()).getGradientSelected();

        // Create gradients at varying saturation levels (100% to 0%)
        for (int i = 100; i >=0; i-=10) {
            float saturation = (float) i / 100.0f;
            gradients.add(new Gradient(gradient.getLeftHue(), gradient.getRightHue(),
                    saturation, VALUE));
        }

        return gradients;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.onSaturationSelected(mAdapter.getGradient(position));
    }

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
