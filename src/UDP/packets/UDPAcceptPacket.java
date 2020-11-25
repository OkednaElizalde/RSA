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
public class UDPAcceptPacket implements UDPPacket, Serializable {

    private final BigInteger e;
    private final BigInteger n;

    public UDPAcceptPacket(final BigInteger e, final BigInteger n) {
        this.e = e;
        this.n = n;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getN() {
        return n;
    }

    @Override
    public int getType() {
        return ACCEPT;
    }

}
