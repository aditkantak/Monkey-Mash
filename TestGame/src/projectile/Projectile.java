package projectile;
import entity.Player;
import objects.Object;
/**
 * Abstract class that represents a projectile and is an object.
 * 
 * @author Gabriel Kan
 *
 */
public abstract class Projectile extends Object{
    protected double x,y;
    protected int velocity;
    protected Player shooter;

    /**
     * Abstract method update
     * @return true if updated false if not
     */
    abstract boolean update();
}
