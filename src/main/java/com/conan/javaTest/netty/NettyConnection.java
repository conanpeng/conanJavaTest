package com.conan.javaTest.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huangjinsheng on 2017/2/14.
 */
public class NettyConnection implements Connection, ChannelFutureListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyConnection.class);
    private Channel channel;
    private volatile byte status = STATUS_NEW;
    private long lastReadTime;
    private long lastWriteTime;

    @Override
    public void init(Channel channel, boolean security) {
        this.channel = channel;
        this.lastReadTime = System.currentTimeMillis();
        this.status = STATUS_CONNECTED;
    }

    @Override
    public String getId() {
        return channel.id().asShortText();
    }

    @Override
    public ChannelFuture send(String packet) {
        return send(packet, null);
    }

    @Override
    public ChannelFuture send(String packet, final ChannelFutureListener listener) {
        if (channel.isActive()) {

            ChannelFuture future = channel.writeAndFlush(channel).addListener(this);

            if (listener != null) {
                future.addListener(listener);
            }

            if (channel.isWritable()) {
                return future;
            }

            //阻塞调用线程还是抛异常？
            //return channel.newPromise().setFailure(new RuntimeException("send data too busy"));
            if (!future.channel().eventLoop().inEventLoop()) {
                future.awaitUninterruptibly(100);
            }
            return future;
        } else {
            /*if (listener != null) {
                channel.newPromise()
                        .addListener(listener)
                        .setFailure(new RuntimeException("connection is disconnected"));
            }*/
            return this.close();
        }
    }

    @Override
    public ChannelFuture close() {
        if (status == STATUS_DISCONNECTED) return null;
        this.status = STATUS_DISCONNECTED;
        return this.channel.close();
    }

    @Override
    public boolean isConnected() {
        return status == STATUS_CONNECTED;
    }

    @Override
    public boolean isReadTimeout() {
        return System.currentTimeMillis() - lastReadTime > 10000 + 1000;
    }

    @Override
    public boolean isWriteTimeout() {
        return System.currentTimeMillis() - lastWriteTime > 10000 - 1000;
    }

    @Override
    public void updateLastReadTime() {
        lastReadTime = System.currentTimeMillis();
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            lastWriteTime = System.currentTimeMillis();
        } else {
            LOGGER.error("connection send msg error", future.cause());
            //Logs.CONN.error("connection send msg error={}, conn={}", future.cause().getMessage(), this);
        }
    }

    @Override
    public void updateLastWriteTime() {
        lastWriteTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "[channel=" + channel
                + ", status=" + status
                + ", lastReadTime=" + lastReadTime
                + ", lastWriteTime=" + lastWriteTime
                + "]";
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NettyConnection that = (NettyConnection) o;

        return channel.id().equals(that.channel.id());
    }

    @Override
    public int hashCode() {
        return channel.id().hashCode();
    }
}
