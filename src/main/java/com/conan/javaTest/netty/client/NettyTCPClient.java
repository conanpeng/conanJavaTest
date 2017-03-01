package com.conan.javaTest.netty.client;

import com.conan.javaTest.netty.Listener;
import com.conan.javaTest.netty.ServerChannelHandler;
import com.conan.javaTest.netty.ServerConnectionManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;
import java.util.List;

/**
 * @author huangjinsheng on 2017/2/14.
 */
public abstract class NettyTCPClient {
    private EventLoopGroup workerGroup;

    protected Bootstrap bootstrap;


    private void createNioClient(Listener listener) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(
                1, new DefaultThreadFactory("nettyClient-client"), SelectorProvider.provider()
        );
        workerGroup.setIoRatio(50);
        createClient(listener, workerGroup, getChannelFactory());
    }

    public ChannelFactory<? extends Channel> getChannelFactory() {
        //return NioSocketChannel::new;
        return null;
    }

    private void createClient(Listener listener,EventLoopGroup workerGroup,ChannelFactory<? extends Channel> channelFactory){
        this.workerGroup = workerGroup;
        this.bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .channelFactory(channelFactory);
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                initPipeline(ch.pipeline());
            }
        });
        initOptions(bootstrap);
    }

    public ChannelFuture connect(String host, int port) {
        return bootstrap.connect(new InetSocketAddress(host, port));
    }

    public ChannelFuture connect(String host, int port, Listener listener) {
        return bootstrap.connect(new InetSocketAddress(host, port)).addListener(f -> {
            if (f.isSuccess()) {
                if (listener != null) listener.onSuccess(port);
               System.out.println("start netty client success, host="+host+", port="+port);
            } else {
                if (listener != null) listener.onFailure(f.cause());
                System.out.println("start netty client failure, host="+host+", port="+port);
                f.cause().printStackTrace();
            }
        });
    }

    protected void initPipeline(ChannelPipeline pipeline) {
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("handler", getChannelHandler());
    }

    public abstract ChannelHandler getChannelHandler();

    protected void initOptions(Bootstrap b) {
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 4000);
        b.option(ChannelOption.TCP_NODELAY, true);
    }

}
