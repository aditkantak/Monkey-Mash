package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import projectile.Projectile;
import objects.Object;
/**
 * Represents an entity.
 * 
 * @author Gabriel Kan
 *
 */
public abstract class Entity extends Object{
    protected int x,y;
    protected int speed;
    public int spriteCounter = 0;
    public int spriteNum = 2;
    protected int health;
    protected String direction = "";
    protected boolean isShot = false;
    /**
     * Returns x coordinate
     * @return x
     */
    public int getX(){
        return x;
    }

    /**
     * Returns y coordinate
     * @return y
     */
    public int getY(){
        return y;
    }

    /**
     * If player is shot at, health decreases by 25 
     * and boolean isShot becomes true
     */
    public void shotAt(){
        if (health != 0){
            health = health - 25;
        }
        isShot = true;
    }

    /**
     * Returns hitbox of entity
     * @return rectangle hitbox
     */
    public Rectangle getHitbox(){
        return hitbox;
    }

    /**
     * Abstract method update
     * @return true if updated, false if not
     */
    public abstract boolean update();

    /**
     * Abstract method draw the screen
     * @param g2 graphics
     */
    public abstract void draw(Graphics2D g2);

    /**
     * Sets x coordinate to x
     * @param x x coordinate
     */
    public void setX(int x){
        this.x = x;
    }

    /**
     * Sets y coordinate to y
     * @param y y coordinate
     */
    public void setY(int y){
        this.y = y;
    }

    /**
     * Returns direction 
     * @return direction
     */
    public String getDirection()
    {
        return direction;
    }

    /**
     * Sets direction
     * @param s direction
     */
    public void setDirection(String s)
    {
        direction = s;
    }

    /**
     * Checks for collision based on position of the player
     * @param dx x
     * @param dy y
     * @param object object that is collidable
     * @return true if collided, false if not
     */
    public boolean checkCollision(int dx, int dy, Object object)
    {
        if (super.checkCollision(dx, dy, object) && 
            object instanceof Projectile && !(this instanceof OtherPlayer)){
            shotAt();
        }
        return super.checkCollision(dx, dy, object);
    }
}
