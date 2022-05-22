package edu.hitsz;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.content.ServiceConnection;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.Game;
import edu.hitsz.application.GameEasy;
import edu.hitsz.application.GameHard;
import edu.hitsz.application.GameMedium;
import edu.hitsz.application.ImageManager;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.item.BombSupplyItem;
import edu.hitsz.item.FireSupplyItem;
import edu.hitsz.item.HealingItem;

public class MainActivity extends AppCompatActivity {
    private Game vGame;

    public static int screenWidth;

    public static int screenHeight;

    public static boolean bgmFlag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent prevIntent = getIntent();
        getScreenHW();
        loadImg();
        switch(prevIntent.getIntExtra("diff", 1)) {
            case 0:
                ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
                bgmFlag = prevIntent.getBooleanExtra("audio", false);
                vGame = new GameEasy(this);
                System.out.println("EasyMode");
                break;
            case 1:
                ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
                bgmFlag = prevIntent.getBooleanExtra("audio", false);
                vGame = new GameMedium(this);
                break;
            case 2:
                ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg3);
                bgmFlag = prevIntent.getBooleanExtra("audio", false);
                vGame = new GameHard(this);
                break;
        }
        System.out.println("Game Activity has been created.");
        setContentView(vGame);
        vGame.action();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            double x = event.getX();
            double y = event.getY();

            Log.i(TAG, "x = " + x + " y = " + y);
            if (x < 0 || x > screenWidth || y < 0 || y > screenHeight){
                // 防止超出边界
                return false;
            }
            vGame.heroAircraft.setLocation(x, y);
            Log.i(TAG, "Now HeroAircraft Location:" + vGame.heroAircraft.getLocationX() + " " + vGame.heroAircraft.getLocationY());
        }

        return true;
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
        ImageManager.CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), ImageManager.MOB_ENEMY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), ImageManager.HERO_BULLET_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ImageManager.ENEMY_BULLET_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ImageManager.ELITE_ENEMY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), ImageManager.BOSS_ENEMY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(HealingItem.class.getName(), ImageManager.HEALING_ITEM_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(BombSupplyItem.class.getName(), ImageManager.BOMB_ITEM_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(FireSupplyItem.class.getName(), ImageManager.BULLET_ITEM_IMAGE);
    }
}