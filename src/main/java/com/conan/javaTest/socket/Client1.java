package com.conan.javaTest.socket;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author huangjinsheng on 2017/3/1.
 */
public class Client1 {

    public static void main(String[] args) throws Exception {
        Client1 client1 = new Client1();
        client1.start();
    }

    /**
     *
     */
    public void start() throws Exception {
        int count = 0;

        while (true) {
            count++;
            Socket socket = null;
            OutputStreamWriter  out = null;
            BufferedReader br = null;
            try {
                System.out.println("connect............");
                socket = new Socket("127.0.0.1", 8088);
                out = new OutputStreamWriter(socket.getOutputStream());
                out.write((""+count+"\n"));
                out.flush();
                socket.shutdownOutput();
                br = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String msg = null;
                while ((msg = br.readLine()) != null){
                    System.out.println("client[" + Thread.currentThread().getName() + "]->" + msg);
                }
                //4.关闭资源
                br.close();
                out.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
            }
            TimeUnit.MILLISECONDS.sleep(1000);
        }


    }

}
