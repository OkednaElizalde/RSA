/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP.packets;

/**
 *
 * @author Willy
 */
public interface UDPPacket {
    
    public static final int REQUEST = 0;
    public static final int ACCEPT = 1;
    public static final int MSG = 2;
    
    public int getType();
    
}
