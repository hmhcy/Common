package com.hxhy.config.util;

public class RandomUtils {

    private static String[] allCharArray = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
            "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z" };

    private static String[] allAlphaArray = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    private static String[] onlyLowerAlphaArray = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

    private static String[] onlyUpperAlphaArray = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    private static String[] onlyNumberArray = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

    public static String randomNum(int length) {
        return random(length, onlyNumberArray);
    }

    public static String randomAllChar(int length) {
        return random(length, allCharArray);
    }

    public static String randonAlpha(int length) {
        return random(length, allAlphaArray);
    }

    public static String randonLowerAlpha(int length) {
        return random(length, onlyLowerAlphaArray);
    }

    public static String randonUpperAlpha(int length) {
        return random(length, onlyUpperAlphaArray);
    }

    private static String random(int length, String[] actionArray) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int index = org.apache.commons.lang3.RandomUtils.nextInt(0, actionArray.length);
            if(i==0 && actionArray[index].equals("0")) {
            	index = org.apache.commons.lang3.RandomUtils.nextInt(0, actionArray.length);
            }
            ret.append(actionArray[index]);
        }
        
        return ret.toString();
    }

}