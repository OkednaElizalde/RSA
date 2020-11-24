/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Willy
 */
public class UDPPacket implements Serializable{
    
    private final String senderName;
    private final BigInteger[] msg;
    private final BigInteger n;
    private final BigInteger totient;
    private final BigInteger e;
    
    public UDPPacket(String senderName, BigInteger[] msg, BigInteger n, BigInteger totient, BigInteger e) {
        this.senderName = senderName;
        this.msg = msg;
        this.n = n;
        this.totient = totient;
        this.e = e;
    }

    public String getSenderName() {
        return senderName;
    }

    public BigInteger[] getMsg() {
        return msg;
    }
    
    public BigInteger getN(){
        return n;
    }
    
    public BigInteger getTotient(){
        return totient;
    }
    
    public BigInteger getE(){
        return e;
    }
}
