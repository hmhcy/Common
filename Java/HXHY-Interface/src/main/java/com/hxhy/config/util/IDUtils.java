package com.hxhy.config.util;

import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;

public class IDUtils {
    public static String getPostfix(int num) {
        // 取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        // long millis = System.nanoTime();

        if (num > 0) {
            int seed = (int) (Math.pow(10, num) - 1);
            // 加上三位随机数
            Random random = new Random();
            int end = random.nextInt(seed);
            // 如果不足三位前面补0
            String str = millis + String.format("%0" + num + "d", end);
            return str;
        } else {
            return "" + millis;
        }
    }

    /**
     * 生成编号
     * 
     * @return
     */
    public static String genID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
