// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils

class LegitSpeed : SpeedMode("Legit") {
    override fun onPreMotion() {
        if (mc.thePlayer.isInWater) return
        mc.gameSettings.keyBindJump.pressed = false
        if (MovementUtils.isMoving()) {
            if (mc.thePlayer.onGround) mc.thePlayer.jump()
        }
    }
}
