package net.ccbluex.liquidbounce.features.module.modules.movement.flys.aac

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.flys.FlyMode
import net.ccbluex.liquidbounce.features.value.*
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.util.AxisAlignedBB
import kotlin.math.cos
import kotlin.math.sin

class AACFly : FlyMode("AACFly") {

    private var flys = ListValue("AACFly-Mode", arrayOf("AAC1.9.10", "AAC3.0.5", "AAC5.2.0"), "AAC1.9.10")

    // Val
    private val speedAAC1910Value = FloatValue("AAC1.9.10-Speed", 0.3f, 0.2f, 1.7f).displayable { flys.equals("AAC1.9.10") }
    private val fastAAC305Value = BoolValue("AAC3.0.5-Fast", true).displayable { flys.equals("AAC.3.0.5") }



    // Var
    private var aacJump = 0.0
    private var delay = 0

    private val packets = mutableListOf<C03PacketPlayer>()
    private val timer = MSTimer()
    private var nextFlag = false
    private var flyClip = false
    private var flyStart = false


    override fun onEnable() {
        aacJump = -3.8

        when (flys.get()) {
            "AAC5.2.0" -> {
                if (mc.isSingleplayer) {
                    LiquidBounce.hud.addNotification(Notification("Fly", "Use AAC5.2.0 Fly will crash single player", NotifyType.ERROR, 2000, 500))
                    fly.state = false
                    return
                }
                MovementUtils.resetMotion(true)
                PacketUtils.sendPacketNoEvent(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.7976931348623157E+308, mc.thePlayer.posZ, true))
            }
        }
    }

    override fun onUpdate(event: UpdateEvent) {
        when (flys.get()) {
            "AAC1.9.10" -> {
                if (mc.gameSettings.keyBindJump.isKeyDown) aacJump += 0.2

                if (mc.gameSettings.keyBindSneak.isKeyDown) aacJump -= 0.2

                if (fly.launchY + aacJump > mc.thePlayer.posY) {
                    mc.netHandler.addToSendQueue(C03PacketPlayer(true))
                    mc.thePlayer.motionY = 0.8
                    MovementUtils.strafe(speedAAC1910Value.get())
                }

                MovementUtils.strafe()
            }
            "AAC3.0.5" -> {
                if (delay == 2) {
                    mc.thePlayer.motionY = 0.1
                } else if (delay > 2) {
                    delay = 0
                }

                if (fastAAC305Value.get()) {
                    if (mc.thePlayer.movementInput.moveStrafe.toDouble() == 0.0) mc.thePlayer.jumpMovementFactor =
                            0.08f else mc.thePlayer.jumpMovementFactor = 0f
                }

                delay++
            }
            "AAC5.2.0" -> {
                mc.thePlayer.motionY = 0.003
                MovementUtils.resetMotion(false)
            }
        }
    }

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet

        when (flys.get()) {
            "AAC5.2.0" -> {
                if (packet is S08PacketPlayerPosLook) {
                    event.cancelEvent()
                    mc.thePlayer.setPosition(packet.x, packet.y, packet.z)
                    PacketUtils.sendPacketNoEvent(C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, packet.yaw, packet.pitch, false))
                    val dist = 0.14
                    val yaw = Math.toRadians(mc.thePlayer.rotationYaw.toDouble())
                    mc.thePlayer.setPosition(mc.thePlayer.posX + -sin(yaw) * dist, mc.thePlayer.posY, mc.thePlayer.posZ + cos(yaw) * dist)
                    PacketUtils.sendPacketNoEvent(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
                    PacketUtils.sendPacketNoEvent(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.7976931348623157E+308, mc.thePlayer.posZ, true))
                } else if (packet is C03PacketPlayer) {
                    event.cancelEvent()
                }
            }
        }
    }

}