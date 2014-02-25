package client;

import common.ChannelType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;

/**
 * Created by isti on 2/24/14.
 */
public class BootstrapFactory {

    private BootstrapFactory() {
    }

    public static Bootstrap createBootstrap(final ChannelType channelType) throws UnsupportedOperationException {
        Bootstrap bootstrap = new Bootstrap();

        switch (channelType) {
            case NIO:
                bootstrap.group(new NioEventLoopGroup());
                bootstrap.channel(NioSocketChannel.class);
                return bootstrap;

            case OIO:
                bootstrap.group(new OioEventLoopGroup());
                bootstrap.channel(OioSocketChannel.class);
                return bootstrap;

            default:
                throw new UnsupportedOperationException("Failed to create Bootstrap,  " + channelType + " not supported!");
        }
    }
}
