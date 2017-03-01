package com.conan.javaTest.netty;

import io.netty.channel.Channel;

/**
 * @author huangjinsheng on 2017/2/14.
 */
public interface ConnectionManager {
    Connection get(Channel channel);

    Connection removeAndClose(Channel channel);

    void add(Connection connection);

    int getConnNum();

    void init();

    void destroy();
}
