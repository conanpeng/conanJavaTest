package com.conan.javaTest.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author huangjinsheng on 2017/3/1.
 */
public class Server1 {

    public static void main(String[] args) throws Exception{
        Server1 server1 = new Server1();
        server1.start();
    }

    public static final ExecutorService pool = Executors.newFixedThreadPool(10);
    /**
     *
     */
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8088);
        while (true){
            Socket socket = serverSocket.accept();
            pool.execute(new Handler(socket));
        }

    }

    class Handler implements Runnable{
        final Socket socket;
        public Handler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            try{
                System.out.println("connect............");
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg = null;
                while ((msg = br.readLine()) != null){
                    System.out.println("server[" + Thread.currentThread().getName() + "]->" + msg);
                }


                //TimeUnit.MILLISECONDS.sleep(2000);
                OutputStream socketOut=socket.getOutputStream();
                BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
                out.write(("" + "ok" + "\n").getBytes());
                out.flush();
                br.close();
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
