package usergui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import entity.Player;
import main.GamePanel;
import main.KeyHandler;
import main.Main;
import main.UI;
import map.GameMap;
import map.GameScore;
/**
 * In-game graphics interface that displays the player's health and abilities on the screen as the game is played.
 * 
 * @author Iris Tsai, Gabriel Kan
 *
 */
public class InGameInterface {
    public final int abilitySquareSize = 100;
    private BufferedImage abilityFrame, heart, deadheart, fire, redwin, bluewin, draw;
    private int angleThreshold;
    private Player player;
    private int maxBullets;
    private GameScore gameScore;
    private GameMap gm;
    private final int totalHearts;

    /**
     * Constructor method.
     * Reads image files.
     * @param keyH key handler
     * @param gm game map
     */
    public InGameInterface(KeyHandler keyH, GameMap gm) {
        this.player = gm.getPlayer();
        maxBullets = player.getMaxBullets();
        totalHearts = player.getHealth()/25;
        gameScore = gm.getGameScore();
        this.gm = gm;
        try {
            abilityFrame = ImageIO.read(Main.class.getResource("/lib/gui/swiftness.png"));
            heart = ImageIO.read(Main.class.getResource("/lib/gui/heart.png"));
            deadheart = ImageIO.read(Main.class.getResource("/lib/gui/deadheart.png"));
            fire = ImageIO.read(Main.class.getResource("/lib/gui/plasmaicon.png"));
            draw = ImageIO.read(Main.class.getResource("/lib/gui/normal.png"));
            redwin = ImageIO.read(Main.class.getResource("/lib/gui/red-win.png"));
            bluewin = ImageIO.read(Main.class.getResource("/lib/gui/blue-win.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the in-game interface.
     * Player's health is displayed with either red or gray hearts depending
     * on how much the total health has been depleted when the player is shot 
     * at. The ability frame for sprinting has a time out right after being 
     * used which draws a loading animation on its icon.
     * @param g2 graphics
     * @param gp game panel
     */
    public void draw(Graphics2D g2, GamePanel gp) {
        g2.setColor(Color.WHITE);
        g2.setFont(UI.getFont().deriveFont(50F));
        gm.getRound().draw(g2);
        if (GameScore.totalRounds < gm.getRound().getRound()){
            g2.drawString("Player " + gameScore.getWinner() + " wins!", GameMap.WIDTH/2, GameMap.HEIGHT/2);
        }
        int x = abilityFrame.getWidth() * GameMap.SCALE;
        int y =  GameMap.HEIGHT - abilitySquareSize;
        int xscale = abilityFrame.getWidth() * GameMap.SCALE;
        int yscale = abilityFrame.getHeight() * GameMap.SCALE;
        BufferedImage darkened;
        darkened = darkenImage(abilityFrame, angleThreshold );
        g2.drawImage(darkened,x + fire.getWidth() * GameMap.SCALE + 10, y, xscale ,yscale, null);
        g2.drawImage(fire, x, y, xscale, yscale, null);
        g2.setColor(Color.WHITE);
        g2.setFont(UI.getFont().deriveFont(15F));
        g2.drawString("L-CLICK", x + fire.getWidth()/2- 3, y + fire.getHeight() - 12);
        g2.drawString("SHIFT", x + fire.getWidth() * GameMap.SCALE + 15 + darkened.getWidth()/2,y + darkened.getHeight() - 12);
        g2.setFont(UI.getFont().deriveFont(30F));
        g2.drawString("FPS: " + gp.getFPS(), gp.getWidth() - 200, 100);
        g2.setFont(UI.getFont().deriveFont(40F));
        g2.drawString(player.getCurrentBullets() + " / " + maxBullets, gp.getWidth() - GameMap.TILESIZE * 3, y + fire.getHeight() * 3);
        if (player.isReloading()){
            g2.setFont(UI.getFont().deriveFont(20F));
            g2.drawString("RELOADING...", gp.getWidth() - GameMap.TILESIZE * 5 - 20, y + fire.getHeight() * 3);
        }
        else {
            g2.setFont(UI.getFont().deriveFont(20F));
            g2.drawString("R TO RELOAD", gp.getWidth() - GameMap.TILESIZE * 5 - 40, y + fire.getHeight() * 3); 
        }
        int hearts = player.getHealth()/25;
        for (int i = 1; i <= totalHearts; i++)
        {
            x = heart.getWidth() * GameMap.SCALE;
            y = GameMap.TILESIZE - 16;
            xscale = heart.getWidth() * GameMap.SCALE;
            yscale = heart.getHeight() * GameMap.SCALE;
            g2.drawImage(heart, x * i, y, xscale, yscale, null);
        }
        for (int i = 1; i <= totalHearts-hearts; i++)
        {
            x = deadheart.getWidth() * GameMap.SCALE;
            y = GameMap.TILESIZE - 16;
            xscale = deadheart.getWidth() * GameMap.SCALE;
            yscale = deadheart.getHeight() * GameMap.SCALE;
            g2.drawImage(deadheart, x * i, y, xscale, yscale, null);
        }
        Map<Integer,Integer> gameTally = gameScore.getHashMap();
        for (int i = 1; i <= GameScore.totalRounds; i++){
            if (gm.getPlayer().getPlayerNum() == gameTally.get(i)){
                g2.drawImage(bluewin, GameMap.WIDTH/2 - (GameScore.totalRounds - i) * GameMap.TILESIZE, y, xscale, yscale, null);
            }
            else if (gm.getOtherPlayer().getPlayerNum() == gameTally.get(i)){
                g2.drawImage(redwin, GameMap.WIDTH/2 - (GameScore.totalRounds - i) * GameMap.TILESIZE, y, xscale, yscale, null);
            }
            else{
                g2.drawImage(draw, GameMap.WIDTH/2 - (GameScore.totalRounds - i) * GameMap.TILESIZE, y, xscale, yscale, null);
            }
        }
    }

    /**
     * Helper method that updates the threshold
     */
    private void updateThreshold() {
        double reloadPercentage = player.getDashPercentage();
        if (reloadPercentage >= 1){
            angleThreshold = 360;
        }
        else{
            angleThreshold = (int)(reloadPercentage * 360);
        }
    }

    /**
     * Darkens image icon to create loading animation for the ability
     * @param image buffered image
     * @param angleThreshold angle
     * @return darkened image
     */
    private static BufferedImage darkenImage(BufferedImage image, int angleThreshold) {
        int width = image.getWidth();
        int height = image.getHeight();
    
        int centerX = width/2;
        int centerY = height/2;
        BufferedImage darkenedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double angle = Math.toDegrees(Math.atan2(y - centerX, x - centerY));
                angle = (angle + 450) % 360;
                if (angle > angleThreshold) {
                    Color color = new Color(image.getRGB(x, y));
    
                    int darkness = 150;
    
                    int red = Math.max(color.getRed() - darkness, 0);
                    int green = Math.max(color.getGreen() - darkness, 0);
                    int blue = Math.max(color.getBlue() - darkness, 0);
    
                    Color darkenedColor = new Color(red, green, blue);
                    darkenedImage.setRGB(x, y, darkenedColor.getRGB());
                } else {
                    darkenedImage.setRGB(x, y, image.getRGB(x, y));
                }
            }
        }
    
        return darkenedImage;
    }

    /**
     * Updates the threshold
     */
    public void update() {
        updateThreshold();
    }
}
