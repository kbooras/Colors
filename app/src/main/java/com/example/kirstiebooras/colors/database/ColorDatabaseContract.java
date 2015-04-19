package com.example.kirstiebooras.colors.database;


import android.provider.BaseColumns;

/**
 * The db schema
 * Created by kirstiebooras on 4/17/15.
 */
public class ColorDatabaseContract {

    public ColorDatabaseContract() {}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "ColorDB";
        public static final String COLUMN_COLOR_NAME = "color_name";
        public static final String COLUMN_HEX = "hex";
        public static final String COLUMN_RED = "red";
        public static final String COLUMN_GREEN = "green";
        public static final String COLUMN_BLUE = "blue";
        public static final String COLUMN_HUE = "hue";
        public static final String COLUMN_SATURATION_HSL = "saturation_hsl";
        public static final String COLUMN_LIGHT = "light";
        public static final String COLUMN_SATURATION = "saturation";
        public static final String COLUMN_VALUE = "value";
        public static final String INTEGER_TYPE = " INTEGER";
        public static final String TEXT_TYPE = " TEXT";
        public static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.COLUMN_COLOR_NAME + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_HEX + TEXT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_RED + INTEGER_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_GREEN + INTEGER_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_BLUE + INTEGER_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_HUE + INTEGER_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_SATURATION_HSL + INTEGER_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_LIGHT + INTEGER_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_SATURATION + INTEGER_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_VALUE + INTEGER_TYPE +
                        " );";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    }
}

