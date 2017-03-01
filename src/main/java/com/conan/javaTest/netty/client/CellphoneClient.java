package com.conan.javaTest.netty.client;

import io.netty.channel.ChannelHandler;

/**
 * @author huangjinsheng on 2017/2/14.
 */
public class CellphoneClient extends NettyTCPClient {
    @Override
    public ChannelHandler getChannelHandler() {
        return new ConnClientChannelHandler();
    }
}
