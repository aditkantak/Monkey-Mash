 package main;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import networking.Client;
import map.GameMap;
import usergui.InGameInterface;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 * Represents the game panel which holds all entities and where the action of the game occurs. 
 * 
 * @author Iris Tsai, Gabriel Kan
 * 
 * @author Sources: None
 *
 */
public class GamePanel extends JPanel implements Runnable {

    //GAME CLIENT
    private Client localClient = new Client("172.20.10.3", "adit1");
    
    // GAME THREAD
    private Thread thread;
    private boolean running;
    private long nextStatTime;
    private int fps, ups;
    private int lastFPS;
    private final double updateRate = 1.0d/144.0d;

    // GAME ENTITIES
    private MouseHandler mHandler = new MouseHandler();
    private KeyHandler kHandler = new KeyHandler(this, localClient);
    private InGameInterface gameInterface;    
    private Image backgroundImage;
    private GameMap gameMap;
    private boolean firstTime = true;

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int lobbyState = 1;
    public final int playState = 2;
    public final int optionsState = 3;
    public final int tutorialState = 4;

    // FULL SCREEN
    int screenWidth = GameMap.WIDTH;
    int screenHeight = GameMap.HEIGHT;
    BufferedImage tempScreen;
    Graphics2D g2;
    boolean fullScreen = false;

    // UI
    public UI ui;
    boolean isConnected = false;

    /**
     * Constructor method for GamePanel class.
     * Creates the game map and in-game interface, takes in background image, sets the first game state to its title state, creates a fullscreen window for the game to be played on.
     */
    public GamePanel() {
        setDoubleBuffered(true);
        addMouseListener(mHandler);
        addKeyListener(kHandler);
        setFocusable(true);
        requestFocus();
        // gameMap = new GameMap(kHandler, mHandler, this, localClient);
        // gameInterface = new InGameInterface(kHandler, gameMap);
        try {

            BufferedImage originalImage = ImageIO.read(Main.class.getResource("/lib/background/space2.png"));

            // Calculate the new height to maintain the aspect ratio
            int newHeight = (int) (originalImage.getHeight() * ((double) GameMap.HEIGHT / originalImage.getWidth()));
        
            // Resize the image
            backgroundImage = originalImage.getScaledInstance(GameMap.WIDTH, newHeight, Image.SCALE_SMOOTH);
            // gameMap = new GameMap(kHandler, mHandler, this);
            // gameInterface = new InGameInterface(kHandler, gameMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ui = new UI(this, localClient);
        
        isConnected = false;

        gameState = titleState;

        tempScreen = new BufferedImage(GameMap.WIDTH, GameMap.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();

        fullScreen = true;
        toggleFullScreen();
        
        setPreferredSize(new Dimension(Main.window.getWidth(), Main.window.getHeight()));
    }

    /**
     * Creates a new game map with in game interface that displays icons and health points on the screen.
     */
    public void buildMap(){
        gameMap = new GameMap(kHandler, mHandler, this, localClient);
        gameInterface = new InGameInterface(kHandler, gameMap);
    }

    /**
     * Begins the game thread.
     */
    public void startGameThread() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Runs the game.
     * Updates and repaints the window to the implied frames and displays the frames per second.
     */
    @Override
    public void run() {
        running = true;
        double accumulator = 0;
        long currentTime, lastUpdate = System.currentTimeMillis();
        nextStatTime = System.currentTimeMillis() + 1000;

        while (running){
            currentTime = System.currentTimeMillis();
            double lastRenderTimeInSeconds = (currentTime - lastUpdate) / 1000d;
            accumulator += lastRenderTimeInSeconds;
            lastUpdate = currentTime;

            if (accumulator >= updateRate){
                if (firstTime && gameState == playState){
                    update();
                    repaint();
                }
                else{
                    repaint();
                    update();
                }
                accumulator -= updateRate;
                GameMap.WIDTH = screenWidth;
                GameMap.HEIGHT = screenHeight;
            }
            printStats();
        }
    }


        /**
     * Displays the frames per second on the right corner of 
     * the game panel
     */
    public void printStats() {
        if (System.currentTimeMillis() > nextStatTime){
            System.out.println(String.format("FPS: %d, UPS: %d", fps, ups));
            lastFPS = fps;
            fps = 0;
            ups = 0;
            nextStatTime = System.currentTimeMillis() + 1000;
        }
    }

    /**
     * Updates the game map and in-game interface for each action and frames. 
     */
    public void update() {
        ups++;
        if (gameState == lobbyState){
            isConnected = localClient.isConnected();
        }
        if (gameState == playState) {
            if (firstTime){
                buildMap();
                gameMap.initializeGame();
                firstTime = false;
            }
            gameMap.update();
            gameInterface.update();
        }
    }

    /**
     * Returns frames per second
     * @return int fps
     */
    public int getFPS(){
        return lastFPS;
    }

    /**
     * Creates full screen for the game that fits into dimensions of the user's computer
     */
    public void toggleFullScreen()
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        if (fullScreen){
            gd.setFullScreenWindow(Main.window);
            Main.window.addKeyListener(kHandler);
            Main.window.addMouseListener(mHandler);
            screenWidth = Main.window.getWidth();
            screenHeight = Main.window.getHeight();
            fullScreen = false;
        }
        else{
            gd.setFullScreenWindow(null);
            fullScreen = true;
        }
    }
    
    /**
     * Draws the different screens based on the game state
     */
    public void paintComponent(Graphics g) {
        fps++;
        if (gameState == optionsState || gameState == titleState || gameState == tutorialState) {
            ui.draw((Graphics2D) g);
        } else if (gameState == lobbyState) {
            ui.drawLobby((Graphics2D) g, isConnected);
        } else {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
    
            if (gameInterface != null && gameMap != null) {
                g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
                double tx = gameMap.getPlayer().camera.getX();
                double ty = gameMap.getPlayer().camera.getY();
                g2.translate(-tx, -ty);
                gameMap.draw(g2);
                g2.translate(tx, ty);
                gameInterface.draw(g2, this);
    
                g2.dispose();
            }
        }
    }
}

