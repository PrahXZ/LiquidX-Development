// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils

class AACYPortSpeed : SpeedMode("AACYPort") {
    override fun onPreMotion() {
        if (MovementUtils.isMoving() && !mc.thePlayer.isSneaking) {
            mc.thePlayer.cameraPitch = 0f
            if (mc.thePlayer.onGround) {
                mc.thePlayer.motionY = 0.3425
                mc.thePlayer.motionX *= 1.5893
                mc.thePlayer.motionZ *= 1.5893
            } else mc.thePlayer.motionY = -0.19
        }
    }
}