package net.ccbluex.liquidbounce.features.module.modules.movement.flys.specials

import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.flys.FlyMode
import net.ccbluex.liquidbounce.features.value.FloatValue
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C0FPacketConfirmTransaction
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import org.lwjgl.input.Keyboard
import java.util.*
import kotlin.math.round
import kotlin.math.sqrt


class DesyncFly : FlyMode("Desync") {

    val speed = FloatValue("Desync-Speed", 1f, 0.1f, 10f)
    val vspeed = FloatValue("Desync-VSpeed", 1f, 0.1f, 10f)

    val packetLol = LinkedList<C0FPacketConfirmTransaction>()
    fun isInventory(action: Short): Boolean = action > 0 && action < 100


    override fun onEnable() {
        packetLol.clear()
    }

    override fun onDisable() {
        packetLol.clear()
    }

    override fun onWorld(event: WorldEvent) {
        packetLol.clear()

    }


    override fun onUpdate(event: UpdateEvent) {
        mc.thePlayer.noClip = true
        mc.thePlayer.onGround = false

        mc.thePlayer.capabilities.isFlying = false
        mc.thePlayer.motionX = 0.0
        mc.thePlayer.motionY = 0.0
        mc.thePlayer.motionZ = 0.0

        if (mc.gameSettings.keyBindJump.isKeyDown) {
            mc.thePlayer.motionY += vspeed.get().toDouble()
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            mc.thePlayer.motionY -= vspeed.get().toDouble()
            mc.gameSettings.keyBindSneak.pressed = false
        }
        MovementUtils.strafe(speed.get())

        if (mc.thePlayer.ticksExisted % 180 == 0) {
            while (packetLol.size > 22) {
                PacketUtils.sendPacketNoEvent(packetLol.poll())
            }
        }
    }

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is C03PacketPlayer) {
            val yPos = round(mc.thePlayer.posY / 0.015625) * 0.015625
            mc.thePlayer.setPosition(mc.thePlayer.posX, yPos, mc.thePlayer.posZ)

            if (mc.thePlayer.ticksExisted % 45 == 0) {
                PacketUtils.sendPacketNoEvent(
                        C03PacketPlayer.C04PacketPlayerPosition(
                                mc.thePlayer.posX,
                                mc.thePlayer.posY,
                                mc.thePlayer.posZ,
                                true
                        )
                )
                PacketUtils.sendPacketNoEvent(
                        C03PacketPlayer.C04PacketPlayerPosition(
                                mc.thePlayer.posX,
                                mc.thePlayer.posY - 11.725,
                                mc.thePlayer.posZ,
                                false
                        )
                )
                PacketUtils.sendPacketNoEvent(
                        C03PacketPlayer.C04PacketPlayerPosition(
                                mc.thePlayer.posX,
                                mc.thePlayer.posY,
                                mc.thePlayer.posZ,
                                true
                        )
                )
            }
        }
            if (packet is S08PacketPlayerPosLook) {
                if (mc.thePlayer == null || mc.thePlayer.ticksExisted <= 0) return

                var x = packet.getX() - mc.thePlayer.posX
                var y = packet.getY() - mc.thePlayer.posY
                var z = packet.getZ() - mc.thePlayer.posZ
                var diff = sqrt(x * x + y * y + z * z)
                if (diff <= 8) {
                    event.cancelEvent()
                    PacketUtils.sendPacketNoEvent(
                            C03PacketPlayer.C06PacketPlayerPosLook(
                                    packet.getX(),
                                    packet.getY(),
                                    packet.getZ(),
                                    packet.getYaw(),
                                    packet.getPitch(),
                                    true
                            )
                    )
                }
            }
            if (packet is C0FPacketConfirmTransaction && !isInventory(packet.uid)) {
                repeat(4) {
                    packetLol.add(packet)
                }
                event.cancelEvent()
            }
    }
}