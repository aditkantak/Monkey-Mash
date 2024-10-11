package map;

import java.awt.Graphics2D;

import entity.OtherPlayer;
import entity.Player;
import main.GamePanel;
import main.UI;
/**
 * Represents the rounds that occur during the game and end when a player loses all health.
 * 
 * @author Gabriel Kan
 *
 */
public class Round {
    private int roundState;
    /**
     *  0 - start
     *  1 - middle 
     *  2 - end (someone dies)
     */
    private Player player;
    private OtherPlayer otherPlayer;
    private long startTime;
    private int winner;
    private long timeInRound;
    private long endStartTime;
    private boolean endStart = true;
    private int currentRound;

    /**
     * Constructor method for round. 
     * @param player player 
     * @param otherPlayer opponent player
     */
    public Round(Player player, OtherPlayer otherPlayer){
        roundState = 0;
        startTime = System.currentTimeMillis();
        this.player = player;
        this.otherPlayer = otherPlayer;
        player.setDefaultValues();
        otherPlayer.setDefaultValues();
        winner = 0;
        this.currentRound = 1;
    }

    /**
     * Returns round state.
     * @return round state 
     */
    public int getRoundState(){
        return roundState;
    }

    /**
     * Returns winner of round.
     * @return winner
     */
    public int getWinner(){
        return winner;
    }

    /**
     * Updates the game based on round state. 
     * When there is a new round, player is set to default values to begin with. 
     * When a player has no health, the player becomes the winner and the round state changes, meaning the round ends. 
     */
    public void update(){
        long currTime = System.currentTimeMillis();
        timeInRound = currTime - startTime;
        player.setRoundState(roundState);
        otherPlayer.setRoundState(roundState);
        if (roundState == 0)
        {
            player.setDefaultValues();
            otherPlayer.setDefaultValues();
            if (timeInRound > 3000 && 
                currentRound < GameScore.totalRounds + 1){
                roundState = 1;
            }
        }
        if (roundState == 1)
        {
            if (player.getHealth() == 0){
                winner = otherPlayer.getPlayerNum();
                roundState = 2;
            }
            if (otherPlayer.getHealth() == 0){
                winner = player.getPlayerNum();
                roundState = 2;
            }
        }
        if (roundState == 2){
            if (endStart)
            {
                endStartTime = System.currentTimeMillis();
                endStart = false;
            }
            else if ((System.currentTimeMillis() - endStartTime) 
                / 1000 >= 5){
                roundState = 0;
                player.setDefaultValues();
                otherPlayer.setDefaultValues();
                currentRound++;
                startTime = System.currentTimeMillis();
                endStart = true;
            }
        }
    }

    /**
     * Displays the text at the beginning and end of the round. 
     * @param g2 graphics
     */
    public void draw(Graphics2D g2){
        if (roundState == 0)
        {
            if (currentRound < GameScore.totalRounds + 1){
                int i = (int)(2 - timeInRound / 1000);
                g2.setFont(UI.getFont().deriveFont(50F));
    
                g2.drawString("" + (i + 1), GameMap.WIDTH / 2, 
                                GameMap.HEIGHT / 2);
            }
        }
        if (roundState == 1)
        {
            if ((int)(timeInRound / 1000) == 3){
                g2.drawString("GO!", GameMap.WIDTH / 2, 
                                GameMap.HEIGHT / 2);  
            }
        }
        if (roundState == 2)
        {
            g2.drawString("ROUND " + currentRound + " END",
                            GameMap.WIDTH / 2, GameMap.HEIGHT / 2);  
        }
    }

    /**
     * Returns current round number.
     * @return current round
     */
    public int getRound(){
        return currentRound;
    }
}
