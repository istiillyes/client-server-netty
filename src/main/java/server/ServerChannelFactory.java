package server;

import java.net.*;

import common.ChannelType;
import common.Resources;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

/**
 * Created by isti on 2/24/14.
 */
final class ServerChannelFactory {

    private static final Logger LOGGER = Logger.getLogger(ServerChannelFactory.class);

    protected static Channel createAcceptorChannel(
            final ChannelType channelType,
            final InetSocketAddress localAddress,
            final ServerHandler serverHandler
    ) {
        final ServerBootstrap serverBootstrap = ServerBootstrapFactory.createServerBootstrap(channelType);

        serverBootstrap
                .childHandler(new ServerChannelInitializer(serverHandler))
                .option(ChannelOption.SO_BACKLOG, Resources.SO_BACKLOG)
                .childOption(ChannelOption.TCP_NODELAY, true);

        try {
            ChannelFuture channelFuture = serverBootstrap.bind(
                    new InetSocketAddress(localAddress.getPort())).sync();
            channelFuture.awaitUninterruptibly();
            if (channelFuture.isSuccess()) {
                return channelFuture.channel();

            } else {
                LOGGER.warn(String.format("Failed to open socket! Cannot bind to port: %d!",
                        localAddress.getPort()));
            }
        } catch (InterruptedException e) {
            LOGGER.error("Failed to create acceptor socket.", e);
        }
        return null;
    }

    private static class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

        private ChannelHandler serverHandler;

        private ServerChannelInitializer(ChannelHandler serverHandler) {
            this.serverHandler = serverHandler;
        }

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            // Encoders
            ch.pipeline().addLast(Resources.STRING_ENCODER_NAME, new StringEncoder(CharsetUtil.UTF_8));
            ch.pipeline().addBefore(Resources.STRING_ENCODER_NAME, Resources.FRAME_ENCODER_NAME,
                    new LengthFieldPrepender(Resources.FRAME_LENGTH_FIELD_SIZE));

            // Decoders
            ch.pipeline().addLast(Resources.STRING_DECODER_NAME, new StringDecoder(CharsetUtil.UTF_8));
            ch.pipeline().addBefore(Resources.STRING_DECODER_NAME, Resources.FRAME_DECODER_NAME,
                    new LengthFieldBasedFrameDecoder(Resources.MAX_FRAME_LENGTH,
                            Resources.FRAME_LENGTH_FIELD_OFFSET, Resources.FRAME_LENGTH_FIELD_SIZE,
                            Resources.FRAME_LENGTH_ADJUSTMENT, Resources.FRAME_LENGTH_FIELD_SIZE));

            // Handlers
            ch.pipeline().addLast(Resources.LOGGING_HANDLER_NAME, new LoggingHandler());
            ch.pipeline().addLast(Resources.SERVER_HANDLER_NAME, serverHandler);
        }
    }
}
