package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.MobEnemy;

public class MobEnemyFactory extends AbstractEnemyFactory {
    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        return new MobEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
