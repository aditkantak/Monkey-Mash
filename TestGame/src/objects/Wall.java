package objects;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Represents a wall object in which the player can collide onto.
 * 
 * @author Iris Tsai
 *
 */
public class Wall extends Object{
    int x, y, width, height;
    public boolean collision;
    /**
     * Constructor method. 
     * Sets a hit box for the wall.
     * @param x x coordinate
     * @param y y coordinate
     * @param width width of the wall
     * @param height heigh of the wall
     */
    public Wall(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        collision = true;
        hitbox = new Rectangle(x, y, width, height);
    }

    /**
     * Draws the wall along designated areas to bound the 
     * square-shaped game map.
     * @param g2d graphics
     */
    public void draw(Graphics2D g2d)
    {
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);
        g2d.setColor(Color.GRAY);
        g2d.fillRect(x+1, y+1, width-2, height-2);
    }
}



