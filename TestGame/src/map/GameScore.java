package map;

import java.util.HashMap;
import java.util.Map;
/**
 * Represents the game score in which determines which player wins or loses. 
 * 
 * @author Gabriel Kan
 *
 */
public class GameScore {
    public static final int totalRounds = 3;
    private int currentRound;
    private Map<Integer, Integer> roundsMap;
    private int winner;
    // /*
    //  * roundsMap -> < Round number, round code>
    //  * rounds code:
    //  * 0 = undetermined
    //  * 1 = player 1 win
    //  * 2 = player 2 win
    //  */

    /**
     * Constructor method for game score.
     * Creates a new hash map and places 3 rounds as keys and empty values.
     */
    public GameScore(){
        roundsMap = new HashMap<>(totalRounds);
        currentRound = 1;
        for (int i = 0; i < totalRounds; i ++){
            roundsMap.put(i + 1, 0);
        }
    }

    /**
     * Sets the winner to the player who did not lose all its health in the round.
     * Places the player into the hashmap by the key of rounds to then determine winner.
     * Once the rounds have reached the total number of rounds (three), checks for winning player.
     * @param player winning player 
     */
    public void setRoundWinner(int player){
        roundsMap.put(currentRound, player);
        if (currentRound == totalRounds){
            checkWinner();
        }
    }

    /**
     * Checks which player is the winner by comparing how many rounds won which is checked through the hashmap.
     */
    public void checkWinner(){
        int playerOne = 0;
        int playerTwo = 0;
        for (int i = 0; i < currentRound;i ++){
            if (roundsMap.get(i + 1) == 1){
                playerOne++;
            }
            if (roundsMap.get(i + 1) == 2){
                playerTwo++;
            }
        }
        if (playerOne > playerTwo){
            winner = 1;
        }
        if (playerOne < playerTwo){
            winner = 2;
        }
    }

    /**
     * Returns winner of the round
     * @return winner
     */
    public int getWinner(){
        return winner;
    }

    /**
     * Sets current round to next round when the game ends/after a player loses
     * @param curRound new round 
     */
    public void setCurRound(int curRound){
        currentRound = curRound;
    }

    /**
     * Returns the hash map of rounds
     * @return hash map of rounds
     */
    public Map<Integer, Integer> getHashMap(){
        return roundsMap;
    }
}
