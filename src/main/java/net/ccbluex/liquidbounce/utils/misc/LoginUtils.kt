// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.utils.misc

import me.liuli.elixir.account.CrackedAccount
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.SessionEvent
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.minecraft.util.Session

object LoginUtils : MinecraftInstance() {
    fun loginCracked(username: String) {
        mc.session = CrackedAccount().also { it.name = username }.session.let { Session(it.username, it.uuid, it.token, it.type) }
        LiquidBounce.eventManager.callEvent(SessionEvent())
    }

    fun randomCracked() {
        var name = GuiAltManager.randomAltField.text

        while (name.contains("%n") || name.contains("%s")) {
            if (name.contains("%n")) {
                name = name.replaceFirst("%n", RandomUtils.nextInt(0, 9).toString())
            }

            if (name.contains("%s")) {
                name = name.replaceFirst("%s", RandomUtils.randomString(1))
            }
        }

        loginCracked(name)
    }
}