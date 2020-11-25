/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RSA;

/**
 *
 * @author Willy
 */
public class InvalidMsgLength extends Exception {

    /**
     * Creates a new instance of <code>InvalidMsgCypher</code> without detail
     * message.
     */
    public InvalidMsgLength() {
    }

    /**
     * Constructs an instance of <code>InvalidMsgCypher</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidMsgLength(String msg) {
        super(msg);
    }
}
