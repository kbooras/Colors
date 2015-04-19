package com.example.kirstiebooras.colors.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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
    private static final float SATURATION = 1.0f;
    private static final float VALUE = 1.0f;
    private ColorAdapter mAdapter;
    private OnItemSelectedListener mListener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onViewCreated");

        ArrayList<Gradient> colors = createGradients();
        mAdapter = new ColorAdapter(getActivity().getBaseContext(), colors);
        setListAdapter(mAdapter);
    }

    private ArrayList<Gradient> createGradients() {
        Log.v(TAG, "createGradients");
        ArrayList<Gradient> gradients = new ArrayList<>();

        // Create the gradients of pure spectral hues
        float[] hues = new float[]{ 345.0f, 15.0f, 45.0f, 75.0f, 105.0f, 135.0f, 165.0f, 195.0f,
                225.0f, 255.0f, 285.0f, 315.0f, 345.0f };

        for (int i = 0; i < hues.length - 1; i++) {
            gradients.add(new Gradient(hues[i], hues[i+1], SATURATION, VALUE));
        }

        return gradients;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.onHueSelected(mAdapter.getGradient(position));
    }

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

