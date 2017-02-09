package com.conan.javaTest.a;

/**
 * @author huangjinsheng on 2017/2/9.
 */
public class QuickSort extends AbstractSortService {

    public static void main(String[] args) {
        int[] arr = new int[]{3, 9, 6, 5, 2, 4, 8};
        quickSort(arr, 0, arr.length - 1);
        printResult(arr);
    }

    /**
     * @param arr
     * @param low
     * @param high
     * @return
     */
    public static void quickSort(int[] arr, int low, int high) {
        if (arr == null || arr.length <= 1 || high <= low) {
            return;
        }
        int middle = partition2(arr, low, high);
        quickSort(arr, low, middle - 1);
        quickSort(arr, middle + 1, high);

    }

    private static int partition(int[] arr ,int low,int high){
        int middle = arr[low];
        while (high > low) {
            while (high > low && arr[low] < middle) {
                low++;
            }

            while (high > low && arr[high] > middle) {
                high--;
            }

            if (high > low) {
                swap(arr, low, high);
            }
        }

        arr[low] = middle;
        return low;
    }

    /**
     * @param arr
     * @param low
     * @param high
     * @return
     */
    private static int partition2(int[] arr, int low, int high) {
        int middle = arr[low];
        int i = low ;
        int j = high;
        while (j > i) {
            while (arr[i] < middle) {
                i++;
            }
            while (arr[j] > middle) {
                j--;
            }
            if (j > i) {
                swap(arr, i, j);
            }else {
                break;
            }
        }
        arr[i] = middle;
        return i;
    }
}
