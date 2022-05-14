package edu.hitsz.application;


import android.graphics.Bitmap;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.item.BombSupplyItem;
import edu.hitsz.item.FireSupplyItem;
import edu.hitsz.item.HealingItem;

import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    public static final Map<String, Bitmap> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static Bitmap BACKGROUND_IMAGE;
    public static Bitmap HERO_IMAGE;
    public static Bitmap HERO_BULLET_IMAGE;
    public static Bitmap ENEMY_BULLET_IMAGE;
    public static Bitmap MOB_ENEMY_IMAGE;
    public static Bitmap ELITE_ENEMY_IMAGE;
    public static Bitmap BOSS_ENEMY_IMAGE;
    public static Bitmap HEALING_ITEM_IMAGE;
    public static Bitmap BOMB_ITEM_IMAGE;
    public static Bitmap BULLET_ITEM_IMAGE;

    public static String diffBG = "src/images/bg.jpg";

    public static Bitmap get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static Bitmap get(Object obj){
        if (obj == null) {
            return null;
        }
        return get(obj.getClass().getName());
    }

}
