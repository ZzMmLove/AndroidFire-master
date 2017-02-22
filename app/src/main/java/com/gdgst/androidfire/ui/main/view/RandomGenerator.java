package com.gdgst.androidfire.ui.main.view;

/**
 * Created by Jenfei on 2016/12/10.
 */
import java.util.Random;

/**
 * 随机生成器
 */
public class RandomGenerator {
    private static final Random RANDOM = new Random();

    // 区间随机
    public float getRandom(float lower, float upper) {
        float min = Math.min(lower, upper);
        float max = Math.max(lower, upper);
        return getRandom(max - min) + min;
    }

    // 上界随机
    public float getRandom(float upper) {
        return RANDOM.nextFloat() * upper;
    }

    // 上界随机
    public int getRandom(int upper) {
        return RANDOM.nextInt(upper);
    }

}


