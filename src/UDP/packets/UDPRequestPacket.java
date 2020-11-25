/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP.packets;

import java.io.Serializable;

/**
 *
 * @author Willy
 */
public class UDPRequestPacket implements UDPPacket, Serializable {

    @Override
    public int getType() {
        return REQUEST;
    }

}
