package com.example.kirstiebooras.colors.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;

/**
 * ContentProvider for the colors db
 * Created by kirstiebooras on 4/17/15.
 */
public class ColorContentProvider extends ContentProvider {

    private SQLiteDatabase mDB;

    private static final int COLORS = 10;
    private static final int COLOR_ID = 20;

    private static final String AUTHORITY = "com.example.kirstiebooras.provider.ColorContentProvider";
    private static final String BASE_PATH = "colors";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/colors";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/color";


    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, COLORS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", COLOR_ID);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        ColorDBHelper dbHelper = new ColorDBHelper(context);
        // permissions to be writable
        mDB = dbHelper.getWritableDatabase();
        return mDB != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        queryBuilder.setTables(ColorDatabaseContract.FeedEntry.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case COLORS:
                break;
            case COLOR_ID:
                queryBuilder.appendWhere(ColorDatabaseContract.FeedEntry._ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(mDB, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    // Check the column requested is valid
    private void checkColumns(String[] projection) {
        String[] available = { ColorDatabaseContract.FeedEntry._ID,
                ColorDatabaseContract.FeedEntry.COLUMN_COLOR_NAME,
                ColorDatabaseContract.FeedEntry.COLUMN_HEX,
                ColorDatabaseContract.FeedEntry.COLUMN_GREEN,
                ColorDatabaseContract.FeedEntry.COLUMN_BLUE,
                ColorDatabaseContract.FeedEntry.COLUMN_HUE,
                ColorDatabaseContract.FeedEntry.COLUMN_SATURATION_HSL,
                ColorDatabaseContract.FeedEntry.COLUMN_LIGHT,
                ColorDatabaseContract.FeedEntry.COLUMN_SATURATION,
                ColorDatabaseContract.FeedEntry.COLUMN_VALUE};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }


    /**
     * For debugging purposes
     */
    public void printTableData(){
        Cursor cur = mDB.rawQuery("SELECT * FROM " + ColorDatabaseContract.FeedEntry.TABLE_NAME, null);
        if(cur.getCount() != 0){
            cur.moveToFirst();
            do{
                String row_values = "";
                for(int i = 0 ; i < cur.getColumnCount(); i++){
                    row_values = row_values + " || " + cur.getString(i);
                }
                Log.d("LOG_TAG_HERE", row_values);
            }while (cur.moveToNext());
        }
        cur.close();
    }
}
