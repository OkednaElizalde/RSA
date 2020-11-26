/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

import RSA.InvalidMsgLength;
import UDP.packets.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import willy.gui.Ventana;
import willy.util.SerializationUtils;
import willy.util.communitations.IP;
import RSA.RSA;
import java.awt.HeadlessException;
import java.math.BigInteger;

/**
 *
 * @author Willy
 */
public class UDPClient extends Ventana {

    private final BigInteger e, n;

    private final JTextArea texto = new JTextArea("Cliente");
    private final JTextField sendingText = new JTextField();
    private final JButton enviar = new JButton("enviar");

    private final DatagramSocket clientSocket;
    private final InetAddress IPAddress;
    private byte[] receiveData = new byte[8046];
    private final int puerto;

    public UDPClient(String title, int w, int h, boolean resizable, String ip, int puerto) throws SocketException, UnknownHostException, IOException, ClassNotFoundException {
        super(title, w, h, resizable);
        super.getContentPane().setLayout(null);

        this.puerto = puerto;
        this.clientSocket = new DatagramSocket(this.puerto);
        this.IPAddress = InetAddress.getByName(ip);

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("closed");
                clientSocket.close();
                e.getWindow().dispose();
            }
        });

        sendMessage(new UDPRequestPacket());

        final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        
        final UDPAcceptPacket receivedAccept = (UDPAcceptPacket) SerializationUtils.convertFromBytes(receiveData);
        this.e = receivedAccept.getE();
        this.n = receivedAccept.getN();
        
        this.texto.append(
                "\nConectado exitosamente"
                + "\ne: " + this.e.toString()
                + "\nn: " + this.n.toString()
        );

        receiveData = new byte[8046];
    }

    @Override
    public void setComp() {
        final JScrollPane jsp = new JScrollPane(texto);
        this.texto.setEditable(false);
        jsp.setBounds(10, 10, 490, 160);
        super.addComp(jsp);

        this.sendingText.setBounds(10, 180, 250, 20);
        super.addComp(sendingText);

        this.enviar.setBounds(270, 180, 150, 20);
        super.addComp(enviar);

        enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    final String msg = sendingText.getText();
                    if (msg.matches("\\d{1,}")) {
                        final BigInteger sentence = RSA.encriptar(new BigInteger(msg), e, n);

                        final UDPMsgPacket udpp = new UDPMsgPacket(sentence);
                        sendMessage(udpp);
                    }
                } catch (NullPointerException ex) {
                    System.out.println("Something happened " + ex.getMessage());
                } catch (InvalidMsgLength ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException ex) {
                    System.out.println("Error en la entrada o salida" + ex.getMessage());
                }
            }
        });

    }

    private void sendMessage(final UDPPacket msg) throws IOException {
        final byte[] sendData = SerializationUtils.serialize(msg);
        final DatagramPacket datagramPacket = new DatagramPacket(sendData, sendData.length, this.IPAddress, this.puerto);
        clientSocket.send(datagramPacket);
    }

    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException, InvocationTargetException, SocketException, ClassNotFoundException, NullPointerException {
        try {
            String ip = JOptionPane.showInputDialog(null, "Ingrese la ip del servidor", "IP", JOptionPane.PLAIN_MESSAGE);
            while (!IP.validate2(ip)) {
                ip = JOptionPane.showInputDialog(null, "Ingrese la ip del servidor nuevamente", "IP", JOptionPane.PLAIN_MESSAGE);
            }

            String puerto = JOptionPane.showInputDialog(null, "Ingrese el puerto del servidor", "Puerto", JOptionPane.PLAIN_MESSAGE);
            while (!puerto.matches("\\d{1,5}")) {
                puerto = JOptionPane.showInputDialog(null, "Ingrese el puerto del servidor nuevamente", "Puerto", JOptionPane.PLAIN_MESSAGE);
            }

            final UDPClient cliente = new UDPClient("Cliente", 500, 210, false, ip, Integer.valueOf(puerto));

            final Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    cliente.mostrar();
                }
            });

            SwingUtilities.invokeAndWait(t);

        } catch (HeadlessException
                | IOException
                | ClassNotFoundException
                | InterruptedException
                | NumberFormatException
                | InvocationTargetException
                | NullPointerException e) {
            System.err.println(e);
        }
    }
}
