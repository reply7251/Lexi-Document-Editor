package me.sa_g6.utils;

import java.util.StringTokenizer;

public class StringUtils {
    public static int count(String str, String toCount){
        return str.split(toCount,-1).length-1;
        //return new StringTokenizer(" "+str+" ", toCount).countTokens()-1;
    }
}
