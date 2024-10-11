package weapons;

import java.awt.Point;

import entity.Player;
import projectile.Bullet;

/**
 * 
 * Has a different spray pattern than normal handgun where multiple bullets are shot at once
 * 
 * @author Iris Tsai, Gabriel Kan
 *
 */
public class Shotgun extends Gun{
    @Override
    /**
     * Adds more bullets to the ArrayList and uses bullets for the player to shoot at a target location
     * @param targetLocation target location
     * @param player player
     */
    public void use(Point targetLocation, Player player){
        if(targetLocation == null){
            return;
        }
        int amt = 45;
        for (int i = 0; i < amt;i++){
            Bullet bullet = new Bullet(player, targetLocation);
            bullet.setVelocity(3);
            bullet.setTheta(bullet.getTheta() + Math.toRadians((i-amt/2) * 8 ));
            bullets.add(bullet);
        }
    }
    
}
