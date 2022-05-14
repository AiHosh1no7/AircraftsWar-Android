package edu.hitsz.strategy;

import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public interface Strategy {
    List<BaseBullet> shoot(int locationX, int locationY, int direction, int aircraftSpeedY, int shootNum, int power);
}
