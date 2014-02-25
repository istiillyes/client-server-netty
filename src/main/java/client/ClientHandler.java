package client;

import common.Resources;
import file.FileUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.MessageStore;
import org.apache.log4j.Logger;

/**
 * Created by isti on 2/24/14.
 */
public final class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class);
    private int noMessagesReceived = 0;

    @Override
    public void channelActive(io.netty.channel.ChannelHandlerContext ctx) throws java.lang.Exception {
        for(int i=0; i< Resources.NO_MESSAGES_TO_SEND; i++) {
            ctx.channel().writeAndFlush(MessageStore.getMessage(i));
        }
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        noMessagesReceived++;
        if (noMessagesReceived > Resources.NO_MESSAGES_TO_SEND) {
            ctx.channel().close();
        }
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        FileUtils.write(Resources.CLIENT_OUTPUT, String.format("Received %d messages", noMessagesReceived));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error(String.format("Exception in %s", this.getClass().getName()), cause);
    }
}
