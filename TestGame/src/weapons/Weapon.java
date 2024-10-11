package weapons;

import java.awt.Graphics2D;
import java.awt.Point;

import entity.Player;

/**
 * Framework for various weapons
 * 
 * @author Iris Tsai, Gabriel Kan
 *
 */
public interface Weapon {
    /**
     * Adds more bullets to the ArrayList and uses bullets for the player to shoot at a target location
     * @param targetLocation target location
     * @param player player
     */
    public void use(Point targetLocation, Player player);

    /**
     * Updates the bullets shot and used up as the player shoots
     */
    public void update();

    /**
     * Draws the bullets by iterating through the arrayList
     */
    public void draw(Graphics2D g);
}
