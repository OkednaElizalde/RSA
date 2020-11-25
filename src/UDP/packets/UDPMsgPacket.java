/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP.packets;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Willy
 */
public class UDPMsgPacket implements UDPPacket, Serializable {

    private final BigInteger msg;

    public UDPMsgPacket(final BigInteger msg) {
        this.msg = msg;
    }

    public BigInteger getMsg() {
        return msg;
    }

    @Override
    public int getType() {
        return MSG;
    }

}
