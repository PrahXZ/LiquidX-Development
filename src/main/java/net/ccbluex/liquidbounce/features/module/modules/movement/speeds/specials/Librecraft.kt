package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.specials

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.exploit.ABlink
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.features.value.*
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.MovementUtils.strafe


class Librecraft : SpeedMode("Librecraft") {

    private val timerBoostValue = BoolValue("Librecraft-TimerBoost", false)
    private val timer1 = FloatValue("Timer-1", 2.2f,1f, 4f).displayable { timerBoostValue.get() }
    private val timer2 = FloatValue("Timer-2", 1.5f,1f, 2f).displayable { timerBoostValue.get() }
    fun onMotion() {}
    override fun onUpdate() {
        if (!mc.thePlayer.isInWeb && !mc.thePlayer.isInLava && !mc.thePlayer.isInWater && !mc.thePlayer.isOnLadder && mc.thePlayer.ridingEntity == null) {
                mc.gameSettings.keyBindJump.pressed = false
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump()
                    strafe(0.48f)
                    if (timerBoostValue.get()) {
                        LiquidBounce.moduleManager[ABlink::class.java]!!.pulseListValue.set("Custom")
                        LiquidBounce.moduleManager[ABlink::class.java]!!.pulseCustomDelayValue.set(100)
                        LiquidBounce.moduleManager[ABlink::class.java]!!.state = true
                        if(mc.thePlayer.ticksExisted % 25 < 10) {
                            mc.timer.timerSpeed = timer1.get()
                        } else {
                            mc.timer.timerSpeed = timer2.get()
                        }
                    }
                }
                strafe()
        }
    }


    override fun onEnable() {
        if (timerBoostValue.get()) {
            LiquidBounce.hud.addNotification(Notification("Speed", "Use version 1.10 onwards to bypass timer checks.", NotifyType.WARNING, 5000, 3000))
        }
    }
    override fun onDisable() {
        LiquidBounce.moduleManager[ABlink::class.java]!!.state = false
    }

    override fun onMove(event: MoveEvent) {}
}

