package com.example.kirstiebooras.colors.database;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

/**
 * Helper class for all queries. Returns a CursorLoader with the results of the query.
 * Created by kirstiebooras on 4/18/15.
 */
public class QueryFactory {

    public static final String ARG_ID = "id";
    public static final String ARG_SUBSTRING = "substring";
    public static final String ARG_HUE = "hue";
    public static final String ARG_HUE_LOWER = "hueLower";
    public static final String ARG_HUE_UPPER = "hueUpper";
    public static final String ARG_SATURATION = "saturation";
    public static final String ARG_SATURATION_LOWER = "saturationLower";
    public static final String ARG_SATURATION_UPPER = "saturationUpper";
    public static final String ARG_VALUE = "value";
    public static final String ARG_VALUE_LOWER = "valueLower";
    public static final String ARG_VALUE_UPPER = "valueUpper";


    private static final String[] sProjection = { ColorDatabaseContract.FeedEntry._ID,
            ColorDatabaseContract.FeedEntry.COLUMN_COLOR_NAME,
            ColorDatabaseContract.FeedEntry.COLUMN_HUE,
            ColorDatabaseContract.FeedEntry.COLUMN_SATURATION,
            ColorDatabaseContract.FeedEntry.COLUMN_VALUE};
    
    /**
     * Returns a CursorLoader with the name, hue, saturation, and value of the color with the id.
     */
    public static Loader<Cursor> getColorFromId(Context context, Bundle args) {
        String selection = ColorDatabaseContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = String.valueOf(args.getInt(ARG_ID));
        return new CursorLoader(context, ColorContentProvider.CONTENT_URI, sProjection,
                selection, selectionArgs, null);
    }

    /**
     * Returns a CursorLoader with the name, hue, saturation, and value of all colors with the substring in their name
     */
    public static Loader<Cursor> getColorsWithSubstring(Context context, Bundle args) {
        String selection = "color_name LIKE ?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = "%" + args.getString(ARG_SUBSTRING) + "%";
        return new CursorLoader(context, ColorContentProvider.CONTENT_URI, sProjection,
                selection, selectionArgs, null);
    }

    /**
     * Returns a CursorLoader with the name, hue, saturation, and value of all colors with the hue
     */
    public static Loader<Cursor> getColorsWithHue(Context context, Bundle args) {
        String selection = "hue = ?";
        String[] selectionArgs = new String[1];
        int hue = (int) args.getFloat(ARG_HUE);
        selectionArgs[0] = String.valueOf(hue);
        return new CursorLoader(context, ColorContentProvider.CONTENT_URI, sProjection,
                selection, selectionArgs, null);
    }

    /**
     * Returns a CursorLoader with the name, hue, saturation, and value of all colors in the hue range
     */
    public static Loader<Cursor> getColorsInHueRange(Context context, Bundle args) {
        String selection = "hue >= ? AND hue <= ?";
        String[] selectionArgs = new String[2];
        int hueLower = (int) args.getFloat(ARG_HUE, 0.0f);
        int hueupper = (int) args.getFloat(ARG_HUE, 360.0f);
        selectionArgs[0] = String.valueOf(hueLower);
        selectionArgs[1] = String.valueOf(hueupper);
        return new CursorLoader(context, ColorContentProvider.CONTENT_URI, sProjection,
                selection, selectionArgs, null);
    }

    /**
     * Returns a CursorLoader with the name, hue, saturation, and value of all colors with the saturation
     */
    public static Loader<Cursor> getColorsWithSaturation(Context context, Bundle args) {
        String selection = "saturation = ?";
        String[] selectionArgs = new String[1];
        // The values in the database are percentages, so must multiply by 100
        int saturation = (int) (args.getFloat(ARG_SATURATION)*100);
        selectionArgs[0] = String.valueOf(saturation);
        return new CursorLoader(context, ColorContentProvider.CONTENT_URI, sProjection,
                selection, selectionArgs, null);
    }

    /**
     * Returns a CursorLoader with the name, hue, saturation, and value of all colors in the saturation range
     */
    public static Loader<Cursor> getColorsInSaturationRange(Context context, Bundle args) {
        String selection = "saturation >= ? AND saturation <= ?";
        String[] selectionArgs = new String[2];
        // The values in the database are percentages, so must multiply by 100
        int lowerBound = (int) (args.getFloat(ARG_SATURATION_LOWER, 0.0f)*100);
        int upperBound = (int) (args.getFloat(ARG_SATURATION_UPPER, 1.0f)*100);
        selectionArgs[0] = String.valueOf(lowerBound);
        selectionArgs[1] = String.valueOf(upperBound);
        return new CursorLoader(context, ColorContentProvider.CONTENT_URI, sProjection,
                selection, selectionArgs, null);
    }

    /**
     * Returns a CursorLoader with the name, hue, saturation, and value of all colors with the value
     */
    public static Loader<Cursor> getColorsWithValue(Context context, Bundle args) {
        String selection = "value = ?";
        String[] selectionArgs = new String[1];
        // The values in the database are percentages, so must multiply by 100
        int value = (int)(args.getFloat(ARG_VALUE)*100);
        selectionArgs[0] = String.valueOf(value);
        return new CursorLoader(context, ColorContentProvider.CONTENT_URI, sProjection,
                selection, selectionArgs, null);
    }

    /**
     * Returns a CursorLoader with the name, hue, saturation, and value of all colors in the value range
     */
    public static Loader<Cursor> getColorsInValueRange(Context context, Bundle args) {
        String selection = "value >= ? AND value <= ?";
        String[] selectionArgs = new String[2];
        // The values in the database are percentages, so must multiply by 100
        int lowerBound = (int) (args.getFloat(ARG_VALUE_LOWER, 0.0f)*100);
        int upperBound = (int) (args.getFloat(ARG_VALUE_UPPER, 1.0f)*100);
        selectionArgs[0] = String.valueOf(lowerBound);
        selectionArgs[1] = String.valueOf(upperBound);
        return new CursorLoader(context, ColorContentProvider.CONTENT_URI, sProjection,
                selection, selectionArgs, null);
    }


    public static Loader<Cursor> getColorsFromHueSaturationValue(Context context, Bundle args) {
        int hueLower = (int) args.getFloat(ARG_HUE_LOWER, 0.0f);
        int hueUpper = (int) args.getFloat(ARG_HUE_UPPER, 360.0f);
        int satLower = (int) (args.getFloat(ARG_SATURATION_LOWER, 0.0f)*100);
        int satUpper = (int) (args.getFloat(ARG_SATURATION_UPPER, 1.0f)*100);
        int valueLower = (int) (args.getFloat(ARG_VALUE_LOWER, 0.0f)*100);
        int valueUpper = (int) (args.getFloat(ARG_VALUE_UPPER, 1.0f)*100);

        String selection;
        if (hueLower > hueUpper) {
            selection = "(hue >= ? OR hue <= ?) AND saturation >= ? AND saturation <= ? AND value >= ? AND value <= ?";
        } else {
            selection = "hue >= ? AND hue <= ? AND saturation >= ? AND saturation <= ? AND value >= ? AND value <= ?";
        }

        String[] selectionArgs = new String[6];
        selectionArgs[0] = String.valueOf(hueLower);
        selectionArgs[1] = String.valueOf(hueUpper);
        selectionArgs[2] = String.valueOf(satLower);
        selectionArgs[3] = String.valueOf(satUpper);
        selectionArgs[4] = String.valueOf(valueLower);
        selectionArgs[5] = String.valueOf(valueUpper);

        return new CursorLoader(context, ColorContentProvider.CONTENT_URI, sProjection,
                selection, selectionArgs, null);
    }

}
