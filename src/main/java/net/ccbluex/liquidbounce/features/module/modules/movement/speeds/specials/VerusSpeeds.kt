package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.specials

import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.features.value.*
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.minecraft.network.play.client.C03PacketPlayer

class VerusSpeeds: SpeedMode("Verus") {

    private val modeValue = ListValue("Verus-Mode", arrayOf("Hop", "Float", "Ground", "YPort"), "Hop")
    private val YPortspeedValue = FloatValue("YPortSpeed", 0.61f, 0.1f, 1f).displayable { modeValue.equals("YPort") }

    // Variables
    private var firstHop = false

    private var ticks = 0
    private var baipas = false
    private var IsinAir = false

    override fun onEnable() {
         baipas = false
    }


    override fun onUpdate() {
        when (modeValue.get()) {
            "Hop" -> {
                if (MovementUtils.isMoving()) {
                    mc.gameSettings.keyBindJump.pressed = false
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump()
                        MovementUtils.strafe(0.48f)

                    }
                    MovementUtils.strafe()
                }
            }
        }
    }

    override fun onPreMotion() {
        when (modeValue.get()) {
            "Ground" -> {
                if (mc.thePlayer.onGround)
                    if (modeValue.equals("Ground")) {
                        if (mc.thePlayer.ticksExisted % 12 == 0) {
                        firstHop = false
                        MovementUtils.strafe(0.69f)
                        mc.thePlayer.jump()
                        mc.thePlayer.motionY = 0.0
                        MovementUtils.strafe(0.69f)
                        mc.netHandler.addToSendQueue(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, false))
                        MovementUtils.strafe(0.41f)
                        mc.netHandler.addToSendQueue(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    } else if (!firstHop) {
                        MovementUtils.strafe(1.01f)
                    }
                }
            }
            "Float" -> {
                ticks++
                if (!mc.gameSettings.keyBindJump.isKeyDown) {
                    if (mc.thePlayer.onGround) {
                        ticks = 0
                        MovementUtils.strafe(0.44f)
                        mc.thePlayer.motionY = 0.42
                        mc.timer.timerSpeed = 2.1f
                        IsinAir = true
                    } else if (IsinAir) {
                        if (ticks >= 10) {
                             baipas = true
                            MovementUtils.strafe(0.2865f)
                            IsinAir = false
                        }

                        if (baipas) {
                            if (ticks <= 1) {
                                MovementUtils.strafe(0.45f)
                            }

                            if (ticks >= 2) {
                                MovementUtils.strafe(0.69f - (ticks - 2) * 0.019f)
                            }
                        }

                        mc.thePlayer.motionY = 0.0
                        mc.timer.timerSpeed = 0.9f

                        mc.thePlayer.onGround = true
                    }
                }
            }
        }
    }

    override fun onMove(event: MoveEvent) {
        when (modeValue.get()) {
            "YPort" -> {
                if (MovementUtils.isMoving()) {
                    mc.gameSettings.keyBindJump.pressed = false
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump()
                        mc.thePlayer.motionY = 0.0
                        MovementUtils.strafe(YPortspeedValue.get())
                        event.y = 0.41999998688698
                    } else {
                        MovementUtils.strafe()
                    }
                }
            }
        }
    }
}