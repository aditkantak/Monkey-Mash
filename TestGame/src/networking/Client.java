package networking;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class represents the local client connecting to the server to send and receive packets in-game
 * @Author Adit Kantak
 */
public class Client {
    /**
     * The IP address at which the server is located
     */
    private InetAddress serverAddress;
    /**
     * The {@code DatagramSocket} which which the client communicates with the server in-game via UDP
     */
    private static DatagramSocket ds;
    /**
     * The player's number as received from the server upon connection (eg: Player 1, Player 2)
     */
    private int playerNum;

    /**
     * The socket used to connect to the server via TCP for gameState changes
     */
    private Socket s;

    /**
     * The outputStream used to output onto the data stream to the Serversocket in the server
     */
    PrintWriter outStream;

    /**
     * The inputStream used to input from the data stream from the Serversocket from the server
     */
    BufferedReader inStream;

    /**
     * Whether both clients are connected to the server, as received from the server. Initialized as false upon connection. 
     */
    boolean isConnected = false;

    String name;

    /**
     * Constructs a {@code Client} that connects to the server at {@code ServerAddress}, with the name {@code name}
     * <p>
     * The Client first establishes the serverAddress and the DatagramSocket and then uses the connect method call to connect to the server
     * @param serverAddress The IP address of the server
     * @param name The name with which to connect to the server
     */
    public Client(String serverAddress, String name){
        System.out.println("Connecting...");
        try{
            this.serverAddress = InetAddress.getByName(serverAddress);
            ds = new DatagramSocket(new InetSocketAddress(InetAddress.getLocalHost(), 9997));
            s = new Socket(serverAddress, 9998);
            outStream = new PrintWriter(s.getOutputStream(), true);
            inStream = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.name = name;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Connected");
    }

    public String getName(){
        return name;
    }

    /**
     * Connects this {@code Client} with name {@code name} to the server via TCP in order to ensure that the server knows this {@code Client} is one of its clients, and receives back a number signifying which player number this {@code Client} represents in the server.
     * @param name The name to connect to the server with
     */
    public void connect(String name){
        try{
            String out = InetAddress.getLocalHost().toString();
            out = out.substring(out.indexOf("/")+1);
            outStream.println(name.trim()+"-"+out);

            String in = inStream.readLine().trim();
            playerNum = Integer.parseInt(in);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Receives from server whether both clients are connected or not, if {@code isConnected} is false.
     * @return isConnected field
     */
    public boolean isConnected(){
        try{
            if (!isConnected){
                isConnected = Boolean.parseBoolean(inStream.readLine().trim());
            }
            return isConnected;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends the server a {@code DatagramPacket} that represents the current state of the {@code Player}
     * @param packet {@code Packet} that represents the state of the player
     */
    public void sendPacket(Packet packet){
        try{
            DatagramPacket dp = new DatagramPacket(packet.getValue(), packet.length, serverAddress, 9999);
            ds.send(dp);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Receives a {@code DatagramPacket} from the server and returns its contents as a {@code Packet}.
     * @return the {@code Packet} based on the incoming data
     */
    public Packet receivePacket(){
        try{
            DatagramPacket dp = new DatagramPacket(new byte[Packet.length], Packet.length);
            ds.receive(dp);
            return new Packet(dp.getData());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     * @return the number of the player as represented in the server
     */
    public int getPlayerNum(){
        return playerNum;
    }

}

    