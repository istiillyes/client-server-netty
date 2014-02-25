package client;

import common.ChannelType;
import common.Resources;
import io.netty.channel.Channel;
import message.MessageStore;

import java.net.InetSocketAddress;

/**
 * Created by isti on 2/24/14.
 */
public class Client {

    private final ChannelType channelType;
    private final InetSocketAddress remoteAddress;

    public Client(ChannelType channelType, InetSocketAddress remoteAddress) {
        this.channelType = channelType;
        this.remoteAddress = remoteAddress;
    }

    public void start() {
        ClientChannelFactory.createConnectorChannel(
                channelType, new ClientHandler(), remoteAddress);
    }
}
