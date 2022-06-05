package com.kadima.utils;

public class mybatisUtil {

    public static boolean isEmpty(Object o){
        if (o == null){
            return true;
        }
        if (o instanceof String){
            if (((String) o).trim().length() == 0) {
                return true;
            }
        }

        return false;

    }
}
