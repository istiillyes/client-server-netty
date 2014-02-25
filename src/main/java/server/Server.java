package server;


import common.ChannelType;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

/**
 * Created by isti on 2/24/14.
 */
public class Server {

    private final ChannelType channelType;
    private final InetSocketAddress localAddress;
    private Channel acceptorChannel;

    public Server(final ChannelType channelType,
                  final InetSocketAddress localAddress
    ) {
        this.channelType = channelType;
        this.localAddress = localAddress;
    }

    public void start() {
        acceptorChannel = ServerChannelFactory.createAcceptorChannel(
                channelType, localAddress, new ServerHandler());
    }

    public void stop() {
        acceptorChannel.close().awaitUninterruptibly();
    }
}
