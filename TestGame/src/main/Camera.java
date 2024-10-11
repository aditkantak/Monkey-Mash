package main;

import java.awt.Point;

import map.GameMap;
/**
 * Represents the camera in which the player is restricted to seeing only a certain part of the game map during the game. 
 * 
 * @author Gabriel Kan
 *
 */
public class Camera {
    private int x;
    private int y;
    private MouseHandler mHandler;
    private GamePanel gp;

    /**
     * Constructor method for Camera class. 
     * @param mHandler mouse handler
     * @param gp game panel
     */
    public Camera(MouseHandler mHandler, GamePanel gp){
        this.mHandler = mHandler;
        this.gp = gp;
    }

    /**
     * Sets the location of the camera based on where the player is located as well as the point which the mouse hoovers over/points to.
     * @param dx change in x direction of the player
     * @param dy change in y direction of the player
     * @param playerx player x coordinate
     * @param playery player y coordinate
     */
    public void setLocation(int dx, int dy, int playerx, int playery){
        // if (dx < 0 && playerx - x < GameMap.WIDTH/2 && x > 0){
        //     x += dx;
        // }
        // if (dx > 0 && playerx - x > GameMap.WIDTH/2  && playerx + GameMap.WIDTH/2 < GameMap.tileX * GameMap.TILESIZE){
        //     x += dx;
        // }
        // if (dy < 0 && playery - y < GameMap.HEIGHT/2 && y > 0){
        //     y += dy;
        // }
        // if (dy > 0 && playery - y > GameMap.HEIGHT/2  && playery + GameMap.HEIGHT/2 < GameMap.tileY * GameMap.TILESIZE){
        //     y += dy;
        // }
        Point mouse = mHandler.getLocation(gp);
        if (mouse != null){
            int distancex = (int)(Math.round(((playerx - x) - mouse.getX()) * 0.5));
            int distancey = (int)(Math.round(((playery - y) - mouse.getY()) * 0.5));
            x = playerx - GameMap.WIDTH/2 - distancex;
            y = playery - GameMap.HEIGHT/2 - distancey;
        }
    }

    /**
     * //TODO
     * @return
     */
    public int getX(){
        return x;
    }

    /**
     * //TODO
     * @return
     */
    public int getY(){
        return y;
    }
}
