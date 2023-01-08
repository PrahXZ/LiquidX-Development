// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils

class SlowHopSpeed : SpeedMode("SlowHop") {
    override fun onPreMotion() {
        if (mc.thePlayer.isInWater) return
        if (MovementUtils.isMoving()) {
            if (mc.thePlayer.onGround) mc.thePlayer.jump() else MovementUtils.strafe(MovementUtils.getSpeed() * 1.011f)
        } else {
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
        }
    }
}