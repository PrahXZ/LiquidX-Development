package net.ccbluex.liquidbounce.utils.timer;


import net.ccbluex.liquidbounce.utils.math.MathUtils;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;

public final class TimeUtils {

    public static long randomDelay(final int minDelay, final int maxDelay) {
        return RandomUtils.nextInt(minDelay, maxDelay);
    }

    public static long randomClickDelay(final int minCPS, final int maxCPS) {
        return (long) ((Math.random() * (1000 / minCPS - 1000 / maxCPS) * 1.03 + MathUtils.randomNumber(3, 1)) + 1050 / maxCPS);
    }
}