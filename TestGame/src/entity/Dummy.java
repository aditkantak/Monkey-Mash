package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Main;
import map.GameMap;
/**
 * Represents a dummy with can be shot at. 
 * 
 * @author Gabriel Kan
 *
 */
public class Dummy extends Entity{
    BufferedImage dummy;
    GameMap gm;

    // Duration of the shot effect in milliseconds
    private final int shotEffectDuration = 300; 
    
    // Timer to track the elapsed time of the shot effect
    private long shotEffectTimer = 0;
     
    private BufferedImage shotDummy;
    private boolean firstShot = false;

    /**
     * Constructor method.
     * @param gm game map
     * @param x x coordinate
     * @param y y coordinate
     */
    public Dummy(GameMap gm, int x, int y){
        this.gm = gm;
        this.x = x;
        this.y = y;
        health = 75;
        hitbox = new Rectangle(x, y, (int)Math.round(GameMap.TILESIZE), (int)Math.round(GameMap.TILESIZE));
        try {
            dummy = ImageIO.read(Main.class.getResource("/lib/entities/dummy.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Sets health to certain amount. 
     * @param healthSet health
     */
    public void setHealth(int healthSet){
        health = healthSet;
    }

    /**
     * Returns health of dummy.
     * @return health
     */
    public int getHealth(){
        return health;
    }

    /**
     * When dummy is shot at, it loses health as would a player. 
     * An animation for the dummy is called when shot at. 
     */
    public void shotAt() {
        super.shotAt();
        shotDummy = applyShotEffect(dummy);
    }

    /**
     * Filters the image coloring to red when the dummy is shot at. 
     * @param originalImage original sprite image of dummy
     * @return modified image
     */
    private BufferedImage applyShotEffect(BufferedImage originalImage) {
        // Create a new BufferedImage with the same dimensions and type as the original image
        BufferedImage modifiedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
    
        // Apply the color filter by iterating through each pixel of the image
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                // Get the color of the pixel in the original image
                int rgb = originalImage.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF; // Extract the alpha component (transparency)
                int red = (rgb >> 16) & 0xFF; // Extract the red component
                int green = (rgb >> 8) & 0xFF; // Extract the green component
                int blue = rgb & 0xFF; // Extract the blue component
    
                // Apply the color filter here (e.g., make the image slightly redder)
                int modifiedRed = Math.min(red + 200, 255); // Increase the red component by 20 (adjust the value as desired)
                int modifiedGreen = green;
                int modifiedBlue = blue;
    
                // Combine the modified color components and alpha back into an RGB value
                int modifiedRGB = (alpha << 24) | (modifiedRed << 16) | (modifiedGreen << 8) | modifiedBlue;
    
                // Set the modified pixel color in the new image
                modifiedImage.setRGB(x, y, modifiedRGB);
            }
        }
    
        return modifiedImage;
    }
    
    /**
     * Updates for each frame and returns if health is greater than zero.
     * @return true if health is greater than zero, false if not
     */
    public boolean update() {
        
        if (isShot && !firstShot) {
            shotEffectTimer = System.currentTimeMillis();
            firstShot = true;
        }
        
        if (isShot && System.currentTimeMillis() - 
            shotEffectTimer >= shotEffectDuration) {
            isShot = false;
            firstShot = false;
        }
        return health > 0;
    }
    
    /**
     * Draws the images of the dummy depending on if it is shot or not. 
     * @param g graphics 
     */
    public void draw(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.RED);
        g2d.draw(hitbox);
    
        if (isShot) {
            g2d.drawImage(shotDummy, x - dummy.getWidth(), y - dummy.getHeight(), GameMap.TILESIZE * 2, GameMap.TILESIZE * 2, null);
        } 
        else {
            g2d.drawImage(dummy, x - dummy.getWidth(), y - dummy.getHeight(), GameMap.TILESIZE * 2, GameMap.TILESIZE * 2, null);
        }
        
        g2d.dispose();
    }
}
