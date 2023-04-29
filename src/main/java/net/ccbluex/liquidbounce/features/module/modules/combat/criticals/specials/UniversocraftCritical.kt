package net.ccbluex.liquidbounce.features.module.modules.combat.criticals.specials

import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.modules.combat.criticals.CriticalMode
import net.minecraft.network.play.client.C03PacketPlayer

class UniversocraftCritical : CriticalMode("Universocraft") {

    private var CombatStatus = false
    override fun onEnable() {
    }

    override fun onAttack(event: AttackEvent) {
        CombatStatus = true
    }

    override fun onPacket(event: PacketEvent) {
        if(event.packet is C03PacketPlayer) {
                if (CombatStatus) {
                    event.packet.onGround = false
                    CombatStatus = false
                } else {
                event.packet.onGround = false
            }
        }
    }
}