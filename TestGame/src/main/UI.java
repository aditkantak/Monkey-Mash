package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.Player;
import networking.Client;
import map.GameMap;
/**
 * 
 * Creates the graphics user interface for the different game states: title state, options state, and tutorial state.
 * 
 * @author Iris Tsai
 *
 */
public class UI {
    private Client localClient;
    GamePanel gp;
    Graphics2D g2;
    public int cursorNum = 0;
    BufferedImage title, mouse, wasd, heart, deadheart, dash, gametut;
    File fontFile;
    static Font customFont;

    /**
     * Constructor method for user interface.
     * @param gp game panel
     */
    public UI(GamePanel gp, Client localClient)
    {
        this.gp = gp;
        this.localClient = localClient;
        try 
        {
            mouse = ImageIO.read(Main.class.getResource("/lib/ui/mouse.png"));
            title = ImageIO.read(Main.class.getResource("/lib/ui/title.png"));
            wasd = ImageIO.read(Main.class.getResource("/lib/ui/wasd.png"));
            heart = ImageIO.read(Main.class.getResource("/lib/ui/heartui.png"));
            deadheart = ImageIO.read(Main.class.getResource("/lib/ui/deadheart.png"));
            dash = ImageIO.read(Main.class.getResource("/lib/ui/dashmonkey.png"));
            gametut = ImageIO.read(Main.class.getResource("/lib/ui/gametut.png"));
            InputStream inputStream = Main.class.getResourceAsStream("/lib/font/Minecraft.ttf");

            // Create a font from the input stream
            customFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);

            // Optionally, derive a new font with desired style and size
        } 
        catch (IOException | FontFormatException e) 
        {
            e.printStackTrace();
        }
    }

    /**
     * Returns font file.
     * @return custom font
     */
    public static Font getFont()
    {
        return customFont;
    }

    /**
     * Draws the game state screens depending on game state.
     * @param g2 graphics
     */
    public void draw(Graphics2D g2)
    {
        this.g2 = g2;

        // TITLE STATE
        if (gp.gameState == gp.titleState)
        {
            makeTitleScreen();
        }

        // OPTIONS STATE
        if (gp.gameState == gp.optionsState)
        {
            makeOptionsScreen();
        }

        // TUTORIAL STATE
        if (gp.gameState == gp.tutorialState)
        {
            makeTutorialScreen();
        }

    }

    /**
     * Draws the lobby screen as the player waits for the other player to connect to the game.
     * @param g2 graphics
     * @param isConnected boolean that states if the other user
     *                    is connected to game
     */
    public void drawLobby(Graphics2D g2, boolean isConnected)
    {
        this.g2 = g2;
        // LOBBY STATE
        if (gp.gameState == gp.lobbyState)
        {
            makeLobbyScreen(isConnected);
        }
    }

    /**
     * Helper method for aligning text to the center of the screen.
     * @param text String text
     * @return x coordinate
     */
    private int getCenterX(String text)
    {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = GameMap.WIDTH / 2 - length / 2;
        return x;
    }

    /**
     * Draws the lobby screen in which the player waits for another player to connect to.
     */
    public void makeLobbyScreen(boolean isConnected)
    {
        cursorNum = 0;

        // BACKGROUND COLOR
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, GameMap.WIDTH, GameMap.HEIGHT);
        
        g2.setColor(Color.WHITE);
        g2.setFont(customFont);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,64F));
        
        
        // CONNECTED STATUS=
        String text = "Waiting for player...";
        int x = getCenterX(text);
        g2.drawString(text, x, GameMap.TILESIZE * 5);

        if (isConnected)
        {
            gp.gameState = gp.playState;
        }
    }

    /**
     * Draws the title screen which will have options to start game, game tutorial, and quit game.
     */
    public void makeTitleScreen()
    {
        // BACKGROUND COLOR
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, GameMap.WIDTH, GameMap.HEIGHT);

        // TITLE & FONT
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,64F));
        String text = "xxxxxxxxxxxxx";
        int x = getCenterX(text);
        g2.setFont(customFont);
        g2.setColor(Color.WHITE);
        g2.drawImage(title, x, GameMap.TILESIZE * 2,null);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,32F));

        // START GAME OPTION
        text = "Start Game";
        x = getCenterX(text);
        g2.drawString(text, x, GameMap.TILESIZE * 8);
        if (cursorNum == 0)
        {
            g2.drawString(">", x - 25, GameMap.TILESIZE * 8);
        }

        // TUTORIAL OPTION
        text = "Game Tutorial";
        x = getCenterX(text);
        g2.drawString(text, x, GameMap.TILESIZE * 9);
        if (cursorNum == 1)
        {
            g2.drawString(">", x - 25, GameMap.TILESIZE * 9);
        }

        // QUIT GAME OPTION
        text = "Quit Game";
        x = getCenterX(text);
        g2.drawString(text, x, GameMap.TILESIZE * 10);
        if (cursorNum == 2)
        {
            g2.drawString(">", x - 25, GameMap.TILESIZE * 10);
        }

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,16F));
        text = "By Gabriel Kan, Adit Kantak, and Iris Tsai";
        x = getCenterX(text);
        g2.drawString(text, x, GameMap.TILESIZE * 12);
    }

    /**
     * Draws an option screen that gives the user an option to quit in the middle of the game and return back to menu.
     */
    public void makeOptionsScreen()
    {
        // TITLE
        cursorNum = 0;
        // g2.setColor(Color.BLACK);
        // g2.fillRect(GameMap.TILESIZE * 12 - 16, GameMap.TILESIZE, GameMap.TILESIZE * 6, GameMap.TILESIZE * 6);
        g2.setFont(customFont);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,40F));
        String text = "Would you like to quit game?";
        int x = getCenterX(text);
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, GameMap.TILESIZE * 2);

        // END GAME OPTION
        text = "Yes";
        x = getCenterX(text);
        g2.drawString(text, x, GameMap.TILESIZE * 4);
        if (cursorNum == 0)
        {
            g2.drawString(">", x - 25, GameMap.TILESIZE * 4);
        }

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,16F));
        text = "( Press ESCAPE to return to game )";
        x = getCenterX(text);
        g2.drawString(text, x, GameMap.TILESIZE * 6);
    }

    /**
     * Draws a tutorial screen with inforgraphic of how to play the game.
     */
    public void makeTutorialScreen()
    {
        cursorNum = 0;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, GameMap.WIDTH, GameMap.HEIGHT);
        g2.setFont(customFont);

        //for the laptop if the stuff dont work
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,120F));
        String text = "xxxxxxxxxxxx";
        int x = getCenterX(text);
        g2.drawImage(gametut, x, 0, null);

        // TITLE
        // g2.setFont(g2.getFont().deriveFont(Font.PLAIN,64F));
        // g2.setColor(Color.WHITE);
        // String text = "How Monkey Mash Works";
        // int x = getCenterX(text);
        // g2.drawString(text, x, GameMap.TILESIZE * 2);

        // g2.setFont(g2.getFont().deriveFont(Font.PLAIN,32F));

        // // LIVES
        // text = "don't get shot!";
        // x = getCenterX(text); //
        // g2.drawString(text, x, GameMap.TILESIZE * 4);
        // text = "your health decreases with every energy ball you are hit by";
        // x = getCenterX(text);
        // g2.drawString(text, x, GameMap.TILESIZE * 6);
        // g2.drawImage(heart, GameMap.TILESIZE * 11, GameMap.TILESIZE * 4, null);
        // g2.drawImage(heart, GameMap.TILESIZE * 13, GameMap.TILESIZE * 4, null);
        // g2.drawImage(heart, GameMap.TILESIZE * 15, GameMap.TILESIZE * 4, null);
        // g2.drawImage(deadheart, GameMap.TILESIZE * 17, GameMap.TILESIZE * 4, null);

        // // WASD
        // text = "to move your monkey, use WASD";
        // g2.drawString(text, GameMap.TILESIZE * 4, GameMap.TILESIZE * 13);
        // g2.drawImage(wasd, GameMap.TILESIZE * 4, GameMap.TILESIZE * 7,null);  
        
        // // SHIFT
        // text = "press shift to dash away";
        // x = getCenterX(text);
        // g2.drawString(text, x, GameMap.TILESIZE * 12);
        // g2.drawImage(dash, GameMap.TILESIZE * 14, GameMap.TILESIZE * 8,null); 

        // // MOUSE
        // text = "and left-click to blast your";
        // g2.drawString(text, GameMap.TILESIZE * 19, GameMap.TILESIZE * 13);
        // text = "enemy monkey!";
        // g2.drawString(text, GameMap.TILESIZE * 21, GameMap.TILESIZE * 14);
        // g2.drawImage(mouse, GameMap.TILESIZE * 20, GameMap.TILESIZE * 9,null);   
        
        // // BACK OPTION
        // g2.setFont(g2.getFont().deriveFont(Font.PLAIN,32F));
        // text = "> Back to menu";
        // x = getCenterX(text);
        // g2.drawString(text, x, GameMap.TILESIZE * 15);
    }
}
