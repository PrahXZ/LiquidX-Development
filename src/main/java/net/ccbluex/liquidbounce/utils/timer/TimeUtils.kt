// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.utils.timer

import net.ccbluex.liquidbounce.utils.misc.RandomUtils

object TimeUtils {
    fun randomDelay(minDelay: Int, maxDelay: Int): Long {
        val baseDelay = RandomUtils.nextInt(minDelay, maxDelay).toLong()
        val randomOffset = RandomUtils.nextInt(0, 20).toLong()
        return baseDelay + randomOffset
    }
    fun randomClickDelay(minCPS: Int, maxCPS: Int): Long {
        val baseDelay = (Math.random() * (1000 / minCPS - 1000 / maxCPS + 1) + 1000 / maxCPS).toLong()
        val randomOffset = RandomUtils.nextInt(0, 20).toLong()
        return baseDelay + randomOffset
    }
}