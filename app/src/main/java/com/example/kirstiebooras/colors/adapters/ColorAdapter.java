package com.example.kirstiebooras.colors.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.kirstiebooras.colors.Gradient;

import java.util.ArrayList;

/**
 * Adapter to set listview gradients.
 * Created by kirstiebooras on 4/15/15.
 */
public class ColorAdapter extends ArrayAdapter<Gradient> {

    private Context mContext;
    private ArrayList<Gradient> mGradients;

    public ColorAdapter(Context context, ArrayList<Gradient> gradients) {
        super(context, android.R.layout.simple_list_item_1, gradients);
        mContext = context;
        mGradients = gradients;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        } else {
            rowView = convertView;
        }

        Gradient gradient = mGradients.get(position);
        GradientDrawable drawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {gradient.getLeftColor(), gradient.getRightColor()});

        rowView.setBackground(drawable);

        return rowView;
    }

    public Gradient getGradient(int position) {
        return mGradients.get(position);
    }
}
