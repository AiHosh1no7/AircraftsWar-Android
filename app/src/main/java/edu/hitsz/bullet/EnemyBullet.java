package edu.hitsz.bullet;

import edu.hitsz.basic.EnemyInstance;

/**
 * @Author hitsz
 */
public class EnemyBullet extends BaseBullet implements EnemyInstance {

    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    public void info() {
        System.out.println("EnemyBullet");
    }
    public void update() {
        vanish();
    }

    @Override
    public EnemyInstance trans() {
        return this;
    }

}
