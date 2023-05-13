package net.ccbluex.liquidbounce.features.module.modules.movement.flys.other

import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.modules.movement.flys.FlyMode
import net.ccbluex.liquidbounce.features.value.*
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.network.play.client.C00PacketKeepAlive
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C0CPacketInput
import net.minecraft.network.play.server.S12PacketEntityVelocity
import java.sql.Wrapper

class UniversoFly : FlyMode("Universocraft") {

    var movespeed = 1.6f
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
            0 -> {
                event.zeroXZ()
            }
            1, 2 -> {
                MovementUtils.strafe(movespeed)
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
                if(mc.thePlayer.hurtTime > 0 && damaged) {
                    ticks = 0
                    stage++
                    mc.thePlayer.motionY = 0.42f.toDouble()
                }
            }
            2 -> {

                var baseSpeed = mc.thePlayer.capabilities.getWalkSpeed() * 2.873
                if (movespeed > baseSpeed) {
                    movespeed -= (movespeed / 159.0).toFloat()
                    movespeed = Math.max(baseSpeed, movespeed.toDouble()).toFloat()
                }

                if (ticks == 0) {
                    movespeed *= 2f
                } else {
                    if(mc.thePlayer.motionY < 0) mc.thePlayer.motionY = -0.033
                }

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