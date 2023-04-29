// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.features.value.*
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MathHelper
import org.lwjgl.input.Keyboard

@ModuleInfo(name = "TargetStrafe",  category = ModuleCategory.MOVEMENT)
class TargetStrafe : Module() {
    val straferange = FloatValue("Range", 2.0f, 0.1f, 4.0f) // esto es para saber a que distancia del jugador se calculara la direccion
    private val modeValue = ListValue("KeyMode", arrayOf("Jump", "None"), "Jump") // esto es para saber si se activara solo cuando apretes la tecla espacio (Jump) o siempre (None)
    private val safewalk = BoolValue("SafeWalk", true) // Esto es para que no te caigas al vacio
    private val mark = BoolValue("Mark", true) // Aca puedes hacer un circulo dependiendo de la distancia osea usando la primera variable y de ahi renderizar el circulo, a este puedes ponerle colores custom

    // Los colores del mark en orden
    private val colorRedValue = IntegerValue("R", 0, 0, 255).displayable { mark.get() }
    private val colorGreenValue = IntegerValue("G", 160, 0, 255).displayable { mark.get() }
    private val colorBlueValue = IntegerValue("B", 255, 0, 255).displayable { mark.get() }

}

