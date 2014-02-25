package client;

import java.net.*;

import common.ChannelType;
import common.Resources;
import io.netty.bootstrap.Bootstrap;
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
final class ClientChannelFactory {

    private static final Logger LOGGER = Logger.getLogger(ClientChannelFactory.class);

    protected static Channel createConnectorChannel(
            ChannelType channelType,
            final ClientHandler clientHandler,
            InetSocketAddress remoteAddress
    ) {
        final Bootstrap bootstrap = BootstrapFactory.createBootstrap(channelType);

        bootstrap.handler(new ClientChannelInitializer(clientHandler));
        bootstrap.option(ChannelOption.TCP_NODELAY, true);

        try {
            final ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(remoteAddress.getAddress(), remoteAddress.getPort()))
                    .sync();
            channelFuture.awaitUninterruptibly();
            if (channelFuture.isSuccess()) {
                return channelFuture.channel();

            } else {
                LOGGER.warn(String.format(
                        "Failed to open socket! Cannot connect to ip: %s port: %d!",
                        remoteAddress.getAddress(), remoteAddress.getPort())
                );
            }
        } catch (InterruptedException e) {
            LOGGER.error("Failed to open socket!", e);
        }
        return null;
    }

    private static class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

        private ChannelHandler clientHandler;

        private ClientChannelInitializer(ChannelHandler clientHandler) {
            this.clientHandler = clientHandler;
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
            ch.pipeline().addLast(Resources.CLIENT_HANDLER_NAME, clientHandler);
        }
    }
}
