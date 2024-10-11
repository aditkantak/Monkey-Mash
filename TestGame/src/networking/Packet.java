package networking;

import java.util.Arrays;

/**
 * This class represents the information of a player at each frame, in order to send to the server.
 * The byte[] value is arraged as below:
 * <p>
 * Xcoord: 5 bytes
 * <p>
 * Ycoord: 5 bytes
 * <p>
 * Shooting: 5 bytes
 * <p>
 * TargX: 5 bytes
 * <p>
 * TargY: 5 bytes
 * <p>
 * WeaponType: 1 byte
 */
public class Packet {
    /**
     * Current player position/target position
     */
    private int x, y, targx, targy;
    /**
     * Currently equipped weapon
     */
    private byte health;
    /**
     * Whether the player is shooting
     */
    private boolean shooting;
    /**
     * byte array to put into DatagramPacket
     */
    private byte[] value = new byte[26];
    /**
     * length of all {@code Packet}s
     */
    public static final int length = 26;

    /**
     * Constructs a {@code Packet} based on a player's in-game state and initializes the byte[] value of the {@code Packet}. 
     * 
     * @param xcoord current x-coordinate of player
     * @param ycoord current y-coordinate of player
     * @param shooting whether the player is shooting or not
     * @param targetx current target x-coordinate of the player
     * @param targety current target y-coordinate of the player
     * @param healthType current health
     */
    public Packet(int xcoord, int ycoord, boolean shooting, int targetx, int targety, byte healthType){
        x = xcoord;
        y = ycoord;
        this.shooting = shooting;
        targx = targetx;
        targy = targety;
        health = healthType;
        byte[] b = (""+x).getBytes();
        for (int i = 0; i<5; i++){
            if (i<b.length){
                value[i] = b[i];
            }
        }
        b = (""+y).getBytes();
        for (int i = 5; i<10; i++){
            if (i<b.length+5){
                value[i] = b[i-5];
            } 
        }
        b = (""+shooting).getBytes();
        for (int i = 10; i<15; i++){
            if (i<b.length+10){
                value[i] = b[i-10];
            }
        }
        b = (""+targx).getBytes();
        for (int i = 15; i<20; i++){
            if (i<b.length+15){
                value[i] = b[i-15];
            }
        }
        b = (""+targy).getBytes();
        for (int i = 20; i<25; i++){
            if (i<b.length+20){
                value[i] = b[i-20];
            }
        }
        value [25] = health;
    }

    /**
     * Constructs a {@code Packet} from the value of a DatagramPacket
     * @param data byte array value from DatagramPacket
     */
    public Packet(byte[] data){
        value = data;
        x = Integer.parseInt(new String(Arrays.copyOf(data, 5)).trim());
        y = Integer.parseInt(new String(Arrays.copyOfRange(data, 5, 10)).trim());
        shooting = Boolean.parseBoolean(new String(Arrays.copyOfRange(data, 10, 15)).trim());
        targx = Integer.parseInt(new String(Arrays.copyOfRange(data, 15, 20)).trim());
        targy = Integer.parseInt(new String(Arrays.copyOfRange(data, 20, 25)).trim());
        health = data[25];
    }

    /**
     * Returns whether the player is shooting
     * @return whether the player is shooting
     */
    public boolean isShooting(){
        return shooting;
    }

    /**
     * Returns the X coordinate.
     * @return the X coordinate in the packet
     */
    public int getX(){
        return x;
    }

    /**
     * Returns the Y coordinate.
     * @return the Y coordinate in the Packet
     */
    public int getY(){
        return y;
    }

    /**
     * Returns the target X coordinate.
     * @return the target X coordinate in the Packet
     */
    public int getTargX(){
        return targx;
    }

    /**
     * Returns the target Y coordinate.
     * @return the target Y coordinate in the Packet
     */
    public int getTargY(){
        return targy;
    }

    /**
     * Returns the current weapon type.
     * @return the equipped weapon type in the Packet
     */
    public int getHealth(){
        return (int)health;
    }

    /**
     * Returns the {@code byte[]} value of the Packet's contents.
     * @return the byte value to send this Packet through a DatagramPacket
     */
    public byte[] getValue(){
        return value;
    }

    /**
     * Prints the Packet for testing purposes
     */
    public void printPacket (){
        System.out.println("position: "+x+", "+ y);
        System.out.println("shooting: "+shooting);
        System.out.println("target: "+ targx + ", "+ targy);
        System.out.println("health: "+ health);
    }
}