package com.example.kirstiebooras.colors;

/**
 * And interface for the fragments to communicate with the MainActivity
 * Created by kirstiebooras on 4/15/15.
 */
public interface OnItemSelectedListener {
    public void onHueSelected(Gradient selected);
    public void onSaturationSelected(Gradient selected);
    public void onValueSelected(Gradient selected);
    public void onStartAgain();
}