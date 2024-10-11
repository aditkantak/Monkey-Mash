package map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import entity.Dummy;
import entity.Entity;
import entity.OtherPlayer;
import entity.Player;
import main.GamePanel;
import main.KeyHandler;
import main.MouseHandler;
import main.UI;
import networking.Client;
import objects.Wall;
/**
 * Represents a game map which the player can move around through with walls and boundaries.
 * 
 * @author Gabriel Kan
 *
 */
public class GameMap {

    private KeyHandler kHandler;
    // DIMENSIONS
    public static final int SCALE = 4;
    public static final int TILESIZE = 16 * SCALE;
    public static final int COL = 16;
    public static final int ROW = 9;
    public static int WIDTH = TILESIZE * COL;
    public static int HEIGHT = TILESIZE * ROW;
    public static final int tileX = 48;
    public static final int tileY = 32;
    private Client localClient;
    private int playerNum;
    private Player player;
    private OtherPlayer otherPlayer;
    public ArrayList<Wall> walls = new ArrayList<>();
    public ArrayList<Entity> entities = new ArrayList<Entity>();
    private GameScore gameScore;
    private Round curRound;
    private Dummy dummy;
    private Entity loser;
    private int lastRound;
    private int size;
    /**
     * Constructor method for game map.
     * Creates the boundary walls and adds the player as well as the opponent player.
     * Adds game score that keeps track of the scores between players. 
     * @param keyH key handler
     * @param mHandler mouse handler
     * @param gp game panel 
     * @param localClient client
     */
    public GameMap(KeyHandler keyH, MouseHandler mHandler, GamePanel gp, Client localClient)
    {
        makeBoundaryWalls();
        this.kHandler = keyH;
        this.localClient = localClient;
        playerNum = localClient.getPlayerNum();
        player = new Player(gp, this, kHandler, mHandler, localClient, playerNum);
        if (playerNum == 1){
            otherPlayer = new OtherPlayer(gp, this, localClient, 2);
        }
        else {
            otherPlayer = new OtherPlayer(gp, this, localClient, 1);
        }
        dummy = new Dummy(this, 3 * TILESIZE, 3 * TILESIZE);
        entities.add(player);
        entities.add(dummy);
        entities.add(otherPlayer);
        size = entities.size();
        gameScore = new GameScore();
    }

    /**
     * Create a new round for both the player and the other player
     */
    public void initializeGame(){
        //gameScore = new GameScore();
        curRound = new Round(player, otherPlayer);
    }

    /**
     * Returns entities
     * @return entities
     */
    public ArrayList<Entity> getEntities(){
        return entities;
    }

    /**
     * Returns ArrayList of Walls to be drawn onto screen
     * @return walls
     */
    public ArrayList<Wall> getWalls(){
        return walls;
    }

    /**
     * Creates the boundary walls and island walls within the game map
     */
    public void makeBoundaryWalls()
    {
        // Boundaries
        walls.add(new Wall(-TILESIZE, 0, TILESIZE, TILESIZE * tileY));
        walls.add(new Wall(0, -TILESIZE, TILESIZE * tileX, TILESIZE));
        walls.add(new Wall(0,  TILESIZE * tileY, TILESIZE * tileX, TILESIZE));
        walls.add(new Wall(TILESIZE * tileX,  0, TILESIZE, TILESIZE * tileY));

        // MAP walls
        walls.add(new Wall(10 * TILESIZE, 10 * TILESIZE, 1 * TILESIZE, 4 * TILESIZE));
        walls.add(new Wall((tileX - 10) * TILESIZE, 10 * TILESIZE, 1 * TILESIZE, 4 * TILESIZE));
        walls.add(new Wall(10 * TILESIZE, (tileY - 10) * TILESIZE, 1 * TILESIZE, 4 * TILESIZE));
        walls.add(new Wall((tileX - 10) * TILESIZE, (tileY - 10) * TILESIZE, 1 * TILESIZE, 4 * TILESIZE));
        walls.add(new Wall((tileX/2) * TILESIZE, (tileY/2) * TILESIZE, 2 * TILESIZE, 4 * TILESIZE));
    }

    /**
     * Updates the game map for different rounds and gets the winner for each round.
     */
    public void update(){
        curRound.update();
        for (int i = 0; i < size;i++){
            Entity e = entities.get(i);
            if (!e.update()){
                if (curRound.getRoundState() == 2){
                    gameScore.setRoundWinner(curRound.getWinner());
                }
                gameScore.setCurRound(curRound.getRound());
            }
            
        }
    }


    /**
     * Returns game score
     * @return game score
     */
    public GameScore getGameScore(){
        return gameScore;
    }

    public OtherPlayer getOtherPlayer(){
        return otherPlayer;
    }
    /**
     * Returns player
     * @return player
     */
    public Player getPlayer(){
        return player;
    }
    public Round getRound(){
        return curRound;
    }
    /**
     * Draws the game map for each round as well as the walls.
     * @param g2 graphics
     */
    public void draw(Graphics2D g2){
        g2.setColor(Color.GREEN);
        for (int i = 0; i < tileX;i++){
            for (int j = 0; j < tileY;j++){
                Rectangle rec = new Rectangle(i * TILESIZE, 
                                j * TILESIZE, TILESIZE, TILESIZE);
                g2.draw(rec);
            }
        }
        for (int i = 0;i < entities.size(); i++){
            entities.get(i).draw(g2);
        }
        for (Wall wall:walls){
            wall.draw(g2);
        }
    }
}
