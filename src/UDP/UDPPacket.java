/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

import java.io.Serializable;

/**
 *
 * @author Willy
 */
public class UDPPacket implements Serializable{
    
    private final String senderName;
    private final String msg;
    
    public UDPPacket(String senderName, String msg) {
        this.senderName = senderName;
        this.msg = msg;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMsg() {
        return msg;
    }
    
}
