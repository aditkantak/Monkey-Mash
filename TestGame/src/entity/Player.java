package entity;

import main.Camera;
import main.GamePanel;
import main.KeyHandler;
import main.Main;
import main.MouseHandler;
import map.GameMap;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import weapons.*;
import networking.*;
import objects.Wall;
/**
 * Represents the player which has abilities to move, shoot, be shot at, dash, and collide into walls. 
 * 
 * @author Gabriel Kan
 *
 */
public class Player extends Entity {
    private GameMap gm;
    private GamePanel gp;
    KeyHandler keyH;
    MouseHandler mouseH;
    ArrayList<Weapon> weapons = new ArrayList<>();
    protected long lastShotTime;
    protected long SHOOT_COOLDOWN = 10; // milliseconds
    protected String direction = "front";
    protected boolean left = true;
    protected int currGun = 0;
    protected Client c;
    public Camera camera;
    private int dashTickAmount = 50;
    private int dashCurrentTick = 0;
    private long lastDash;
    private long dashCooldown = 5000;
    private Point lastDirection;
    private Point lastMovement = new Point(0, 1);
    private boolean isDashing = false;
    public static final int maxBullets = 20;
    private int currentBullets = maxBullets;
    long reloadStartTime = 0;
    private boolean reloadCooldown = false;
    public static final int reloadTime = 2500;
    private double dashPercentage;
    private int playerNum;
    private int roundState = 1;
    long relss;
    BufferedImage front1, front2, front3, back1, back2, back3, right1, right2, right3, left1, left2, left3;
    /**
     * Constructor method.
     * Takes in player sprite images for the player movement animation.
     * Adds a gun for the player, as well as a camera that allows the user to view a certain area on the game map.
     * Sets default values for player.
     * @param gp game panel
     * @param gm game map
     * @param keyH keyhandler
     * @param mouseH mousehandler
     * @param client client
     * @param playerNum player number
     */
    public Player(GamePanel gp,GameMap gm, KeyHandler keyH, MouseHandler mouseH, Client client, int playerNum){
        this.gp = gp;
        this.gm = gm;
        this.keyH = keyH;
        this.mouseH = mouseH;
        weapons.add(new Gun());
        weapons.add(new Shotgun());
        currGun = 0;
        c = client;
        this.camera = new Camera(mouseH, gp);
        hitbox = new Rectangle(x, y, (int)Math.round(GameMap.TILESIZE * 0.6), (int)Math.round(GameMap.TILESIZE * 0.9));
        setDefaultValues();
        this.playerNum = playerNum;
        try {
            front1 = ImageIO.read(Main.class.getResource("/lib/player/front-3.png"));
            front2 = ImageIO.read(Main.class.getResource("/lib/player/front-2.png"));
            front3 = ImageIO.read(Main.class.getResource("/lib/player/front-1.png"));
            back1 = ImageIO.read(Main.class.getResource("/lib/player/back-1.png"));
            back2 = ImageIO.read(Main.class.getResource("/lib/player/back-2.png"));
            back3 = ImageIO.read(Main.class.getResource("/lib/player/back-3.png"));
            left1 = ImageIO.read(Main.class.getResource("/lib/player/left-3.png"));
            left2 = ImageIO.read(Main.class.getResource("/lib/player/left-2.png"));
            left3 = ImageIO.read(Main.class.getResource("/lib/player/left-1.png"));
            right1 = ImageIO.read(Main.class.getResource("/lib/player/right-2.png"));
            right2 = ImageIO.read(Main.class.getResource("/lib/player/right-1.png"));
            right3 = ImageIO.read(Main.class.getResource("/lib/player/right-3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the player number
     * @return player number
     */
    public int getPlayerNum(){
        return playerNum;
    }

    /**
     * Returns game map
     * @return game map
     */
    public GameMap getMap(){
        return gm;
    }

    /**
     * Returns current bullets
     * @return current number of bullets
     */
    public int getCurrentBullets(){
        return currentBullets;
    }

    /**
     * Returns maximum number of bullets
     * @return maximum bullets
     */
    public int getMaxBullets(){
        return maxBullets;
    }

    /**
     * Returns round state
     * @return round state
     */
    public void setRoundState(int state){
        roundState = state;
    }

    /**
     * Set base values for health, speed, bullets and a default placement for the player during the very beginning of each round.
      */
    public void setDefaultValues() {
        if (playerNum == 1){
            x = GameMap.tileX / 4 * GameMap.TILESIZE;
        }
        else{
            x = 3 * GameMap.tileX / 4 * GameMap.TILESIZE;
        }
        y = GameMap.tileY / 2 * GameMap.TILESIZE;
        health = 200;
        speed = 3;
        currentBullets = maxBullets;
    }

    /**
     * Updates the frames for each animation and movement of the player based on the type of key pressed. 
     * Also includes the dashing power up, checks collisions and maintains the reload cooldown for bullets. 
     */
    public boolean update() {
        long currentTime = System.currentTimeMillis();
        int dy = 0;
        int dx = 0;
        for (Weapon weapon : weapons) {
            weapon.update();
        }
        if (health == 0){
            hitbox.setSize(0, 0);
        }
        else {
            hitbox.setSize((int)Math.round(GameMap.TILESIZE * 0.6), (int)Math.round(GameMap.TILESIZE * 0.9));
        }
        if (health > 0){
            if (keyH.upPressed) {
                direction = "back";
                dy--;
            }
            if (keyH.downPressed) {
                direction = "front";
                dy++;
            }
            if (keyH.leftPressed) {
                direction = "left";
                dx--;
            }
            if (keyH.rightPressed) {
                direction = "right";
                dx++;
            }
            if (dx != 0 || dy != 0){
                lastMovement = new Point(dx, dy);
            }
            dashPercentage = (currentTime - lastDash + 0.0) / dashCooldown;
            if ((currentTime - lastDash >= dashCooldown)){
                if (keyH.shiftPressed){
                    isDashing = true;
                }
            }
            if (isDashing)
            {
                if (dashCurrentTick == 0){
                    lastDirection = lastMovement;
                    dashCurrentTick++;
                }
                else{
                    lastDash = currentTime;
                    if (dashCurrentTick < dashTickAmount){
                        speed = 7;
                        dx = (int)lastDirection.getX();
                        dy = (int)lastDirection.getY();
                        dashCurrentTick++;
                    }
                    else{
                        isDashing = false;
                        speed = 3;
                        dashCurrentTick = 0;
                    }
                }
            }
            Point check = handleCollisions(dx, dy);
            x += check.getX() * speed;
            y += check.getY() * speed;
            camera.setLocation(dx, dy, x, y);
        }
        spriteCounter++;
        if (spriteCounter > 25) {
            if (spriteNum == 2 || spriteNum == 3) {
                spriteNum = 1;
            } else if (spriteNum == 1) {
                if (left) {
                    spriteNum = 2;
                    left = false;
                } else {
                    spriteNum = 3;
                    left = true;
                }
            }
            spriteCounter = 0;
        }
        if (!keyH.downPressed && !keyH.leftPressed && 
            !keyH.rightPressed && !keyH.upPressed) {
            spriteNum = 1;
        }
        currGun = keyH.weaponChoice - 1;
        if (roundState != 0 && mouseH.leftPressed() && 
            (currentTime - lastShotTime >= SHOOT_COOLDOWN && currentBullets > 0 && !reloadCooldown)) {
            switch (currGun) {
                case 0:
                    SHOOT_COOLDOWN = 300;
                    break;
                case 1:
                    SHOOT_COOLDOWN = 100;
                    //mouseH.setLeft(false);
                    break;
            }
            Point targ = mouseH.getLocation(gp);
            weapons.get(currGun).use(targ, this);
            c.sendPacket(new Packet(x, y, true, targ.x + camera.getX(), targ.y + camera.getY(), (byte) (health/25)));
            currentBullets--;
            lastShotTime = currentTime;

        } else {
            c.sendPacket(new Packet(x, y, false, 0, 0, (byte) (health/25)));
        }
        if (currentBullets == 0 || keyH.reloadPressed){
            if (!reloadCooldown){
                reloadStartTime = currentTime;
                reloadCooldown = true;
            }
        }
        if (reloadCooldown && currentTime - reloadStartTime > reloadTime){
            currentBullets = maxBullets;
            reloadStartTime = currentTime;
            reloadCooldown = false;
        }
        hitbox.setLocation(x + 5, y);
        if (health <= 0) {
            return false;
        }
        return true;
    }
    public boolean isReloading(){
        return reloadCooldown;
    }
    /**
     * Returns reload percentage
     * @return reload percentage
     */
    public double getDashPercentage(){
        return dashPercentage;
    }

    /**
     * Returns health of player
     * @return health
     */
    public int getHealth(){
        return health;
    }

    /**
     * Prevents the player from moving any further when collision is detected. (when player hits a wall)
     * @param dx change in x direction 
     * @param dy change in y direction
     * @return new point coordinate
     */
    protected Point handleCollisions(int dx, int dy){
        int x = dx;
        int y = dy;
        if (isColliding(dx * speed, 0)){
            x = 0;
        }
        if (isColliding(0, dy * speed)){
            y = 0;
        }
        return new Point(x, y);
    }

    /**
     * Determines if the player is colliding with a collidable object like a wall.
     * @param x x coordinate
     * @param y y coordinate
     * @return true if player is colliding and false if not
     */
    public boolean isColliding(int x, int y) {
        for (Wall wall : gm.getWalls()) {
            if (checkCollision(x, y, wall)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns array list of weapons
     * @return weapons
     */
    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * Draws the sprite animation for each direction in which the player moves in for front, back, right, and left.
     * If the health is zero, the player is not drawn, implying that it cannot move or perform any action. 
     * @param g2 graphics
     */
    public void draw(Graphics2D g2) {
        for (Weapon weapon : weapons) {
            weapon.draw(g2);
        }
        //g2.draw(hitbox);
        BufferedImage monkey = null;
        switch (direction) {
            case "front":
                if (spriteNum == 1) {
                    monkey = front1;
                }
                if (spriteNum == 2) {
                    monkey = front2;
                }
                if (spriteNum == 3) {
                    monkey = front3;
                }
                break;
            case "back":
                if (spriteNum == 1) {
                    monkey = back1;
                }
                if (spriteNum == 2) {
                    monkey = back2;
                }
                if (spriteNum == 3) {
                    monkey = back3;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    monkey = right1;
                }
                if (spriteNum == 2) {
                    monkey = right2;
                }
                if (spriteNum == 3) {
                    monkey = right3;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    monkey = left1;
                }
                if (spriteNum == 2) {
                    monkey = left2;
                }
                if (spriteNum == 3) {
                    monkey = left3;
                }
                break;
        }
        if (health != 0){
            g2.drawImage(monkey, x - monkey.getWidth() / 2, 
            y - monkey.getHeight() / 2, GameMap.TILESIZE, 
            GameMap.TILESIZE, null);
        }
    }
}
