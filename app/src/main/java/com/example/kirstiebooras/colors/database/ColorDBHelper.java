package com.example.kirstiebooras.colors.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The DBHelper
 * Created by kirstiebooras on 4/17/15.
 */
public class ColorDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Color.db";
    private static final String LOG_TAG = "ColorDBHelper";
    private Context mContext;

    public ColorDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ColorDatabaseContract.FeedEntry.SQL_CREATE_ENTRIES);
        createDatabaseFromTextFile(db);
        initDatabaseData(db);
    }

    private void createDatabaseFromTextFile(SQLiteDatabase db) {
        db.execSQL(ColorDatabaseContract.FeedEntry.SQL_DELETE_ENTRIES);
        db.execSQL(ColorDatabaseContract.FeedEntry.SQL_CREATE_ENTRIES);
    }

    private void initDatabaseData(SQLiteDatabase db) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(mContext.getAssets().open("color.txt")), 1024 * 4);
            String line;
            db.beginTransaction();
            while ((line = br.readLine()) != null) {
                db.execSQL(line);
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Read database init file error " + e.toString());
        } finally {
            db.endTransaction();
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Buffer reader close error " + e.toString());
                }
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
    }
}

