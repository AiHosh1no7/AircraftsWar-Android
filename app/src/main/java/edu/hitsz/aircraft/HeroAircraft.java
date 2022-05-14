package edu.hitsz.aircraft;

import edu.hitsz.basic.EnemyInstance;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.AbstractItemFactory;
import edu.hitsz.item.AbstractItem;
import edu.hitsz.strategy.Context;
import edu.hitsz.strategy.HeroStraightStrategy;
import edu.hitsz.strategy.Strategy;

import java.util.List;

public class HeroAircraft extends AbstractAircraft{
    private volatile static HeroAircraft hero;
    public Context fireObj;
    public EnemyInstance trans() {
        return null;
    }

    private HeroAircraft() { }
    public static HeroAircraft getInstance() {
        if(hero == null) {
            synchronized (HeroAircraft.class) {
                if(hero == null) {
                    hero = new HeroAircraft();
                }
            }
        }
        return hero;
    }
    public void setStrategy(Strategy strategy) {
        fireObj.setStrategy(strategy);
    }

    public void setStatus(int locationX, int locationY, int speedX, int speedY, int hp) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.hp = hp;
        this.maxHp = hp;
        this.fireObj = new Context(new HeroStraightStrategy(), locationX, locationY, -1, speedY, 50, 1);
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        fireObj.updateStatus(getLocationX(), getLocationY(), getSpeedY());
        return fireObj.executeStrategy();
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    public AbstractItem dropItem(AbstractItemFactory itemFactory) {
        return null;
    }

    public void increaseHp(int increase) {
        hp += increase;
        if(hp > maxHp) {
            hp = maxHp;
        }
    }
}
