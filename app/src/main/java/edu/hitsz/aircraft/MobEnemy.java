package edu.hitsz.aircraft;

import edu.hitsz.MainActivity;
import edu.hitsz.basic.EnemyInstance;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.AbstractItemFactory;
import edu.hitsz.item.AbstractItem;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft implements EnemyInstance {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        bossFlag = false;
        eliteFlag = false;
    }

    public void info() {
        System.out.println("MobEnemy");
    }
    public EnemyInstance trans() {
        return this;
    }

    public void update() {
        vanish();
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.screenHeight) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public AbstractItem dropItem(AbstractItemFactory itemFactory) {
        return null;
    }

}
