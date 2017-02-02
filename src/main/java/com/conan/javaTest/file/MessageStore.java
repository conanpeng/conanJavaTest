package com.conan.javaTest.file;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Created by conan on 17-1-29.
 */
@Service
public class MessageStore {

    @PostConstruct
    public void test() throws Exception{
        System.out.println("aaaa");
        TimeUnit.SECONDS.sleep(5);
    }

    @PostConstruct
    public void insertFile() throws Exception {
        MapedFile mapedFile = new MapedFile("/home/conan/fileTest/tt1.tm",1024*1024*5);

        long count = 0;
        for(;;){
            String msg = (""+count+",");
            mapedFile.appendMessage(msg.getBytes());
            mapedFile.commit();
            readFile(mapedFile.getCommittedPosition().get()-msg.length(),mapedFile.getCommittedPosition().get(),mapedFile);
            count ++;

            //TimeUnit.SECONDS.sleep(1);
        }

    }

    /**
     *
     * @param pos
     */
    private void readFile(int pos, int offset,MapedFile mapedFile){
        ByteBuffer buffer = mapedFile.getMappedByteBuffer().slice();
        buffer.position(pos);
        buffer.limit(offset);

        System.out.println("->:"+Charset.forName("utf-8").decode(buffer));
        buffer = null;
    }


}
