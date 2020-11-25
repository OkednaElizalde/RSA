/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

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
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private boolean listens = true;

    //Si el cliente sólo recibe un paquete Accept, a lo mejor no es necesario hacer un hilo de escucha, a menos que quieras
    // que reciba el msg cifrado con d, te lo dejo aquí por si acaso
//    private final Thread escuchar = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            while (listens) {
//                try {
//                    final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//                    clientSocket.receive(receivePacket);
//
//                    final Object receivedObj = SerializationUtils.convertFromBytes(receivePacket.getData());
//                    final UDPPacket receivedPacket = (UDPPacket) receivedObj;
//
//                    switch (receivedPacket.getType()) {
//                        case UDPPacket.ACCEPT:
//
//                            break;
//                    }
//                    RSA r = new RSA(100);
//                    r.generarD(receivedPacket.getE(), receivedPacket.getTotient());
//                    final String modifiedSentence = r.desencriptar(receivedPacket.getMsg(), receivedPacket.getN());
//                    texto.append("\nRespuesta: " + modifiedSentence);
//                } catch (IOException ex) {
//                    if ("socket closed".equals(ex.getMessage())) {
//                        listens = false;
//                    } else {
//                        System.err.println("Murió en la escucha unu " + ex.getMessage());
//                    }
//                } catch (ClassNotFoundException ex) {
//                    Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
//                } finally {
//                    System.out.println(clientSocket.getPort());
//                    receiveData = new byte[8046];
//                }
//            }
//        }
//    });
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
                listens = false;
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
//                try {
//                    int msg = Integer.parseInt(sendingText.getText());
//                    String elc = Integer.toString(men);
//                    final BigInteger[] sentence = r.encriptar(elc);
//                    final UDPPacket udpp = new UDPPacket(name, sentence, r.n, r.totient, r.e);
//
//                    sendData = SerializationUtils.serialize(udpp);
//
//                    final DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, puerto);
//                    clientSocket.send(sendPacket);
//                } catch (IOException | NullPointerException ex) {
//                    System.out.println("Something happeneden " + ex.getMessage());
//                } catch(NumberFormatException e){
//                    System.out.println("Solo números en el mensaje por favor");
//                } finally {
//                    System.out.println("finally");
//                    sendData = new byte[8046];
//                }
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

//            cliente.escuchar.start();  
        } catch (HeadlessException
                | IOException
                | ClassNotFoundException
                | InterruptedException
                | NumberFormatException
                | InvocationTargetException
                | NullPointerException e) {
            System.err.println(e);
            //Algo pashó, qué? Quien sabe xD
        }
    }
}
