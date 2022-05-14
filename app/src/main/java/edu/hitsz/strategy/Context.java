package edu.hitsz.strategy;

import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public class Context {
    private int locationX;
    private int locationY;
    private int direction;
    private int aircraftSpeedY;
    private int power;
    private int shootNum;
    private Strategy strategy;
    public Context(Strategy strategy, int locationX, int locationY, int direction, int aircraftSpeedY, int power, int shootNum) {
        this.strategy = strategy;
        this.locationX = locationX;
        this.locationY = locationY;
        this.direction = direction;
        this.aircraftSpeedY = aircraftSpeedY;
        this.power = power;
        this.shootNum = shootNum;
    }
    public void updateStatus(int locationX, int locationY, int aircraftSpeedY) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.aircraftSpeedY = aircraftSpeedY;
    }

    public void setShootNum(int shootNum) {
        this.shootNum = shootNum;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<BaseBullet> executeStrategy() {
        return strategy.shoot(locationX, locationY, direction, aircraftSpeedY, shootNum, power);
    }
}
