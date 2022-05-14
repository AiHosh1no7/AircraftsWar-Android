package edu.hitsz.application;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;

import edu.hitsz.R;
import edu.hitsz.aircraft.HeroAircraft;

public class GameActivity extends AppCompatActivity {
    private Game vGame;

    public static int screenWidth;

    public static int screenHeight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vGame = new GameEasy(this);
        loadImg();
        getScreenHW();
        setContentView(vGame);
    }

    public void getScreenHW() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;
        Log.i(TAG, "ScreenWidth:" + screenWidth);

        screenHeight = dm.heightPixels;
        Log.i(TAG, "ScreenHeight: " + screenHeight);
    }

    public void loadImg() {
        ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        ImageManager.HERO_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        ImageManager.MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.mob);
        ImageManager.HERO_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_hero);
        ImageManager.ENEMY_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_enemy);
        ImageManager.ELITE_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.elite);
        ImageManager.BOSS_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.boss);
        ImageManager.HEALING_ITEM_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_blood);
        ImageManager.BOMB_ITEM_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bomb);
        ImageManager.BULLET_ITEM_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bullet);

        ImageManager.CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), ImageManager.HERO_IMAGE);

    }
}
