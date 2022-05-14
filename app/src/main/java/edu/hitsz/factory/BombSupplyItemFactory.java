package edu.hitsz.factory;

import edu.hitsz.item.AbstractItem;
import edu.hitsz.item.BombSupplyItem;

public class BombSupplyItemFactory extends AbstractItemFactory{
    @Override
    public AbstractItem createItem(int locationX, int locationY, int speedX, int speedY) {
        return new BombSupplyItem(locationX, locationY, speedX, speedY);
    }
}
