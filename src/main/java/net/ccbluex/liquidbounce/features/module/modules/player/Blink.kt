// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.features.value.*
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.minecraft.network.INetHandler
import net.minecraft.network.Packet
import net.minecraft.network.handshake.client.C00Handshake
import net.minecraft.network.login.client.C00PacketLoginStart
import net.minecraft.network.login.server.S00PacketDisconnect
import net.minecraft.network.play.INetHandlerPlayClient
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.*
import net.minecraft.network.play.server.S29PacketSoundEffect
import net.minecraft.network.play.server.S3BPacketScoreboardObjective
import net.minecraft.network.play.server.S3EPacketTeams
import net.minecraft.network.status.client.C00PacketServerQuery
import net.minecraft.network.status.server.S00PacketServerInfo
import java.util.concurrent.LinkedBlockingQueue

@ModuleInfo(name = "Blink", category = ModuleCategory.PLAYER)
class Blink : Module() {

    private val inboundValue = BoolValue("Inbound", true)
    private val outboundValue = BoolValue("Outbound", false)
    private val pulseValue = BoolValue("Pulse", false)
    private val pulseDelayValue = IntegerValue("PulseDelay", 1000, 500, 5000).displayable { pulseValue.get() }

    private var disableLogger = false
    private val pulseTimer = MSTimer()
    private val clientPackets = LinkedBlockingQueue<Packet<INetHandlerPlayServer>>()
    private val serverPackets = LinkedBlockingQueue<Packet<INetHandlerPlayClient>>()

    override fun onEnable() {
        if (mc.thePlayer == null) return
        pulseTimer.reset()
    }

    override fun onDisable() {
        if (mc.thePlayer == null) return
        blink()
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (mc.thePlayer == null || disableLogger) return

        if (inboundValue.get()) {
            if(event.isServerSide()) return
            if(packet is C00PacketKeepAlive || packet is C00PacketLoginStart ||
                packet is C00PacketServerQuery || packet is C00Handshake) return
            event.cancelEvent()
            clientPackets.add(packet as Packet<INetHandlerPlayServer>);
        }

        if(outboundValue.get()) {
            if(!event.isServerSide()) return
            if(packet is S00PacketDisconnect || packet is S00PacketServerInfo || packet is S3EPacketTeams ||
                packet is S29PacketSoundEffect || packet is S3BPacketScoreboardObjective) return
            event.cancelEvent()
            serverPackets.add(packet as Packet<INetHandlerPlayClient>)
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (pulseValue.get() && pulseTimer.hasTimePassed(pulseDelayValue.get().toLong())) {
            blink()
            pulseTimer.reset()
        }
    }

    override val tag: String
        get() = (clientPackets.size + serverPackets.size).toString()

    private fun blink() {
        try {
            disableLogger = true

            while (!clientPackets.isEmpty()) {
                PacketUtils.sendPacketNoEvent(clientPackets.take() as Packet<INetHandlerPlayServer>)
            }

            while (!serverPackets.isEmpty()) {
                serverPackets.poll()?.processPacket(mc.netHandler)
            }

            disableLogger = false
        } catch (e: Exception) {
            e.printStackTrace()
            disableLogger = false
        }
    }
}
