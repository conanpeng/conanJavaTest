package com.conan.javaTest.core;

/**
 * @author huangjinsheng on 2017/2/7.
 */
public class TestObject {

    public synchronized void methodA(){
        System.out.println("aaaaaa start");

        try {
            Thread.sleep(5000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("aaaaaa end!");
    }


    public void methodB(){
        System.out.println("bbbbbb start");

        try {
            Thread.sleep(5000l);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("bbbbbb end!");
    }
}
