package net.ccbluex.liquidbounce.features.module.modules.movement.flys.specials

import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.flys.FlyMode
import net.ccbluex.liquidbounce.features.value.FloatValue
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.utils.WorldUtils
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.util.BlockPos
import org.lwjgl.input.Keyboard

class BufferAbuseFly : FlyMode("BufferAbuse") {

    val speed = FloatValue("BufferAbuse-Speed", 1f, 0.1f, 10f)
    val vspeed = FloatValue("BufferAbuse-VSpeed", 1f, 0.1f, 10f)

    override fun onUpdate(event: UpdateEvent) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            mc.gameSettings.keyBindSneak.pressed = false
        }
    }

    override fun onMotion(event: MotionEvent) {
        if (WorldUtils.getBlockBellow() != BlockPos.ORIGIN && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            mc.thePlayer.setPosition(mc.thePlayer.posX, WorldUtils.getBlockBellow().y.toDouble(), mc.thePlayer.posZ)
        }
        if (mc.thePlayer.movementInput.jump)
            mc.thePlayer.motionY = vspeed.get().toDouble()
        else mc.thePlayer.motionY = 0.0
        MovementUtils.strafe(speed.get())
    }

    override fun onDisable() {

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            mc.gameSettings.keyBindSneak.pressed = true
        }

        PacketUtils.sendPacketNoEvent(
                C03PacketPlayer.C06PacketPlayerPosLook(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY - WorldUtils.distanceToGround(),
                        mc.thePlayer.posZ,
                        -1f,
                        0f,
                        false
                )
        )
    }

    override fun onPacket(event: PacketEvent) {
        if (event.packet is C03PacketPlayer) {
            event.cancelEvent()
        }
    }
}