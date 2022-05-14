package edu.hitsz.application;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;

import edu.hitsz.R;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.item.BombSupplyItem;
import edu.hitsz.item.FireSupplyItem;
import edu.hitsz.item.HealingItem;

public class GameActivity extends AppCompatActivity {
    private Game vGame;

    public static int screenWidth;

    public static int screenHeight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScreenHW();
        loadImg();
        vGame = new GameEasy(this);
        System.out.println("Game Activity has been created.");
        setContentView(vGame);
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
