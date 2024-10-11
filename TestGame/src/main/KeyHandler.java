package main;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import networking.Client;
/**
 * Represents a key handler that takes keyboard input from the user and performs actions as per game state.
 * 
 * @author Iris Tsai
 *
 */
public class KeyHandler implements KeyListener{

    public boolean upPressed, rightPressed, leftPressed, downPressed, enterPressed, escapePressed, shiftPressed, reloadPressed;
    public int weaponChoice = 1;
    GamePanel gp;
    private Client localClient;
    
    /**
     * Constructor method
     * @param gp game panel
     */
    public KeyHandler(GamePanel gp, Client localClient)
    {
        this.gp = gp;
        this.localClient = localClient;
    }

    /**
     * Determines actions to be performed in game depending on
     * the game state. Creates a cursor that lets 
     * player select options.
     * @param arg0 key pressed by user
     */
    @Override
    public void keyPressed(KeyEvent arg0) 
    {
        int code = arg0.getKeyCode();

        // TITLE STATE
        if (gp.gameState == gp.titleState)
        {
            if (code == KeyEvent.VK_W) 
            {
                gp.ui.cursorNum--;
                if (gp.ui.cursorNum < 0)
                {
                    gp.ui.cursorNum = 2;
                }
            }

            if (code == KeyEvent.VK_S) 
            {
                gp.ui.cursorNum++;
                if (gp.ui.cursorNum > 2)
                {
                    gp.ui.cursorNum = 0;
                }
            }

            if (code == KeyEvent.VK_ENTER)
            {
                if (gp.ui.cursorNum == 0)
                {
                    gp.ui.cursorNum = 3;
                    gp.gameState = gp.lobbyState;
                    localClient.connect(localClient.getName());
                }
                if (gp.ui.cursorNum == 1)
                {
                    gp.gameState = gp.tutorialState;
                }
                if (gp.ui.cursorNum == 2)
                {
                    System.exit(0);
                }
            }
        }

        // LOBBY STATE        
        if (gp.gameState == gp.lobbyState)
        {
            if (code == KeyEvent.VK_ENTER)
            {
                if (gp.ui.cursorNum == 0)
                {
                    gp.gameState = gp.titleState;
                }
            }
        }

        // OPTIONS STATE
        if (gp.gameState == gp.optionsState)
        {
            if (code == KeyEvent.VK_W) 
            {
                gp.ui.cursorNum--;
                if (gp.ui.cursorNum < 0)
                {
                    gp.ui.cursorNum = 1;
                }
            }

            if (code == KeyEvent.VK_S) 
            {
                gp.ui.cursorNum++;
                if (gp.ui.cursorNum > 1)
                {
                    gp.ui.cursorNum = 0;
                }
            }

            if (code == KeyEvent.VK_ENTER)
            {
                if (gp.ui.cursorNum == 0)
                {
                    System.exit(0);
                }
                // if (gp.ui.cursorNum == 1)
                // {
                //     System.exit(0);
                // }
            }
        }       

        // TUTORIAL STATE        
        if (gp.gameState == gp.tutorialState)
        {
            if (code == KeyEvent.VK_ENTER)
            {
                if (gp.ui.cursorNum == 0)
                {
                    gp.gameState = gp.titleState;
                }
            }
        }

        // GENERAL
        if (code == KeyEvent.VK_SHIFT){
            shiftPressed = true;
        }
        if (code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if (code == KeyEvent.VK_W){
            upPressed = true;
        }
        if (code == KeyEvent.VK_S){
            downPressed = true;
        }
        if (code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if (code == KeyEvent.VK_R){
            reloadPressed = true;
        }
        //
        if (code == KeyEvent.VK_1){
            weaponChoice = 1;
        }
        if (code == KeyEvent.VK_2){
            weaponChoice = 2;
        }
        if (code == KeyEvent.VK_ESCAPE)
        {
            if (gp.gameState == gp.playState)
            {
                gp.gameState = gp.optionsState;
            }
            else if (gp.gameState == gp.optionsState)
            {
                gp.gameState = gp.playState;
            } 
        }
    }

    /**
     * Reads the keys that can be held in place to perform an action 
     * and sets their state of being pressed to false when user 
     * releases the key
     * @param arg0 key released by user
     */
    @Override
    public void keyReleased(KeyEvent arg0) {
        int code = arg0.getKeyCode();
        if (code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if (code == KeyEvent.VK_W){
            upPressed = false;
        }
        if (code == KeyEvent.VK_S){
            downPressed = false;
        }
        if (code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if (code == KeyEvent.VK_O){
            gp.toggleFullScreen();
        }
        if (code == KeyEvent.VK_SHIFT){
            shiftPressed = false;
        }
        if (code == KeyEvent.VK_R){
            reloadPressed = false;
        }
    }

    /**
     * @param arg0 key 
     */
    @Override
    public void keyTyped(KeyEvent arg0) {
    }
    
}
