// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.value.FloatValue

@ModuleInfo(name = "ItemPhysics", category = ModuleCategory.RENDER)
class ItemPhysics : Module() {
    val itemWeight = FloatValue("Weight", 0.5F, 0F, 1F)
    override val tag: String?
        get() = "${itemWeight.get()}"
}