package com.conan.javaTest.netty.client;

import com.conan.javaTest.netty.Connection;
import com.conan.javaTest.netty.NamedPoolThreadFactory;
import com.conan.javaTest.netty.NettyConnection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author huangjinsheng on 2017/2/14.
 */
public class ConnClientChannelHandler extends ChannelInboundHandlerAdapter {
    private final Connection connection = new NettyConnection();
    private boolean perfTest;
    private int hbTimeoutTimes;
    private static final Timer HASHED_WHEEL_TIMER = new HashedWheelTimer(new NamedPoolThreadFactory("mp-conn-check-timer"));
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

        if(StringUtils.startsWithIgnoreCase(str, "handshake")){
            System.out.println("handshake success, conn={}" + connection);
            connection.send("handshake-r");
        }else if(StringUtils.equalsIgnoreCase(str,"heartbeat-r")){
            System.out.println("client_ping -> pong" + connection);
        }else if(StringUtils.startsWithIgnoreCase(str, "push")){
            System.out.println("client_recieve push msg->" + str);
        }
    }

    private void startHeartBeat(final int heartbeat) throws Exception {
        HASHED_WHEEL_TIMER.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                if (connection.isConnected() && healthCheck()) {
                    HASHED_WHEEL_TIMER.newTimeout(this, heartbeat, TimeUnit.MILLISECONDS);
                }
            }
        }, heartbeat, TimeUnit.MILLISECONDS);
    }

    private boolean healthCheck() {

        if (connection.isReadTimeout()) {
            hbTimeoutTimes++;
            System.out.println("heartbeat timeout times=" + hbTimeoutTimes + ", client="+connection);
        } else {
            hbTimeoutTimes = 0;
        }

        if (hbTimeoutTimes >= 2) {
            //LOGGER.warn("heartbeat timeout times={} over limit={}, client={}", hbTimeoutTimes, 2, connection);
            hbTimeoutTimes = 0;
            connection.close();
            return false;
        }

        if (connection.isWriteTimeout()) {
            //LOGGER.info("send heartbeat ping...");
            connection.send("heartbeat");
        }

        return true;
    }
}
