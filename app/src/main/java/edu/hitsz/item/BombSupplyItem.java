package edu.hitsz.item;

import edu.hitsz.MainActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.Game;
import edu.hitsz.basic.EnemyInstance;
import edu.hitsz.bullet.BaseBullet;

import java.util.ArrayList;
import java.util.List;

public class BombSupplyItem extends AbstractItem {
    public boolean isBomb = true;

    List<EnemyInstance> enemyList = new ArrayList<>();

    public void addEnemyInstance(EnemyInstance e) {
        enemyList.add(e);
    }

    public void removeEnemyInstance(EnemyInstance e) {
        enemyList.remove(e);
    }

    public void updateAll() {
        for(AbstractAircraft enemy: Game.enemyAircrafts) {
            if(!enemy.bossFlag) {
                addEnemyInstance(enemy.trans());
            }
        }
        for(BaseBullet bullet: Game.enemyBullets) {
            addEnemyInstance(bullet.trans());
        }
        for(EnemyInstance enemy: enemyList) {
            enemy.update();
        }
    }
    public BombSupplyItem(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void itemFunction() {
        updateAll();
        System.out.println("BombSupply active!");
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.screenHeight || locationY <= 0) {
            speedY = -speedY;
        }
    }
}
