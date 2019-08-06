package com.atguigu.gulimall.commons.utils;

public class ArrayToStringUtils {

    public static String getString(String[] arr, String sep) {
        String str = "";
        StringBuffer sb = new StringBuffer();
        if (arr != null && arr.length > 0) {
            for (String s : arr) {
                sb.append(s);
                sb.append(sep);
            }
            str = sb.toString().substring(0, sb.length() - 1);
        }

        return str;
    }

}
