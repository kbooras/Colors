package com.example.kirstiebooras.colors;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Object encapsulating a gradient.
 * Created by kirstiebooras on 4/15/15.
 */
public class Gradient implements Parcelable {

    private float mLeftHue;
    private float mRightHue;
    private float mSaturation;
    private float mValue;

    public Gradient(float leftHue, float rightHue, float saturation, float value) {
        mLeftHue = leftHue;
        mRightHue = rightHue;
        mSaturation = saturation;
        mValue = value;
    }

    public Gradient(Parcel in) {
        float[] data = new float[4];

        in.readFloatArray(data);
        mLeftHue = data[0];
        mRightHue = data[1];
        mSaturation = data[2];
        mValue = data[3];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloatArray(new float[] {mLeftHue, mRightHue, mSaturation, mValue});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Gradient createFromParcel(Parcel in) {
            return new Gradient(in);
        }

        public Gradient[] newArray(int size) {
            return new Gradient[size];
        }
    };

    public float getLeftHue() {
        return mLeftHue;
    }

    public float getRightHue() {
        return mRightHue;
    }

    public float getSaturation() {
        return mSaturation;
    }

    public float getValue() {
        return mValue;
    }

    public int getLeftColor() {
        float[] hsv = new float[] {mLeftHue, mSaturation, mValue};
        return Color.HSVToColor(hsv);
    }

    public int getRightColor() {
        float[] hsv = new float[] {mRightHue, mSaturation, mValue};
        return Color.HSVToColor(hsv);
    }
}
