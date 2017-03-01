package com.conan.javaTest.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ThreadFactory;


/**
 * @author huangjinsheng on 2017/2/14.
 */
public class NettyPushTCPServer {

    protected final int port;
    protected EventLoopGroup bossGroup ;
    protected EventLoopGroup workerGroup ;

    public NettyPushTCPServer(int port){
        this.port = port;
    }

    public void start(){

    }

    private void createNIOServer(Listener listener){
        EventLoopGroup bossGroup = getBossGroup();
        EventLoopGroup workerGroup = getWorkerGroup();

        if(bossGroup == null){
            NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(1,getBossThreadFactory(), SelectorProvider.provider());
            nioEventLoopGroup.setIoRatio(100);
            bossGroup = nioEventLoopGroup;
        }

        if(workerGroup == null){
            NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(0,getWorkThreadFactory(),SelectorProvider.provider());
            nioEventLoopGroup.setIoRatio(70);
            workerGroup = nioEventLoopGroup;
        }

        createServer(listener,bossGroup,workerGroup,getChannelFactory());
    }

    private void createServer(Listener listener,EventLoopGroup bossGroup,EventLoopGroup workerGroup,ChannelFactory<? extends ServerChannel> channelFactory){
        this.bossGroup = bossGroup;
        this.workerGroup = bossGroup;

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup);
            serverBootstrap.channelFactory(channelFactory);

            serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    initPipiline(channel.pipeline());
                }
            });
            initOptions(serverBootstrap);

            serverBootstrap.bind(port).addListener(future -> {
                if(future.isSuccess()){
                    if(listener != null){
                        System.out.println("server start successfully on port:"+port);
                        listener.onSuccess(port);
                    }
                }else{
                    System.out.println("server start failed on port:"+port+"-->");
                    future.cause().printStackTrace();
                    if(listener != null){
                        listener.onFailure(future.cause());
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            if(listener != null){
                listener.onFailure(e);
            }
            throw e;
        }
    }

    /**
     *
     * @param pipeline
     */
    protected void initPipiline(ChannelPipeline pipeline){
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("handler",new ServerChannelHandler(new ServerConnectionManager(true)));
    }

    /**
     *
     * @param b
     */
    protected void initOptions(ServerBootstrap b){ //使用缓存池
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.childOption(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT);
        b.option(ChannelOption.SO_BACKLOG, 1024);
    }

    protected ThreadFactory getBossThreadFactory(){
        return new DefaultThreadFactory("nioPushServer-boss");
    }

    protected ThreadFactory getWorkThreadFactory(){
        return new DefaultThreadFactory("nioPushServer-worker");
    }

    public ChannelFactory<? extends ServerChannel> getChannelFactory(){
        //return NioServerSocketChannel::new;
        return null;
    }


    public int getPort() {
        return port;
    }

    public EventLoopGroup getBossGroup() {
        return bossGroup;
    }

    public void setBossGroup(EventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public void setWorkerGroup(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }
}
