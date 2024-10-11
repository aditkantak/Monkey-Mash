package weapons;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import entity.Player;
import projectile.Bullet;
/**
 * Represents a gun.
 * Has an ArrayList of bullets and updates the bullets position and direction.
 * 
 * @author Gabriel Kan
 *
 */
public class Gun implements Weapon{
    protected ArrayList<Bullet> bullets = new ArrayList<>();

    /**
     * Returns ArrayList of bullets
     * @return bullets
     */
    public ArrayList<Bullet> getBullets(){
        return bullets;
    }

    /**
     * Adds more bullets to the ArrayList and uses bullets for the player to shoot at a target location
     * @param targetLocation target location
     * @param player player
     */
    public void use(Point targetLocation, Player player){
        if(targetLocation == null){
            return;
        }
        Bullet bullet = new Bullet(player, targetLocation);
        bullets.add(bullet);
    }

    /**
     * Updates the bullets shot and used up as the player shoots
     */
    public void update(){
        Iterator<Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            Bullet bullet = iter.next();
            bullet.update();
            if (!bullet.ota.running) {
                iter.remove();
            }
        }
    }

    /**
     * Draws the bullets by iterating through the arrayList
     */
    public void draw(Graphics2D g){
        for (int i = 0; i < bullets.size();i++){
            bullets.get(i).draw(g);
        }
    }
}
