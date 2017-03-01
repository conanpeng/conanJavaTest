package com.conan.javaTest.test;

import org.apache.commons.lang3.StringUtils;

/**
 * @author huangjinsheng on 2017/2/21.
 */
public class Tester {

    public static void main(String[] args){

        Tester t = new Tester();
        t.test1();
    }


    public void test2(){
    }

    public void test1(){
        Father f = new Father();
        System.out.print("father......");
        f.start();

        Son s1 = new Son();
        System.out.print("son1......");
        s1.start();

        Son s2 = new Son(25);
        System.out.print("son2......");
        s2.start();
    }
}
