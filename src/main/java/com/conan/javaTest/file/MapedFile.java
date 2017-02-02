package com.conan.javaTest.file;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by conan on 17-1-29.
 */
public class MapedFile {
    public static final int OS_PAGE_SIZE = 1024 * 4;

    private MappedByteBuffer mappedByteBuffer;
    private FileChannel fileChannel;

    private String fileName;
    private int fileSize;
    private File file;
    private long fileFormOffset;
    private AtomicInteger wrotePostion = new AtomicInteger(0);
    private AtomicInteger committedPosition = new AtomicInteger(0);
    private AtomicInteger readPosition = new AtomicInteger(0);


    public MapedFile(final String fileName, final int fileSize) throws IOException {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.file = new File(fileName);

        ensureDirOK(this.file.getParent());

        boolean ok = false;

        try {
            this.fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
            this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE,0,fileSize);
        } finally {
            if (!ok && this.fileChannel != null) {
                this.fileChannel.close();
            }
        }
    }

    /**
     *
     * @param data
     */
    public boolean appendMessage(final byte[] data){
        if(data == null ||  data.length == 0){
            return false;
        }

        int currentPosition = this.wrotePostion.get();
        if((currentPosition + data.length) > this.fileSize){
            System.out.println("data is too large...");
            return false;
        }

        ByteBuffer buffer = this.mappedByteBuffer.slice();
        buffer.position(currentPosition);
        buffer.put(data);
        this.wrotePostion.addAndGet(data.length);


        return true;
    }

    public void commit(){
        int currentPosition = this.wrotePostion.get();
        this.mappedByteBuffer.force();
        this.committedPosition.set(currentPosition);
        try {
            clean(this.mappedByteBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("file commited ! "+this.committedPosition.get());
    }


    public static void clean(final ByteBuffer buffer) {
        if (buffer == null || !buffer.isDirect() || buffer.capacity() == 0)
            return;
        invoke(invoke(viewed(buffer), "cleaner"), "clean");
    }


    private static Object invoke(final Object target, final String methodName, final Class<?>... args) {
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    Method method = method(target, methodName, args);
                    method.setAccessible(true);
                    return method.invoke(target);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }


    private static Method method(Object target, String methodName, Class<?>[] args)
            throws NoSuchMethodException {
        try {
            return target.getClass().getMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            return target.getClass().getDeclaredMethod(methodName, args);
        }
    }


    private static ByteBuffer viewed(ByteBuffer buffer) {
        String methodName = "viewedBuffer";


        Method[] methods = buffer.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("attachment")) {
                methodName = "attachment";
                break;
            }
        }

        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
        if (viewedBuffer == null)
            return buffer;
        else
            return viewed(viewedBuffer);
    }

    /*public static void clean(final Object buffer) throws Exception{
        AccessController.doPrivileged(new PrivilegedAction<Object>(){
            public Object run(){
                try{
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner",new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    sun.misc.Cleaner cleaner = (sun.misc.Cleaner)getCleanerMethod.invoke(buffer,new Object[0]);
                    cleaner.clean();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        });

    }*/

    public boolean isFull() {
        return this.fileSize == this.wrotePostion.get();
    }
    /**
     * @param dirName
     */
    public static void ensureDirOK(final String dirName) {
        if (dirName != null) {
            File f = new File(dirName);
            if (!f.exists()) {
                boolean result = f.mkdirs();
                System.out.println(dirName + " mkdir " + (result ? "OK" : "Failed"));
            }
        }
    }

    public AtomicInteger getReadPosition() {
        return readPosition;
    }

    public void setReadPosition(AtomicInteger readPosition) {
        this.readPosition = readPosition;
    }

    public MappedByteBuffer getMappedByteBuffer() {
        return mappedByteBuffer;
    }

    public void setMappedByteBuffer(MappedByteBuffer mappedByteBuffer) {
        this.mappedByteBuffer = mappedByteBuffer;
    }

    public FileChannel getFileChannel() {
        return fileChannel;
    }

    public void setFileChannel(FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getFileFormOffset() {
        return fileFormOffset;
    }

    public void setFileFormOffset(long fileFormOffset) {
        this.fileFormOffset = fileFormOffset;
    }

    public AtomicInteger getWrotePostion() {
        return wrotePostion;
    }

    public void setWrotePostion(AtomicInteger wrotePostion) {
        this.wrotePostion = wrotePostion;
    }

    public AtomicInteger getCommittedPosition() {
        return committedPosition;
    }

    public void setCommittedPosition(AtomicInteger committedPosition) {
        this.committedPosition = committedPosition;
    }
}
