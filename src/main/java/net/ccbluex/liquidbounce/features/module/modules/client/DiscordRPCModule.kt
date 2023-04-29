// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.features.module.modules.client

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.special.rpcs.*
import net.ccbluex.liquidbounce.features.value.*

@ModuleInfo(name = "RichPresence", category = ModuleCategory.CLIENT,  defaultOn = true)
class DiscordRPCModule : Module() {

    val rpcsmode = ListValue("RPC-Mode", arrayOf("LiquidX", "Sigma5", "Rise6", "Intent", "Lunar", "Custom"), "LiquidX")


    // CustomRPC
    val customipc = TextValue("CustomIPC-ID", "854747806341136404").displayable { rpcsmode.equals("Custom") }
    val customlargeimage = TextValue("CustomLargeImage", "Example-Asset").displayable { rpcsmode.equals("Custom") }
    val customsmallimage = TextValue("CustomSmallImage", "Example-Asset").displayable { rpcsmode.equals("Custom") }
    val customdetails = TextValue("CustomDetails", "Java Client v1.2").displayable { rpcsmode.equals("Custom") }
    val customstate = TextValue("CustomState", "Playing Minecraft").displayable { rpcsmode.equals("Custom") }

    // Intent
    val intentclient = ListValue("Intent-Clients", arrayOf("Rise", "ZeroDay", "Tenacity", "Juul"), "Rise").displayable { rpcsmode.equals("Intent") }

    // LiquidX
    val showServerValue = BoolValue("ShowServer", true).displayable { rpcsmode.equals("LiquidX") }
    val showNameValue = BoolValue("ShowName", false).displayable { rpcsmode.equals("LiquidX") }
    val showHealthValue = BoolValue("ShowHealth", false).displayable { rpcsmode.equals("LiquidX") }
    val showOtherValue = BoolValue("ShowOther", false).displayable { rpcsmode.equals("LiquidX") }
    val animated = BoolValue("ShouldAnimate?", true).displayable { rpcsmode.equals("LiquidX") }

    override fun onEnable() {
        when (rpcsmode.get()) {
            "LiquidX" -> {
                LiquidXRPC.run()
            }

            "Sigma5" -> {
                SigmaRPC.run()
            }

            "Rise6" -> {
                Rise6RPC.run()
            }

            "Intent" -> {
                IntentRPC.run()
            }
            "Lunar" -> {
                LunarRPC.run()
            }
            "Custom" -> {
                CustomRPC.run()
            }
        }
    }


    override fun onDisable() {
        LiquidXRPC.stop()
        Rise6RPC.stop()
        IntentRPC.stop()
        SigmaRPC.stop()
        LunarRPC.stop()
        CustomRPC.stop()
    }


    override val tag: String
        get() = rpcsmode.get()
}