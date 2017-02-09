package com.conan.javaTest.a;

/**
 * @author huangjinsheng on 2017/2/9.
 */
public class AbstractSortService {

    public static final void swap(int[] arr ,int i, int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static final void printResult(int[] arr){
        for(int i : arr){
            System.out.print(i+" ");
        }
    }
}
