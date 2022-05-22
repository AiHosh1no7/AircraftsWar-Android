package edu.hitsz.item;

import edu.hitsz.basic.AbstractFlyingObject;

public abstract class AbstractItem extends AbstractFlyingObject {
    public boolean isBomb;

    public AbstractItem(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public abstract void itemFunction();

}
