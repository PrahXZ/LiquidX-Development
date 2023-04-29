
package net.ccbluex.liquidbounce.ui.client.gui

import net.ccbluex.liquidbounce.launch.data.modernui.mainmenu.LiquidXMainMenu
import net.minecraft.client.gui.*
import net.minecraft.client.settings.GameSettings
import net.minecraft.client.Minecraft;


class GuiMainMenu : GuiScreen(), GuiYesNoCallback {
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        fun isFastRenderEnabled(): Boolean {
            return try {
                val fastRender = GameSettings::class.java.getDeclaredField("ofFastRender")
                fastRender.getBoolean(Minecraft.getMinecraft().gameSettings)
            } catch (var1: Exception) {
                false
            }
        }
        if (isFastRenderEnabled()){
            mc.displayGuiScreen(LiquidXMainMenu())
        } else {
            mc.displayGuiScreen(LiquidXMainMenu())
        }
        drawBackground(1)
    }

}