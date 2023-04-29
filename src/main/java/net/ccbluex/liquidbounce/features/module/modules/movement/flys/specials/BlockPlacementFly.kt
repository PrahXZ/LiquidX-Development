package net.ccbluex.liquidbounce.features.module.modules.movement.flys.specials

import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.flys.FlyMode
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.utils.Rotation
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.util.BlockPos

class BlockPlacementFly : FlyMode("BlockPlacement") {

    override fun onUpdate(event: UpdateEvent) {
        mc.thePlayer.onGround = true
        mc.thePlayer.motionY = 0.0
        PacketUtils.sendPacketNoEvent(
                C08PacketPlayerBlockPlacement(
                        BlockPos.ORIGIN,
                        255,
                        mc.thePlayer.heldItem,
                        0F,
                        0F,
                        0F
                )
        )
        RotationUtils.setTargetRotation(
                RotationUtils.limitAngleChange(
                        RotationUtils.serverRotation!!,
                        Rotation(mc.thePlayer.rotationYaw - 180, 90F),
                        RandomUtils.nextFloat(120F, 160F)
                )
        )
    }

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is C03PacketPlayer) {
            packet.onGround = true
        }

    }

}