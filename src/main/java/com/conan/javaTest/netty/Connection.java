package com.conan.javaTest.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author huangjinsheng on 2017/2/14.
 */
public interface Connection {
    byte STATUS_NEW = 0;
    byte STATUS_CONNECTED = 1;
    byte STATUS_DISCONNECTED = 2;

    void init(Channel channel, boolean security);

    ChannelFuture send(String packet);

    ChannelFuture send(String packet, ChannelFutureListener listener);

    String getId();

    ChannelFuture close();

    boolean isConnected();

    boolean isReadTimeout();

    boolean isWriteTimeout();

    void updateLastReadTime();

    void updateLastWriteTime();

    Channel getChannel();
}
