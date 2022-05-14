package edu.hitsz.factory;

import edu.hitsz.item.AbstractItem;
import edu.hitsz.item.FireSupplyItem;

public class FireSupplyItemFactory extends AbstractItemFactory{
    @Override
    public AbstractItem createItem(int locationX, int locationY, int speedX, int speedY) {
        return new FireSupplyItem(locationX, locationY, speedX, speedY);
    }
}
