package objects;

import java.awt.Rectangle;

/**
 * Represents an object that collides with player, restricting movement from where the player collides to.
 * 
 * @author Iris Tsai
 *
 */
public abstract class Object {
    protected Rectangle hitbox;

    /**
     * Returns hit box of object
     * @return rectangle hitbox
     */
    public Rectangle getHitBox(){
        return hitbox;
    }

    /**
     * Checks collision of object and another object based on if their
     * hitboxes intersect with each other
     * @param dx x coordinate of object
     * @param dy y coordinate of object
     * @param object object to check for collision
     * @return boolean of true if collision detected and false if not
     */
    public boolean checkCollision(int dx, int dy, Object object)
    {
        int x = (int)Math.round(hitbox.getX() + dx);
        int y = (int)Math.round(hitbox.getY() + dy);
        Rectangle entityHitBoxUp =  new Rectangle(x, y, (int)hitbox.getWidth(), (int)hitbox.getHeight());
        if (entityHitBoxUp.intersects(object.getHitBox()))
        {
            return true;
        }
        return false;
    }
}
