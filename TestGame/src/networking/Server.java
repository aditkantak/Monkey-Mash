package networking;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the server that the clients connect to in order to communicate to facilitate the game. 
 */
public class Server{
    /**
     * The socket used by the server to accept and transmit game packets via UDP
     */
    private static DatagramSocket ds;
    /**
     * The local IP address of the server
     */
    private InetAddress ia;
    /**
     * The IP addresses of the clients connected to the server, keyed by their name
     */
    private HashMap<String, InetAddress> clients = new HashMap<String, InetAddress>();
    /**
     * The number of clients connected to the server
     */
    private int numClients;
    /**
     * The SeverSocket used to facilitate the client TCP connection for gamestate changes
     */
    private ServerSocket ss;
    /**
     * The list of sockets of connected players
     */
    private ArrayList<Socket> socketList = new ArrayList<Socket>();

    /**
     * Constructs a {@code Server} bound to its local IP address that binds the {@client DatagramSocket} and {@code ServerSocket} to listen on port 9999 and 9998, respectively.
     */
    public Server(){
        System.out.println("server constructor");
        long currTime = System.currentTimeMillis();
        try{
        ds = new DatagramSocket(9999);
        ia = InetAddress.getLocalHost();
        ss = new ServerSocket(9998);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        long finTime = System.currentTimeMillis();
        System.out.println("ia: "+ia);
        System.out.println("server established in "+(finTime-currTime)+"ms");
    }

    /**
     * Listens until a client connects, adds it to clients, increments numClients, and sends it its player number.
     * @param num This client's player number
     * @return the IP address of the connected player
     */
    public InetAddress connect(int num){
        try{
            Socket s = ss.accept();
            BufferedReader inStream = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter outStream = new PrintWriter(s.getOutputStream(), true);
            socketList.add(s);

            String[]in = inStream.readLine().split("-");
            InetAddress out = InetAddress.getByName(in[1]);
            System.out.println(in[0]+out+ " connected");
            clients.put(in[0], out);
            numClients++;
            outStream.println(num);
            return out;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Returns whether the game is full
     * @return whether numClients equals 2
     */
    public boolean isFull(){
        return (numClients == 2);
    }

    /**
     * Sends true to all Serversockets once the numClients becomes two, and closes the sockets after doing so
     */
    public void sendIsConnected(){
        for (Socket s: socketList){
            try{
                PrintWriter outStream = new PrintWriter(s.getOutputStream(), true);
                outStream.println(isFull());
                s.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * This class represents a thread that represents a match between two players.
     */
    class GameThread extends Thread {
        /**
         * The IP addresses of the clients in this game instance
         */
        private InetAddress[] inGameClients = new InetAddress[2];
        private ExecutorService executorService;
        private final int MAX_THREADS = 10;
        
        /**
         * Constructs a {@code GameThread} that has clients with the IP addresses in in.
         * @param in IP addresses of the clients
         */
        public GameThread(InetAddress[] in) {
            inGameClients = in;
            executorService = Executors.newFixedThreadPool(MAX_THREADS);
        }
     
        /**
         * Receives a packet from a client, and starts a new ClientHandler to handle it. 
         */
        public void receive() {
            try {
                while (true) {
                    byte[] message = new byte[26];
                    DatagramPacket in = new DatagramPacket(message, message.length);
                    ds.receive(in);
                    executorService.execute(new ClientHandler(in));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        /**
         * Sends a packet to all client sin this game instance
         * @param p the DatagramPacket to send
         */
        public void sendToAllClients(DatagramPacket p) {
                // Synchronized code block
            Packet p1 = new Packet(p.getData());
            for (InetAddress i : inGameClients) {
                if (!i.equals(p.getAddress())) {
                    try {
                        System.out.println("x: " + p1.getX() + " y: " + p1.getY());
                        ds.send(new DatagramPacket(p.getData(), p.getData().length, i, 9997));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    
    /**
     * This class handles received packets by sending them to the respective clients. 
     */
    class ClientHandler extends Thread {
        /**
         * The packet to send
         */
        private DatagramPacket in;
    
        /**
         * Constructs a {@code ClientHandler} to handle the given DatagramPacket dp
         * @param dp The packet to handle
         */
        public ClientHandler(DatagramPacket dp) {
            this.in = dp;
        }
    
        /**
         * Runs this ClientHandler, and sends the packet to all clients in the game instance. 
         */
        @Override
        public void run() {
            try {
                {
                    // Synchronized code block
                    sendToAllClients(in);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Runs this GameThread
     */
    @Override
    public void run(){
        boolean running = true;
        while (running){
            receive();
        }
    }
}

    /**
     * 
     * @return the Client IP addresses as an ArrayList
     */
    public ArrayList<InetAddress> getClients(){
        return new ArrayList(clients.keySet());
    }

    /**
     * Begins a game instance of the first two Clients that connect
     */
    public void begin(){
        InetAddress[] in = {connect(1), connect(2)};
        sendIsConnected();
        System.out.println("game thread started");
        GameThread gt = new GameThread(in);
        gt.start();
    }

    public static void main (String[]args)throws Exception{
        Server s = new Server();
        s.begin(); 
    }
}
//10.2.0.2