package edu.hitsz.item;

import edu.hitsz.MainActivity;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.HeroSprayStrategy;
import edu.hitsz.strategy.HeroStraightStrategy;

public class FireSupplyItem extends AbstractItem {
    public boolean isBomb = false;

    public FireSupplyItem(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void itemFunction() {
        HeroAircraft hero = HeroAircraft.getInstance();
        Runnable rFunc = () -> {
            hero.fireObj.setStrategy(new HeroSprayStrategy());
            System.out.println("FireSupply active!");
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hero.fireObj.setShootNum(1);
            hero.fireObj.setStrategy(new HeroStraightStrategy());
        };

        Thread tSpray = new Thread(rFunc, "SprayPeriod");
        tSpray.start();
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
