/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package willy.util.communitations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author Willy
 */
public class IP {
    
    public static final int A_MAX_IP = 127;
    public static final int B_MAX_IP = 191;
    public static final int C_MAX_IP = 223;

    public static String getPublicIP() {
        String pIp = "";
        do {
            try {
                final URL whatismyip = new URL("http://checkip.amazonaws.com");
                final BufferedReader in = new BufferedReader(new InputStreamReader(
                        whatismyip.openStream()
                ));
                pIp = in.readLine();
            } catch (IOException ex) {
            }
        } while ("".equals(pIp));
        return pIp;
    }
    
    public static String getType(final String ip) {
        final int i = Integer.valueOf(ip.split("\\.")[0]);
        if (i <= A_MAX_IP) {
            return "A";
        } else if (i <= B_MAX_IP) {
            return "B";
        } else if (i <= C_MAX_IP) {
            return "C";
        } else {
            return "";
        }
    }
    
    public static boolean validate(final String ip) {
        boolean bol = true;
        String[] str = ip.split("\\.");
        bol = bol && ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        for (String str1 : str) {
            bol = bol && Integer.valueOf(str1) <= 255;
        }
        String type = getType(ip);
        bol = bol && ((type.equals("A") && str[1].equals("0") && str[2].equals("0") && str[3].equals("0"))
                || (type.equals("B") && str[2].equals("0") && str[3].equals("0"))
                || (type.equals("C") && str[3].equals("0")));
        return bol;
    }

    public static boolean validate2(final String ip) {
        boolean bol = true;
        String[] str = ip.split("\\.");
        bol = bol && ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        for (String str1 : str) {
            bol = bol && Integer.valueOf(str1) <= 255;
        }
        return bol;
    }
}
