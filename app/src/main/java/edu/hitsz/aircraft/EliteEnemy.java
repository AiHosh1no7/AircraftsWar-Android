package edu.hitsz.aircraft;

import edu.hitsz.MainActivity;
import edu.hitsz.basic.EnemyInstance;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.AbstractItemFactory;
import edu.hitsz.item.AbstractItem;
import edu.hitsz.strategy.Context;
import edu.hitsz.strategy.EnemyStraightStrategy;

import java.util.List;

public class EliteEnemy extends AbstractAircraft implements EnemyInstance {
    public Context fireObj;

    public void update() {
        vanish();
    }

    public void info() {
        System.out.println("EliteEnemy");
    }

    public EnemyInstance trans() {
        return this;
    }

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        bossFlag = false;
        eliteFlag = true;
        fireObj = new Context(new EnemyStraightStrategy(), locationX, locationY, 1, speedY, 20, 2);
    }

    @Override
    public void forward() {
        super.forward();

        if(locationY >= MainActivity.screenHeight) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        fireObj.updateStatus(getLocationX(), getLocationY(), getSpeedY());
        return fireObj.executeStrategy();
    }

    @Override
    public AbstractItem dropItem(AbstractItemFactory itemFactory) {
        double dirProb = Math.random();

        return itemFactory.createItem(locationX, locationY, dirProb >= 0.5 ? -2 : 2, 5);
    }
}
