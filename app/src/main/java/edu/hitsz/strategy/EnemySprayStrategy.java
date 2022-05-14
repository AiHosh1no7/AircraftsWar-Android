package edu.hitsz.strategy;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

public class EnemySprayStrategy implements Strategy{
    @Override
    public List<BaseBullet> shoot(int locationX, int locationY, int direction, int aircraftSpeedY, int shootNum, int power) {
        if(shootNum < 3) {
            shootNum = 3;
        } else {
            shootNum = (shootNum % 2 == 0) ? shootNum + 1 : shootNum;
        }
        List<BaseBullet> res = new LinkedList<>();
        int x = locationX;
        int y = locationY + direction * 2;
        int bulletSpeedX = -6;
        int bulletSpeedY = aircraftSpeedY + direction * 8;

        BaseBullet baseBullet;
        for(int i = 0; i < shootNum; i++) {
            baseBullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, bulletSpeedX, bulletSpeedY, power);
            bulletSpeedX += 12 / (shootNum + 1);
            res.add(baseBullet);
        }

        return res;
    }
}
