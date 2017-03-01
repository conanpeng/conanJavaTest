package com.conan.javaTest.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author huangjinsheng on 2017/2/14.
 */
@ChannelHandler.Sharable
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    private final ConnectionManager connectionManager;

    public ServerChannelHandler(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelRead(Object)} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * <p/>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String str = (String) msg;

        Connection connection = connectionManager.get(ctx.channel());
        connection.updateLastReadTime();

        if(StringUtils.startsWithIgnoreCase(str,"handshake")){
            System.out.println("handshake success, conn={}" + connection);
            connection.send("handshake-r");
        }else if(StringUtils.equalsIgnoreCase(str,"heartbeat-r")){
            connection.send("heartbeat");//ping -> pong
            System.out.println("ping -> pong" + connection);
        }else if(StringUtils.startsWithIgnoreCase(str, "push")){
            connection.send(RandomStringUtils.randomAlphabetic(15));
        }
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelActive()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * <p/>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client connected conn={}" + ctx.channel());
        Connection connection = new NettyConnection();
        connection.init(ctx.channel(), false);
        connectionManager.add(connection);
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelInactive()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     * <p/>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Connection connection = connectionManager.removeAndClose(ctx.channel());
//        EventBus.I.post(new ConnectionCloseEvent(connection));
        System.out.println("client disconnected conn={}" + connection);
    }

    /**
     * Calls {@link ChannelHandlerContext#fireExceptionCaught(Throwable)} to forward
     * to the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     * <p/>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Connection connection = connectionManager.get(ctx.channel());
        System.out.println("client caught ex, conn={}" + connection);
        System.out.println("caught an ex, channel=" + ctx.channel() + ", conn={}" + connection);
        cause.printStackTrace();
        ctx.close();
    }
}
