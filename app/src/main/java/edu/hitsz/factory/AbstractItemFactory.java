package edu.hitsz.factory;

import edu.hitsz.item.AbstractItem;

public abstract class AbstractItemFactory {
    public abstract AbstractItem createItem(int locationX, int locationY, int speedX, int speedY);
}
