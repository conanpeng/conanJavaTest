package com.conan.javaTest.test;

/**
 * @author huangjinsheng on 2017/2/22.
 */
public class Father {
    private volatile int looperThreadNum = 2;


    public void start(){
        startLoop();
    }

    private void startLoop(){
        System.out.println(looperThreadNum);
    }
}
