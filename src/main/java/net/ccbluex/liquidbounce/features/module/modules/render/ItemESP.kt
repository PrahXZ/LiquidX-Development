// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.render.ColorUtils.rainbow
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.shader.shaders.GlowShader
import net.ccbluex.liquidbounce.utils.render.shader.shaders.OutlineShader
import net.ccbluex.liquidbounce.features.value.*
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.util.AxisAlignedBB
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.roundToInt

@ModuleInfo(name = "ItemESP", category = ModuleCategory.RENDER)
class ItemESP : Module() {
    private val modeValue = ListValue("Mode", arrayOf("Box", "OtherBox", "Outline", "LightBox", "ShaderOutline", "ShaderGlow"), "Box")
    private val outlineWidth = FloatValue("Outline-Width", 3f, 0.5f, 5f).displayable { modeValue.equals("Outline") }
    private val colorRainbowValue = BoolValue("Rainbow", true)
    private val colorRedValue = IntegerValue("R", 0, 0, 255).displayable { !colorRainbowValue.get() }
    private val colorGreenValue = IntegerValue("G", 255, 0, 255).displayable { !colorRainbowValue.get() }
    private val colorBlueValue = IntegerValue("B", 0, 0, 255).displayable { !colorRainbowValue.get() }

    private fun getColor(): Color {
        return if (colorRainbowValue.get()) rainbow() else Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get())
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent?) {
        val color = getColor()
        for (entity in mc.theWorld.loadedEntityList) {
            if (!(entity is EntityItem || entity is EntityArrow)) continue
            when (modeValue.get().lowercase()) {
                "box" -> RenderUtils.drawEntityBox(entity, color, true, true, outlineWidth.get())
                "otherbox" -> RenderUtils.drawEntityBox(entity, color, false, true, outlineWidth.get())
                "outline" -> RenderUtils.drawEntityBox(entity, color, true, false, outlineWidth.get())
            }
        }

        if (modeValue.equals("LightBox")) {
            for (o in mc.theWorld.loadedEntityList) {
                if (o !is EntityItem) continue
                val item = o
                val x = item.posX - mc.renderManager.renderPosX
                val y = item.posY + 0.5 - mc.renderManager.renderPosY
                val z = item.posZ - mc.renderManager.renderPosZ
                GL11.glEnable(3042)
                GL11.glLineWidth(2.0f)
                GL11.glColor4f(1f, 1f, 1f, .75f)
                GL11.glDisable(3553)
                GL11.glDisable(2929)
                GL11.glDepthMask(false)
                RenderUtils.drawOutlinedBoundingBox(AxisAlignedBB(x - .2, y - 0.3, z - .2, x + .2, y - 0.4, z + .2))
                GL11.glColor4f(1f, 1f, 1f, 0.15f)
                RenderUtils.drawBoundingBox(AxisAlignedBB(x - .2, y - 0.3, z - .2, x + .2, y - 0.4, z + .2))
                GL11.glEnable(3553)
                GL11.glEnable(2929)
                GL11.glDepthMask(true)
                GL11.glDisable(3042)
            }
        }
    }

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        if (modeValue.equals("ShaderOutline")) {
            OutlineShader.OUTLINE_SHADER.startDraw(event.partialTicks)
            try {
                for (entity in mc.theWorld.loadedEntityList) {
                    if (!(entity is EntityItem || entity is EntityArrow)) continue
                    mc.renderManager.renderEntityStatic(entity, event.partialTicks, true)
                }
            } catch (ex: Exception) {
                alert("An error occurred while rendering all item entities for shader esp")
            }
            OutlineShader.OUTLINE_SHADER.stopDraw(
                if (colorRainbowValue.get()) rainbow() else Color(
                    colorRedValue.get(),
                    colorGreenValue.get(),
                    colorBlueValue.get()
                ), 1f, 1f
            )
        }

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        val shader = (if (modeValue.equals("shaderoutline")) OutlineShader.OUTLINE_SHADER else if (modeValue.equals("shaderglow")) GlowShader.GLOW_SHADER else null)
            ?: return
        val partialTicks = event.partialTicks

        shader.startDraw(partialTicks)

        for (entity in mc.theWorld.loadedEntityList) {
            if (!(entity is EntityItem || entity is EntityArrow)) continue
            mc.renderManager.renderEntityStatic(entity, event.partialTicks, true)
        }

        shader.stopDraw(getColor(), outlineWidth.get(), 1f)
    }
    }

    fun getIncremental(`val`: Double, inc: Double): Double {
        val one = 1.0 / inc
        return (`val` * one).roundToInt() / one
    }
}