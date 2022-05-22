package edu.hitsz.item;

import edu.hitsz.MainActivity;
import edu.hitsz.aircraft.HeroAircraft;

public class HealingItem extends AbstractItem {
    public boolean isBomb = false;

    private int healHp = 30; //治疗恢复血量数值

    public HealingItem(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void itemFunction() {
        HeroAircraft hero = HeroAircraft.getInstance();
        hero.increaseHp(healHp);
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
