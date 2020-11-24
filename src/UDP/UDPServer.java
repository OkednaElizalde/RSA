/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDP;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import willy.gui.Ventana;
import willy.util.SerializationUtils;
import willy.util.communitations.IP;

/**
 *
 * @author Willy
 */
public class UDPServer extends Ventana {

    private final JTextArea texto = new JTextArea("Server iniciado en " + IP.getPublicIP());

    private final DatagramSocket serverSocket;
    private final List<InetAddress> clientAddressList = new ArrayList<>();
    private final List<Integer> clientPortList = new ArrayList<>();
    private byte[] receiveData;
    private byte[] sendData;

    private boolean listens = true;

    private final Thread escuchar = new Thread(new Runnable() {

        @Override
        public void run() {
            boolean isListening = true;
            while (isListening && listens) {
                try {
                    final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);

                    final InetAddress IPAddress = receivePacket.getAddress();
                    final int port = receivePacket.getPort();
                    if (!clientAddressList.contains(IPAddress) || !clientPortList.contains(port)) {
                        clientAddressList.add(IPAddress);
                        clientPortList.add(port);
                    }

                    final UDPPacket receivedPacket = (UDPPacket) SerializationUtils.convertFromBytes(receivePacket.getData());
                    final String sentence = receivedPacket.getSenderName() + ": " + receivedPacket.getMsg();
//                    final String sentence = new String(receivePacket.getData());
                    texto.append("\n" + receivePacket.getAddress() + ":" + receivePacket.getPort() + " dice: " + sentence);

                    final String capitalizedSentence = sentence.toUpperCase();
                    sendData = capitalizedSentence.getBytes();

                    for (int i = 0; i < clientAddressList.size(); i++) {
                        final DatagramPacket sendPacket = new DatagramPacket(
                                sendData,
                                sendData.length,
                                clientAddressList.get(i),
                                clientPortList.get(i)
                        );
                        serverSocket.send(sendPacket);
                    }
                } catch (IOException ex) {
                    if ("socket closed".equals(ex.getMessage())) {
                        isListening = false;
                    } else {
                        System.err.println("Something went wrong: " + ex.getMessage());
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    receiveData = new byte[1024];
                    sendData = new byte[1024];
                }
            }
        }
    });

    public UDPServer(String title, int w, int h, boolean resizable, int puerto) throws SocketException {
        super(title, w, h, resizable);
        super.getContentPane().setLayout(new BorderLayout());

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
        receiveData = new byte[1024];
        sendData = new byte[1024];
    }

    @Override
    public void setComp() {
        final JScrollPane sptext = new JScrollPane(texto/*, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS*/);
        texto.setEditable(false);
        super.addComp(sptext, BorderLayout.CENTER);
    }

    public static void main(String args[]) throws SocketException, InterruptedException, InvocationTargetException {
        try {
            String puerto = JOptionPane.showInputDialog(null, "Ingrese el puerto a correr el servidor", "Puerto", JOptionPane.PLAIN_MESSAGE);
            while (!puerto.matches("\\d{1,5}")) {
                puerto = JOptionPane.showInputDialog(null, "Ingrese el puerto a correr el servidor nuevamente", "Puerto", JOptionPane.PLAIN_MESSAGE);
            }
            
            final String pIp = IP.getPublicIP();
            JOptionPane.showMessageDialog(null, "Tu ip pública es: " + pIp, "IP pública", JOptionPane.INFORMATION_MESSAGE);

            final UDPServer server = new UDPServer("Servidor", 200, 200, true, Integer.valueOf(puerto));

            final Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    server.mostrar();
                }
            });

            SwingUtilities.invokeAndWait(t);

            server.escuchar.start();
        } catch (NullPointerException e) {
        } catch (IOException e) {}
    }
}
