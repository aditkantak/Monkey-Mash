package main;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;
/**
 * Represents a mouse handler that takes user mouse movement and performs certain actions based on that. 
 * Gives that data to the player to make a bullet array in order to shoot bullets.
 * 
 * @author Gabriel Kan
 *
 */
public class MouseHandler implements MouseMotionListener, MouseListener{

    public boolean rightClick = false;
    private boolean leftClick = false;
    private Point location = new Point();

    /**
     * Returns mouse position on the game panel as a Point
     * @param gp game panel
     * @return mouse position
     */
    public Point getLocation(GamePanel gp){
        if (gp.getMousePosition() != null){
            location = gp.getMousePosition();
        }
        return location;
    }

    /**
     * Returns if the user left clicks
     * @return true if left click, false if not
     */
    public boolean leftPressed(){
        return leftClick;
    }

    /**
     * Sets left click to true if left was pressed
     * and false if not
     * @param left boolean for leftPressed
     */
    public void setLeft(boolean left){
        leftClick = left;
    }

    /**
     * Mouse moved
     * @param e mouse event
     */
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Sets location to where mouse was pressed
     * @param e mouse event
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        location = e.getPoint();
    }

    /**
     * Mouse clicked
     * @param e mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Left click is true and sets location to where the 
     * mouse was pressed
     * @param e mouse event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        location = e.getPoint();
        leftClick = true;
    }

    /**
     * Left click is false
     * @param e mouse event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        leftClick = false;
    }

    /**
     * Mouse enter
     * @param e mouse event
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    /**
     * Mouse exit
     * @param e mouse event
     */
    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
}
