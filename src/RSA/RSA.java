package RSA;

import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class RSA {

    private final int tamPrimo;

    private final BigInteger q, p;
    public final BigInteger n;
    private final BigInteger phi;

    public final BigInteger e;
    private final BigInteger d;

    public RSA(int tamPrimo) {
        this.tamPrimo = (BigInteger.TEN.pow(tamPrimo + 1)).subtract(BigInteger.ONE).toString(2).length() - 1;

        this.p = new BigInteger(this.tamPrimo, 10, new Random());
        BigInteger qt;
        do {
            qt = new BigInteger(this.tamPrimo, 10, new Random());
        } while (qt.compareTo(p) == 0);
        this.q = qt;

        this.n = this.p.multiply(this.q);
        this.phi = this.p.subtract(BigInteger.valueOf(1)).multiply(this.q.subtract(BigInteger.valueOf(1)));

        BigInteger et;
        do {
            et = new BigInteger(2 * this.tamPrimo, new Random());
        } while ((et.compareTo(phi) != -1)
                || (et.gcd(phi).compareTo(BigInteger.valueOf(1)) != 0));

        this.e = et;
        this.d = this.e.modInverse(this.phi);

    }

    /*
    Cifrar con el numero e ya que "e" es la clave publica
     */
//    public BigInteger[] encriptar(String mensaje){
//        //variables
//        int i;
//        byte[] temp = new byte[1];
//        byte[] digitos = mensaje.getBytes();
//        BigInteger[] bigdigitos = new BigInteger[digitos.length];
//
//        //lo primero que debemos hacer es correr el tamaño de bigdigitos
//        for(i = 0; i<bigdigitos.length; i++){
//            temp[0] = digitos[i];
//            bigdigitos[i] = new BigInteger(temp);
//        }
//
//        //vamos a cifrar
//        BigInteger[] encriptado = new BigInteger[bigdigitos.length];
//
//        for(i = 0; i<bigdigitos.length; i++){
//            encriptado[i] = bigdigitos[i].modPow(e,n);
//        }
//        return encriptado;
//    }

    /*
    descifrar array de biginteger
     */
//    public String desencriptar(BigInteger[] encriptado, BigInteger n){
//        BigInteger[] desencriptar = new BigInteger[encriptado.length];
//
//        for(int i = 0; i<desencriptar.length; i++){
//            desencriptar[i] = encriptado[i].modPow(d, n);
//        }
//
//        char[] charArray = new char[desencriptar.length];
//
//        for(int i = 0; i<charArray.length; i++){
//            charArray[i] = (char)(desencriptar[i].intValue());
//        }
//
//        return (new String(charArray));
//    }
    public BigInteger encriptar(final BigInteger msg) throws InvalidMsgLength {
        if (msg.compareTo(n) > 0) {
            throw new InvalidMsgLength("El mensage no puede ser mayor al valor de n: "
                    + msg.toString() + " > " + n.toString());
        }
        return msg.modPow(e, n);
    }

    public BigInteger desencriptar(final BigInteger cypher) {
        return cypher.modPow(d, n);
    }

    public int getTamPrimo() {
        return tamPrimo;
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getPhi() {
        return phi;
    }

    public BigInteger getD() {
        return d;
    }

    public static BigInteger encriptar(final BigInteger msg, final BigInteger e, final BigInteger n) throws InvalidMsgLength {
        if (msg.compareTo(n) > 0) {
            throw new InvalidMsgLength("El mensage no puede ser mayor al valor de n: "
                    + msg.toString() + " > " + n.toString());
        }
        return msg.modPow(e, n);
    }

    public static BigInteger desencriptar(final BigInteger cypher, final BigInteger d, final BigInteger n) {
        return cypher.modPow(d, n);
    }

}
