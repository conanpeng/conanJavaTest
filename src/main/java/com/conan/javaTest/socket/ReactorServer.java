package com.conan.javaTest.socket;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * @author huangjinsheng on 2017/3/1.
 */
public class ReactorServer {
    public static void main(String[] args) throws Exception{

    }

    Selector selector = null;
    ServerSocketChannel serverSocketChannel = null;
    /**
     *
     */
    public void start() throws Exception{
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.socket().bind(new InetSocketAddress(8088));

        SelectionKey sk = serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        sk.attach();


    }

    private void run() throws Exception{
        while (!Thread.interrupted()){
            selector.select();
            Set<SelectionKey> selected = selector.selectedKeys();

            for(SelectionKey sk : selected){
                dispatch(sk);
            }
            selected.clear();
        }
    }

    private void dispatch(SelectionKey sk){

    }

    class Acceptor implements Runnable{
        @Override
        public void run() {

        }
    }
}
