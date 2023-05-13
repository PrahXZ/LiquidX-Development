package net.ccbluex.liquidbounce.features.module.modules.movement.flys.other

import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.modules.movement.flys.FlyMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.network.play.server.S12PacketEntityVelocity

class UniversoFly : FlyMode("Universocraft") {

    var movespeed = 0.0
    var damaged = false

    var stage = 0
    var hops = 0
    var ticks = 0

    override fun onEnable() {
        stage = -1
        hops = 0
        ticks = 0
        damaged = false
    }

    override fun onDisable() {
        MovementUtils.resetMotion(true)
    }

    override fun onMove(event: MoveEvent) {
        when (stage) {
            0, 1 -> {
                event.zeroXZ()
            }
            2 -> {
                MovementUtils.setMotion(movespeed)
            }
        }
    }

    override fun onUpdate(event: UpdateEvent) {
        when (stage) {
            -1 -> {
                stage = 0
            }
            0 -> {
                if(hops >= 4 && mc.thePlayer.onGround) {
                    stage++
                    return
                }

                if(mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = 0.42f.toDouble()
                    hops++
                }
            }
            1 -> {
                if(mc.thePlayer.hurtTime > 0) {
                    ticks = 0
                    movespeed = 0.525
                    stage++
                    mc.thePlayer.motionY = 0.42f.toDouble()
                }
            }
            2 -> {
                val pos = mc.thePlayer.position.add(0.0, -1.25, 0.0)
                PacketUtils.sendPacketNoEvent(
                    C08PacketPlayerBlockPlacement(pos, 1,
                        ItemStack(Blocks.stone.getItem(mc.theWorld, pos)), 0.0F, 0.5F + Math.random().toFloat() * 0.44.toFloat(), 0.0F)
                )

                //if(mc.thePlayer.motionY < 0) mc.thePlayer.motionY = -0.033

                if (ticks == 0) {
                    movespeed *= 3
                }

                movespeed -= movespeed / 159.0

                ticks++
            }
        }
    }

    override fun onPacket(event: PacketEvent) {
        if(event.packet is C03PacketPlayer) {
            if(stage == 0 && hops >= 1) {
                if(mc.thePlayer.onGround) {
                    event.packet.onGround = false
                }
            }
        }
        if(event.packet is S12PacketEntityVelocity) {
            if (mc.thePlayer == null || (mc.theWorld?.getEntityByID(event.packet.entityID) ?: return) != mc.thePlayer) {
                return
            }
            damaged = true
        }
    }

}