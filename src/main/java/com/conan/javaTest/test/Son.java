package com.conan.javaTest.test;

/**
 * @author huangjinsheng on 2017/2/22.
 */
public class Son extends Father{
    private volatile int looperThreadNum = 2;

    public Son(){

    }

    public Son(int looperThreadNum){
        this.looperThreadNum = looperThreadNum;
    }

}
