package edu.hitsz;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import edu.hitsz.application.GameActivity;

public class MainActivity extends AppCompatActivity {

    public static int screenWidth;

    public static int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getScreenHW();
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void getScreenHW() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;
        Log.i(TAG, "ScreenWidth:" + screenWidth);

        screenHeight = dm.heightPixels;
        Log.i(TAG, "ScreenHeight: " + screenHeight);
    }
}