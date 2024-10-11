package projectile;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.Entity;
import entity.OtherPlayer;
import entity.Player;
import gfx.AnimationManager;
import gfx.OneTimeAnimation;
import main.Camera;
import main.Main;
import objects.Wall;

/**
 * Represents a bullet which can shoot at other players and collide into walls.
 * 
 * @author Gabriel Kan
 *
 */
public class Bullet extends Projectile {
    private Point targetLocation;
    private double theta;
    private BufferedImage bullet;
    private ArrayList<Entity> entities;
    private Camera camera;
    AnimationManager anim;
    public OneTimeAnimation ota;
    BufferedImage collision;
    boolean collided = false;
    /**
     * Constructor method for bullet class.
     * Takes image of bullet and sets velocity of the bullet.
     * Calculates the angle which the bullet should travel.
     * @param player player
     * @param targetL point that the bullet targets at
     */
    public Bullet(Player player, Point targetL) {
        this.shooter = player;
        velocity = 8;
        entities = player.getMap().getEntities();
        this.camera = player.camera;
        x = player.getX();
        y = player.getY();
        try {
            if (!(player instanceof OtherPlayer)){
                bullet = ImageIO.read(Main.class.getResource("/lib/gui/energyballscaled.png"));
                collision = ImageIO.read(Main.class.getResource("/lib/gui/energyballcollidescaled.png"));
            }
            else{
                bullet = ImageIO.read(Main.class.getResource("/lib/gui/enemyball.png"));
                collision = ImageIO.read(Main.class.getResource("/lib/gui/enemyballcollision.png")); 
            }
            ota = new OneTimeAnimation(collision);
            anim = new AnimationManager(bullet);
            collision = (BufferedImage)ota.getSprite(0, 0, 32, 32);
            bullet = (BufferedImage)anim.getSprite(0, 0, 32, 32);
            hitbox = new Rectangle((int)x, (int)y, Math.round(bullet.getWidth()) * 3 / 2, Math.round(bullet.getHeight()) * 3 / 2);
            int gameWorldX = targetL.x + camera.getX() - (int)Math.round(bullet.getWidth() / 2.0);
            int gameWorldY = targetL.y + camera.getY() - (int)Math.round(bullet.getHeight() / 2.0);
            targetLocation = new Point(gameWorldX, gameWorldY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        calculateAngle();
    }

    /**
     * Calculates the angle of the bullet.
     */
    public void calculateAngle() {
        theta = Math.atan2(targetLocation.getY() - y, targetLocation.getX() - x);
    }

    /**
     * Returns bullet's angle.
     * @return angle theta
     */
    public double getTheta() {
        return theta;
    }

    /**
     * Sets an angle for the bullet to travel in.
     * @param changeTheta theta to be changed to
     */
    public void setTheta(Double changeTheta) {
        theta = changeTheta;
    }

    /**
     * Updates the bullet animation and movement, with different animation for collisions.
     */
    public boolean update() {
        anim.update();
        bullet = (BufferedImage) anim.getSprite(0,0,32,32);
        if (collided){
            ota.update();
            collision = (BufferedImage) ota.getSprite(0,0,32,32);
        }
        else{
            double dx = velocity * Math.cos(theta);
            double dy = velocity * Math.sin(theta);
            x += dx;
            y += dy;
        }
        for (Entity entity : entities) {
            if (!entity.equals(shooter) && entity.checkCollision(0, 0, this)){
                collided = true;
                hitbox.setSize(0,0);
                return true;
            }
        }
        hitbox.setLocation((int)x, (int)y);
        for (Wall wall : shooter.getMap().getWalls()) {
            if (checkCollision(0, 0, wall)){
                collided = true;
                hitbox.setSize(0,0);
                return true;
            }
        }
        return false;
    }

    /**
     * Sets velocity of bullet
     * @param velocity velocity
     */
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    /**
     * Draws the bullet in the rotated angle in which it will move at and be directed towards. 
     * @param g graphics
     */
    public void draw(Graphics2D g) {
        double locationX = bullet.getWidth() / 2;
        double locationY = bullet.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(theta, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        if (collided){
            g.drawImage(op.filter(collision, null), (int)Math.round(x), (int)Math.round(y), Math.round(bullet.getWidth()) * 3 , Math.round(bullet.getHeight()) * 3, null);
            hitbox.setSize(0, 0);
        }
        else{
            //g.draw(hitbox);
            g.drawImage(op.filter(bullet, null), (int)Math.round(x), (int)Math.round(y), Math.round(bullet.getWidth()) * 3, Math.round(bullet.getHeight()) * 3, null);
        }
    }
}
