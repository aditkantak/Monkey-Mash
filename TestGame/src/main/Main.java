package main;
import java.net.InetAddress;

import javax.swing.JFrame;

/**
 * Main class for the Monkey Mash
 * 
 * @author Iris Tsai, Gabriel Kan, Adit Kantak
 *
 */
public class Main{
    public static JFrame window;

    /**
     * Main.
     * @param args string[]
     * @throws Exception e
     */
    public static void main(String[] args) throws Exception{
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Monkey Mash");
        window.setCursor(null);
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        gamePanel.startGameThread();
    }
}



