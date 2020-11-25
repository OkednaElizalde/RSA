/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

import UDP.packets.*;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import willy.gui.Ventana;
import willy.util.SerializationUtils;
import willy.util.communitations.IP;
import RSA.RSA;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Willy
 */
public class UDPServer extends Ventana {

    //Variable de codificación
    private final RSA rsa;

    //Variables de GUI
    private final JTextArea texto = new JTextArea("Server iniciado en " + IP.getPublicIP());
    private final JLabel msgCL = new JLabel("Mensaje cifrado:");
    private final JTextField msgC = new JTextField("", SwingConstants.CENTER);
    private final JLabel msgDL = new JLabel("Mensaje decifrado:");
    private final JTextField msgD = new JTextField("", SwingConstants.CENTER);
    private final JButton decriptButton = new JButton("Desencriptar");

    //Variables de Comunicación
    private final DatagramSocket serverSocket;
    private byte[] receiveData;
//    private byte[] sendData;

    //Variables de escucha
    private boolean listens = true;
    private final Thread escuchar = new Thread(new Runnable() {

        @Override
        public void run() {
            boolean isListening = true;
            while (isListening && listens) {
                try {
                    //Espera a recibir el paquete
                    final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    
                    //Consigue la dirección IP
                    final InetAddress IPAddress = receivePacket.getAddress();
                    final int port = receivePacket.getPort();

                    //Transforma el paquete
                    final Object receivedObj = SerializationUtils.convertFromBytes(receivePacket.getData());
                    final UDPPacket receivedPacket = (UDPPacket) receivedObj;

                    switch (receivedPacket.getType()) {
                        case UDPPacket.REQUEST:
                            //Manda un msg al cliente de tipo Accept con los datos bonitos osi
                            sendMessage(IPAddress, port, new UDPAcceptPacket(rsa.e, rsa.n));
                            texto.append("\nSolicitud de " + IPAddress.getCanonicalHostName());
                            break;
                        case UDPPacket.MSG:
                            //Aquí pasa el msg a las variables y eso osi osi (y a lo mejor manda el msg codificado con la llave privada uwu)
                            final UDPMsgPacket receivedMsg = (UDPMsgPacket) receivedObj;
                            final String msg = receivedMsg.getMsg().toString();
                            texto.append(
                                    "\n" + IPAddress.getCanonicalHostName() + " " + (new Date()).toString() + ":"
                                    + "\n\tCifrado: " + msg
                            );
                            msgC.setText(msg);
                            break;
                        default:
                        //Se supone que no debería llegar ningún accept... pero por si acaso algo va aquí
                    }

//                    final String decriptedmsg = r.desencriptar(receivedPacket.getMsg(), receivedPacket.getN());
//                    final String sentence = " (" + receivedPacket.getSenderName() + ") c:" + receivedPacket.getMsg() + " d:" + decriptedmsg;
//                    texto.append("\n" + receivePacket.getAddress() + ":" + receivePacket.getPort() + sentence);
//                    final String returnSentence = receivedPacket.getSenderName() + ": " + decriptedmsg;
//                    RSA re = new RSA(100);
//                    re.generarPrimos();
//                    re.generarClaves();
//                    final BigInteger[] cifreg = re.encriptar(returnSentence);
//                    UDPPacket paenviar = new UDPPacket("Servidor", cifreg, re.n, re.totient, re.e);
//                    sendData = SerializationUtils.serialize(paenviar);
                } catch (IOException ex) {
                    if ("socket closed".equals(ex.getMessage())) {
                        isListening = false;
                    } else {
                        System.err.println("Something went wrong: " + ex.getMessage());
                    }
                } catch (ClassNotFoundException ex) {
                    //Aquí creo que es cuando intentamos parsear el objeto
                    JOptionPane.showMessageDialog(null, "El mensaje recibido no pudo ser convertido correctamente", "Error al parsear", WIDTH);
                    //Aquí a lo mejor podríamos mandar un msg de que no llegó correctamente al cliente y hacer que lo mande de nuevo
                } finally {
                    receiveData = new byte[8046];
//                    sendData = new byte[8046];
                }
            }
        }
    });

    public UDPServer(String title, int w, int h, boolean resizable, int puerto, int tamPrimo) throws SocketException {
        super(title, w, h, resizable);
        super.getContentPane().setLayout(null);

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("closed");
                serverSocket.close();
                listens = false;
                e.getWindow().dispose();
            }
        });

        this.serverSocket = new DatagramSocket(puerto);
        this.receiveData = new byte[8046];
//        this.sendData = new byte[8046];
        this.rsa = new RSA(tamPrimo);
        this.texto.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        this.texto.append(
                ":" + puerto
                + "\np:  " + rsa.getP().toString()
                + "\nq:  " + rsa.getQ().toString()
                + "\nn:  " + rsa.n.toString()
                + "\nphi:" + rsa.getPhi().toString()
                + "\ne:  " + rsa.e.toString()
                + "\nd:  " + rsa.getD().toString()
                + "\nMensajes de clientes:"
        );
    }

    @Override
    public void setComp() {
        final JScrollPane sptext = new JScrollPane(texto);
        this.texto.setEditable(false);
        sptext.setBounds(10, 10, 480, 220);
        super.addComp(sptext);

        this.msgCL.setBounds(10, 240, 110, 20);
        super.addComp(this.msgCL);
        this.msgC.setBounds(120, 240, 200, 20);
        this.msgC.setEnabled(false);
        super.addComp(this.msgC);
        this.decriptButton.setBounds(330, 240, 150, 20);
        super.addComp(this.decriptButton);

        this.msgDL.setBounds(10, 270, 110, 20);
        super.addComp(this.msgDL);
        this.msgD.setBounds(120, 270, 200, 20);
        this.msgD.setEnabled(false);
        super.addComp(this.msgD);

        decriptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                final String decriptedMsg = rsa.desencriptar(new BigInteger(msgC.getText())).toString();
                texto.append("\n\tDesencriptado: " + decriptedMsg);
                msgD.setText(decriptedMsg);
            }
        });
    }

    private void sendMessage(final InetAddress IPAddress, final int port, final UDPPacket msg) throws IOException {
        final byte[] sendData = SerializationUtils.serialize(msg);
        final DatagramPacket datagramPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        serverSocket.send(datagramPacket);
    }

    public static void main(String args[]) throws SocketException, InterruptedException, InvocationTargetException {
        try {
            String puerto = JOptionPane.showInputDialog(null, "Ingrese el puerto a correr el servidor", "Puerto", JOptionPane.PLAIN_MESSAGE);
            while (!puerto.matches("\\d{1,5}")) {
                puerto = JOptionPane.showInputDialog(null, "Ingrese el puerto a correr el servidor nuevamente", "Puerto", JOptionPane.PLAIN_MESSAGE);
            }

            String tamPrimo = JOptionPane.showInputDialog(null, "Ingrese el tamaño del primo", "Tamaño del primo", JOptionPane.PLAIN_MESSAGE);
            while (!tamPrimo.matches("\\d{1,4}")) {
                tamPrimo = JOptionPane.showInputDialog(null, "Ingrese el tamaño del primo", "Tamaño del primo", JOptionPane.PLAIN_MESSAGE);
            }

            final UDPServer server = new UDPServer("Servidor", 500, 300, true, Integer.valueOf(puerto), Integer.valueOf(tamPrimo));

            final Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    server.mostrar();
                }
            });

            SwingUtilities.invokeAndWait(t);

            server.escuchar.start();
        } catch (NullPointerException e) {
        } catch (IOException e) {
        }
    }
}
