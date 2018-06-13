package com.hxhy.config.util;

import java.util.Random;
/**
 * 基于Twitter/snowflake 算法的分布式全局id生成器
 * 64位ID (42(毫秒)+10位（统一分配的业务号workerIdBits）+12(重复累加))
 *
 * 整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由datacenter和机器ID作区分），
 * 并且效率较高，经测试，snowflake每秒能够产生26万ID左右，完全满足需要。
 *
 * 以下代码均为twitter官方scala版本的一个改编版,算法核心无二
 */
public class SnowFlake {
    //当前起始时间戳
    private final static long twepoch = 1468685273491L;
    //机器码标识
    private static long workerId = 0L;
    //数据中心标识
    private static long datacenterId = 0L;
    //序列号
    private static long sequence = 0L;
    //机器码标识位数
    private static long workerIdBits = 5L;
    //数据中心标识位数
    private static long datacenterIdBits = 5L;
    //最大机器码标识
    private static long maxWorkerId = -1L ^ (-1L << workerIdBits);
    //最大数据中心标识
    private static long maxDataCenterId = -1L ^ (-1L << datacenterIdBits);
    //序列号位数
    private static long sequenceBits = 12L;
    //机器码位移数
    private static long workerIdShift = sequenceBits;
    //数据中心标识位移数
    private static long datacenterIdShift = workerIdBits + sequenceBits;
    //时间戳位移数
    private static long timestampShift = datacenterIdBits + workerIdBits + sequenceBits;
    //上次计算的时间戳
    private static long lastTimeStamp = -1L;
    //序列号掩码
    private static long sequenceMask = -1L ^ (-1L << sequenceBits);
    //构造函数
    public SnowFlake() {
        Random random = new Random();
        workerId = random.nextInt((int) maxWorkerId);
        datacenterId = random.nextInt((int) maxDataCenterId);
    }
    public SnowFlake(final long workerId, final long datacenterId) {
        if(workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("worker Id can't be greater than %d or less than 0");
        }
        if(datacenterId > maxDataCenterId || datacenterId < 0) {
            throw new IllegalArgumentException("datacenter Id can't be greater than %d or less than 0");
        }
        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }
    private long getWorkerId() {
        return this.workerId;
    }
    private long getDatacenterId() {
        return this.datacenterId;
    }
    private long getTimeStamp() {
        return System.currentTimeMillis();
    }
    public synchronized long nextId() {
        long timeStamp = timeGen();
        if(timeStamp < lastTimeStamp) {
            try {
                throw new Exception(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimeStamp - timeStamp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(timeStamp == lastTimeStamp) {
            sequence = (sequence + 1) & sequenceMask;
            if(sequence == 0) {
                timeStamp = tilNextMills();
            }
        }else {
            sequence = 0;
        }
        lastTimeStamp = timeStamp;
        long workId = ((timeStamp - twepoch) << timestampShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
        return workId;
    }
    /**
     * 等到下一秒
     * @return
     */
    private long tilNextMills() {
        long timeStamp = timeGen();
        do {
            timeStamp = timeGen();
        } while (timeStamp <= lastTimeStamp);
        return timeStamp;
    }
    /**
     * 生产时间戳
     * @return
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }
}

