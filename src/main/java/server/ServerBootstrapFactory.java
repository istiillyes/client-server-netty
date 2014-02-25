package server;

import common.ChannelType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

/**
 * Created by isti on 2/24/14.
 */
public class ServerBootstrapFactory {

    private ServerBootstrapFactory() {
    }

    public static ServerBootstrap createServerBootstrap(final ChannelType channelType) throws UnsupportedOperationException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        switch (channelType) {
            case NIO:
                serverBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup());
                serverBootstrap.channel(NioServerSocketChannel.class);
                return serverBootstrap;

            case OIO:
                serverBootstrap.group(new OioEventLoopGroup(), new OioEventLoopGroup());
                serverBootstrap.channel(OioServerSocketChannel.class);
                return serverBootstrap;

            default:
                throw new UnsupportedOperationException("Failed to create ServerBootstrap,  " + channelType + " not supported!");
        }
    }
}
