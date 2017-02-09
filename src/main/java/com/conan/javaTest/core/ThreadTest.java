package com.conan.javaTest.core;

/**
 * @author huangjinsheng on 2017/2/7.
 */
public class ThreadTest {

    public static void main(String[] args){
        ThreadTest threadTest = new ThreadTest();
        threadTest.test1();
    }


    public void test1(){
        final TestObject testObject = new TestObject();
        new Thread(new Runnable() {
            @Override
            public void run() {
                testObject.methodA();
            }
        }).start();

        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                testObject.methodB();
            }
        }).start();
    }
}
