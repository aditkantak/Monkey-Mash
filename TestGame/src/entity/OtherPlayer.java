package entity;

import main.GamePanel;
import main.Main;
import map.GameMap;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.imageio.ImageIO;
import main.KeyHandler;

import weapons.*;
import networking.*;

/**
 * 
 * This class represents all opponent(s) being constructed on the client side by the input stream from the server
 * 
 * @author Adit Kantak
 */
public class OtherPlayer extends Player{
    /**
     * The last x and y coordinates (used for animation purposes)
     */
    protected int lastX, lastY;
    /**
     * The packet being used to update this OtherPlayer every frame
     */
    protected Packet p, lastP;
    private int totalHearts;
    private BufferedImage heart;
    /**
     * Constructs an {@code OtherPlayer} on {@code gm}, on {@code gp}, using the local {@code Client}, and player number {@code playerNum}
     * @param gp the GamePanel for this game
     * @param gm the GameMap for this game
     * @param client the local client instance for this machine
     * @param playerNum the opposite of the number the local player has
     */
    public OtherPlayer(GamePanel gp, GameMap gm, Client client, int playerNum){
        super(gp, gm, null, null, client, playerNum);
        lastX = x;
        lastY = y;
        totalHearts = 200;
        hitbox = new Rectangle(this.x, this.y, 
                                (int)Math.round(GameMap.TILESIZE * 0.6), 
                                (int)Math.round(GameMap.TILESIZE * 0.9));
        try{
            front1 = ImageIO.read(Main.class.getResource("/lib/opponent/front2.png"));
            front2 = ImageIO.read(Main.class.getResource("/lib/opponent/front3.png"));
            front3 = ImageIO.read(Main.class.getResource("/lib/opponent/front1.png"));
            back1 = ImageIO.read(Main.class.getResource("/lib/opponent/back1.png"));
            back2 = ImageIO.read(Main.class.getResource("/lib/opponent/back2.png"));
            back3 = ImageIO.read(Main.class.getResource("/lib/opponent/back3.png"));
            left1 = ImageIO.read(Main.class.getResource("/lib/opponent/left1.png"));
            left2 = ImageIO.read(Main.class.getResource("/lib/opponent/left2.png"));
            left3 = ImageIO.read(Main.class.getResource("/lib/opponent/left3.png"));
            right1 = ImageIO.read(Main.class.getResource("/lib/opponent/right1.png"));
            right2 = ImageIO.read(Main.class.getResource("/lib/opponent/right2.png"));
            right3 = ImageIO.read(Main.class.getResource("/lib/opponent/right3.png"));
            heart = ImageIO.read(Main.class.getResource("/lib/gui/heart.png"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void receivePacket(){
        p = c.receivePacket();
    }
    
    /**
     * Updates the {@code OtherPlayer} with a received packet by doing the following:
     * <p> - updating the current weapon in hand
     * <p> - updating the current and last x and y coordinates
     * <p> - setting the direction of the player and handling sprites (for animation purposes)
     * <p> - shooting based on the packet input
     * <p> - checking the health
     * <p> - redrawing the hitbox
     */
    @Override
    public boolean update(){
        lastP = p;
        ExecutorService ex = Executors.newFixedThreadPool(1);
        Runnable runnable = () -> receivePacket();
        try{
            Future<?> future = ex.submit(runnable);
            future.get(7, TimeUnit.MILLISECONDS);
        }
        catch (TimeoutException to){
            p = lastP;
            System.out.println("Packet lost :(");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        int dy = 0;
        int dx = 0;
        if (health == 0){
            hitbox.setSize(0, 0);
        }
        else {
            hitbox.setSize((int)Math.round(GameMap.TILESIZE * 0.6), 
                            (int)Math.round(GameMap.TILESIZE * 0.9));
        }
        for (Weapon weapon: weapons){
            weapon.update();
        }
        if (health > 0){
            x = p.getX();
            y = p.getY();
            if (y<lastY){
                direction = "back";
                dy--;
            }
            if (y>lastY){
                direction = "front";
                dy++;
            }
            if (x<lastX){
                direction = "left";
                dx--;
            }
            if (x>lastX){
                direction = "right";
                dx++;
            }
            Point check = handleCollisions(dx, dy);
            lastX = x;
            lastY = y;
            x += check.getX() * speed;
            y += check.getY() * speed;
        }
        spriteCounter++;
        if (spriteCounter > 25){
            if (spriteNum == 2 || spriteNum == 3){
                spriteNum = 1;
            }
            else if (spriteNum == 1){
                if (left){
                    spriteNum = 2;
                    left = false;
                }
                else{
                    spriteNum = 3;
                    left = true;
                }
            }
            spriteCounter = 0;
        }
        if (x == lastX && y == lastY){
            spriteNum = 1;
        }

        health = p.getHealth() * 25;

        long currentTime = System.currentTimeMillis();
        if (p.isShooting()){
            SHOOT_COOLDOWN = 300;
            if (currentTime - lastShotTime >= SHOOT_COOLDOWN){
                weapons.get(0).use(
                    new Point(p.getTargX(), p.getTargY()), this);
                lastShotTime = currentTime;
            }
        }
        hitbox.setLocation(x + 5, y);
        if (health <= 0){
            return false;
        }
        return true;

    }

    public void draw(Graphics2D g2){
        super.draw(g2);
        int hearts = health/25;
        if (hearts != 0){
            int startX = x - (hearts * heart.getWidth() * 2) / 2 + GameMap.TILESIZE/2; // Calculate the starting x-coordinate
            for (int i = 0; i < hearts; i++)
            {
                int heartX = startX + i * heart.getWidth() * 2; // Calculate the x-coordinate for each heart
                g2.drawImage(heart, heartX, y - 30, heart.getWidth() * 2, heart.getHeight() * 2, null);
            }
        }
    }
}
