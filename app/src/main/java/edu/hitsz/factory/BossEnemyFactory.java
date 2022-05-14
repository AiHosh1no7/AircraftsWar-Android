package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;

public class BossEnemyFactory extends AbstractEnemyFactory {
    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        return new BossEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
