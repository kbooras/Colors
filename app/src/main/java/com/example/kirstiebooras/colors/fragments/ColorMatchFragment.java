package com.example.kirstiebooras.colors.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kirstiebooras.colors.R;

/**
 * Created by kirstiebooras on 4/20/15.
 */
public class ColorMatchFragment extends Fragment {

    private TextView mMatchStatus;
    private ImageView mColorSwatch;
    private TextView mColorDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_color_match, container, false);

        mMatchStatus = (TextView) view.findViewById(R.id.matchStatus);
        mColorSwatch = (ImageView) view.findViewById(R.id.colorSwatch);
        mColorDetails = (TextView) view.findViewById(R.id.colorDetails);

        return view;
    }

    public void setNoMatchViewDetails() {
        mMatchStatus.setText(getString(R.string.no_match));
    }

    public void setMatchViewDetails(String name, int hue, int sat, int value) {
        mMatchStatus.setText(getString(R.string.exact_match));
        // mColorSwatch.setBackground();
        mColorDetails.setText(String.format(getString(R.string.color_details), name, hue, sat, value));
    }

}
