package com.example.kirstiebooras.colors.activities;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.kirstiebooras.colors.Gradient;
import com.example.kirstiebooras.colors.OnItemSelectedListener;
import com.example.kirstiebooras.colors.R;
import com.example.kirstiebooras.colors.database.QueryFactory;
import com.example.kirstiebooras.colors.fragments.ColorMatchFragment;
import com.example.kirstiebooras.colors.fragments.NamedColorFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Activity to identify dominant color in a picture
 * Created by kirstiebooras on 4/20/15.
 */
public class IdentifyColorActivity extends FragmentActivity implements OnItemSelectedListener {

    private static final String TAG = "IdentifyColorActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ColorMatchFragment mMatchFragment;
    private NamedColorFragment mSimilarFragment;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_color);

        mMatchFragment = (ColorMatchFragment) getSupportFragmentManager()
                .findFragmentById(R.id.colorMatchFragment);
        mSimilarFragment = (NamedColorFragment) getSupportFragmentManager()
                .findFragmentById(R.id.namedColorFragment);

        Log.v(TAG, "create fragments");

        // Launch the camera and get a photo as a result
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Check that there is a camera app
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (IOException e) {
                Log.e(TAG, "Error while creating image file: " + e.getMessage());
            }
            // Start the camera if the file was successfully created
            if (imageFile != null) {
                mImageUri = Uri.fromFile(imageFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // Create a file name for a new photo
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        // Save a file: path for use with ACTION_VIEW intents
        // String currentPhotoPath = "file:" + imageFile.getAbsolutePath();
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            this.getContentResolver().notifyChange(mImageUri, null);
            Bitmap bitmap = null;
            try {
                bitmap = android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failed to load image: " + e.getMessage());
            }

            if (bitmap != null) {
                float[] hsv = getDominantColor(bitmap);
                findMatch(hsv);
                findSimilarColors(hsv);
            }
        }
    }

    // Retrieve the dominant color from an image
    private float[] getDominantColor(Bitmap bitmap) {
        int reds = 0;
        int greens = 0;
        int blues = 0;
        int pixelCount = 0;

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int color = bitmap.getPixel(x, y);
                pixelCount++;
                reds += Color.red(color);
                greens += Color.green(color);
                blues += Color.blue(color);
            }
        }

        int redAverage = reds/pixelCount;
        int greenAverage = greens/pixelCount;
        int blueAverage = blues/pixelCount;

        float[] hsv = new float[3];
        Color.RGBToHSV(redAverage, greenAverage, blueAverage, hsv);
        Log.v(TAG, "Found color: " + hsv[0] + " " + hsv[1] + " " + hsv[2]);
        return hsv;
    }

    // Query for a match for the color in the database
    private void findMatch(float[] hsv) {
        Cursor cur = QueryFactory.findColorMatch(this, hsv);
        if (cur.getCount() != 0) {
            // Match found!
            Log.d(TAG, "Match found");
            cur.moveToFirst();
            String name = cur.getString(1);
            int hue = cur.getInt(2);
            int saturation = cur.getInt(3);
            int value = cur.getInt(4);
            mMatchFragment.setMatchViewDetails(name, hue, saturation, value);

        } else {
            // No match found
            Log.d(TAG, "No match found");
            mMatchFragment.setNoMatchViewDetails();
        }
    }

    private void findSimilarColors(float[] hsv) {
        int delta = 8;
        float hue = hsv[0];
        float leftHue = (hue < delta) ? 360 - delta : (hue - delta) % 360;
        float rightHue = (hue + delta) % 360;
        Gradient gradient = new Gradient(leftHue, rightHue, hsv[1], hsv[2]);
        mSimilarFragment.setGradient(gradient);
    }

    @Override
    public void onHueSelected(Gradient selected) {

    }

    @Override
    public void onSaturationSelected(Gradient selected) {

    }

    @Override
    public void onValueSelected(Gradient selected) {

    }

    @Override
    public void onStartAgain() {
        // Display the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
