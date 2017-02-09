package com.conan.javaTest.a;

/**
 * @author huangjinsheng on 2017/2/7.
 */
public class StringSearch {


    public static void main(String[] args) {
        System.out.println(nativeSearch("sdfsklngsdfgsgnsgs", "fg")); //10
        System.out.println(nativeSearch("fg", "fg")); //0
        System.out.println(nativeSearch("fs", "fg")); //-1
        System.out.println(nativeSearch("f", "fg")); //-1
        System.out.println(nativeSearch("", "")); //0
        System.out.println(nativeSearch(null, "")); //-1
        System.out.println(nativeSearch("", null)); //-1

//        String str = "fggss";
//        String searchStr = "fs";
//        System.out.println(indexOf(str.toCharArray(), 0, str.length(),
//                searchStr.toCharArray(), 0, searchStr.length(), 0));

        System.out.println(maxSubSum(new int[]{-1,-3,0,-5,3,1}));
    }


    /**
     * 最大子串和，动态规划问题
     * @param array
     * @return
     */
    public static int maxSubSum(int[] array){
        int sum = 0, max = array[0];
        for(int i = 0; i < array.length; i++){
            sum += array[i];
            if(sum > max)
                max = sum;
            if(sum < 0)  //如果 sum < 0, 将 sum 重新置 0
                sum = 0;
        }
        return max;
    }


    /**
     * 模拟 string.indexOf
     * 暴力检索
     * @param str
     * @param searchStr
     * @return
     */
    public static int nativeSearch(String str, String searchStr) {
        if (str == null || searchStr == null ) {
            return -1;
        }

        if(searchStr.length() == 0){
            return 0;
        }

        char first = searchStr.charAt(0);
        int max = str.length() - searchStr.length();

        for (int i = 0; i <= max; i++) {
            //找第一个匹配的字符
            if (str.charAt(i) != first) {
                while (++i < max && str.charAt(i) != first) ;
            }

            //找到第一个匹配的字符后，比较后续的字符
            if (i <= max) {//如果第一个匹配的字符后续的字符长度<查找的字符串长度就不需要继续了
                int j = i + 1;
                int end = j + searchStr.length() - 1;
                for (int k = 1; j < end && str.charAt(j) == searchStr.charAt(k); j++, k++) ;

                if (j == end) { //找到完全匹配
                    return i;
                }
            }
        }
        return -1;
    }
}
