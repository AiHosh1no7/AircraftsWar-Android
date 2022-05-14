package edu.hitsz.factory;

import edu.hitsz.item.AbstractItem;
import edu.hitsz.item.HealingItem;

public class HealingItemFactory extends AbstractItemFactory{

    @Override
    public AbstractItem createItem(int locationX, int locationY, int speedX, int speedY) {
        return new HealingItem(locationX, locationY, speedX, speedY);
    }
}
