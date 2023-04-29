package net.ccbluex.liquidbounce.features.module.modules.movement.longjumps.specials

import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.longjumps.LongJumpMode
import net.ccbluex.liquidbounce.features.value.FloatValue
import net.ccbluex.liquidbounce.features.value.ListValue
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.minecraft.network.play.client.C03PacketPlayer
import net.ccbluex.liquidbounce.utils.PacketUtils


class VerusDmgLongJump : LongJumpMode("VerusDmg") {

    private val verusDmgModeValue = ListValue("VerusDmg-DamageMode", arrayOf("Instant", "InstantC06", "Jump"), "None")
    private val verusBoostValue = FloatValue("VerusDmg-Boost", 4.25f, 0f, 10f)
    private val verusHeightValue = FloatValue("VerusDmg-Height", 0.42f, 0f, 10f)
    private val verusTimerValue = FloatValue("VerusDmg-Timer", 1f, 0.05f, 10f)

    var verusDmged = false
    var verusJumpTimes = 0

    override fun onEnable() {
        verusDmged = false
        verusJumpTimes = 0

        if (verusDmgModeValue.get().equals("Instant", ignoreCase = true)) {
            if (mc.thePlayer.onGround && mc.theWorld.getCollidingBoundingBoxes(
                            mc.thePlayer,
                            mc.thePlayer.entityBoundingBox.offset(0.0, 4.0, 0.0).expand(0.0, 0.0, 0.0)
                    ).isEmpty()
            ) {
                PacketUtils.sendPacketNoEvent(
                        C03PacketPlayer.C04PacketPlayerPosition(
                                mc.thePlayer.posX,
                                mc.thePlayer.posY + 4,
                                mc.thePlayer.posZ,
                                false
                        )
                )
                PacketUtils.sendPacketNoEvent(
                        C03PacketPlayer.C04PacketPlayerPosition(
                                mc.thePlayer.posX,
                                mc.thePlayer.posY,
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
                mc.thePlayer.motionZ = 0.0
                mc.thePlayer.motionX = mc.thePlayer.motionZ
            }
        } else if (verusDmgModeValue.get().equals("InstantC06", ignoreCase = true)) {
            if (mc.thePlayer.onGround && mc.theWorld.getCollidingBoundingBoxes(
                            mc.thePlayer,
                            mc.thePlayer.entityBoundingBox.offset(0.0, 4.0, 0.0).expand(0.0, 0.0, 0.0)
                    ).isEmpty()
            ) {
                PacketUtils.sendPacketNoEvent(
                        C03PacketPlayer.C06PacketPlayerPosLook(
                                mc.thePlayer.posX,
                                mc.thePlayer.posY + 4,
                                mc.thePlayer.posZ,
                                mc.thePlayer.rotationYaw,
                                mc.thePlayer.rotationPitch,
                                false
                        )
                )
                PacketUtils.sendPacketNoEvent(
                        C03PacketPlayer.C06PacketPlayerPosLook(
                                mc.thePlayer.posX,
                                mc.thePlayer.posY,
                                mc.thePlayer.posZ,
                                mc.thePlayer.rotationYaw,
                                mc.thePlayer.rotationPitch,
                                false
                        )
                )
                PacketUtils.sendPacketNoEvent(
                        C03PacketPlayer.C06PacketPlayerPosLook(
                                mc.thePlayer.posX,
                                mc.thePlayer.posY,
                                mc.thePlayer.posZ,
                                mc.thePlayer.rotationYaw,
                                mc.thePlayer.rotationPitch,
                                true
                        )
                )
                mc.thePlayer.motionZ = 0.0
                mc.thePlayer.motionX = mc.thePlayer.motionZ
            }
        } else if (verusDmgModeValue.get().equals("Jump", ignoreCase = true)) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump()
                verusJumpTimes = 1
            }
        }
    }

    override fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer.hurtTime > 0 && !verusDmged) {
            verusDmged = true
            MovementUtils.strafe(verusBoostValue.get())
            mc.thePlayer.motionY = verusHeightValue.get().toDouble()
        }
        if (verusDmgModeValue.get().equals("Jump", ignoreCase = true) && verusJumpTimes < 5) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump()
                verusJumpTimes += 1
            }
            return
        }
        if (verusDmged) mc.timer.timerSpeed = verusTimerValue.get() else {
            mc.thePlayer.movementInput.moveForward = 0f
            mc.thePlayer.movementInput.moveStrafe = 0f
            if (!verusDmgModeValue.get().equals("Jump", ignoreCase = true)) mc.thePlayer.motionY = 0.0
        }
        return
    }

    override fun onMove(event: MoveEvent) {
        if (!verusDmged) event.zeroXZ()
    }

    override fun onPacket(event: PacketEvent) {

        val packet = event.packet
        if (packet is C03PacketPlayer) {
            if (verusDmgModeValue.get().equals("Jump", ignoreCase = true) && verusJumpTimes < 5) {
                packet.onGround = false
            }
        }
    }
}