package client;

import common.ChannelType;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Created by isti on 2/24/14.
 */
public class ClientLauncher {

    public static void main(String[] args) throws UnknownHostException {
        InetAddress host = InetAddress.getByName("127.0.0.1");
        int port = 5005;
        new Client(ChannelType.OIO, new InetSocketAddress(host, port)).start();
    }
}
