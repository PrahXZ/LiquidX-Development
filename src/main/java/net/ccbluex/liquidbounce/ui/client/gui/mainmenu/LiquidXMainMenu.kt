package net.ccbluex.liquidbounce.launch.data.modernui.mainmenu
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.font.FontLoaders
import net.ccbluex.liquidbounce.ui.client.GuiBackground
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.minecraft.client.gui.*
import net.minecraft.client.resources.I18n
import net.minecraftforge.fml.client.GuiModList
import java.awt.Color

class LiquidXMainMenu : GuiScreen(), GuiYesNoCallback {
    override fun initGui() {
        val height = (this.height / 3.5).toInt()
        this.buttonList.add(GuiButton(1, this.width / 2 - 50, height, 100, 24, I18n.format("menu.singleplayer")))
        this.buttonList.add(GuiButton(2, this.width / 2 - 50, height + 30, 100, 24, I18n.format("menu.multiplayer")))
        this.buttonList.add(GuiButton(3, this.width / 2 - 50, height + 30 * 2, 100, 24, "%ui.altmanager%"))
        this.buttonList.add(GuiButton(4, this.width / 2 - 50, height + 30 * 3, 100, 24, "%ui.mods%"))
        this.buttonList.add(GuiButton(5, this.width / 2 - 50, height + 30 * 4, 100, 24, "%ui.background%"))
        this.buttonList.add(GuiButton(6, this.width / 2 - 50, height + 30 * 5, 100, 24, I18n.format("menu.options")))
        this.buttonList.add(GuiButton(7, this.width / 2 - 50, height + 30 * 6, 100, 24, I18n.format("menu.quit")))

        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val height = (this.height / 3.5).toInt()
        drawBackground(0)
        FontLoaders.JELLO40.drawCenteredString(LiquidBounce.CLIENT_NAME, (width / 2).toDouble(), (height - 60).toDouble(), Color.BLACK.rgb)
        FontLoaders.JELLO20.drawCenteredString("Made by vPrah and Crypt", (width / 2).toDouble(), (height - 30).toDouble(), Color.BLACK.rgb)
        FontLoaders.JELLO20.drawCenteredString("To all our users with much love", (width / 2).toDouble(), (height - 18).toDouble(), Color.BLACK.rgb)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            1 -> mc.displayGuiScreen(GuiSelectWorld(this))
            2 -> mc.displayGuiScreen(GuiMultiplayer(this))
            3 -> mc.displayGuiScreen(GuiAltManager(this))
            4 -> mc.displayGuiScreen(GuiModList(this))
            6 -> mc.displayGuiScreen(GuiOptions(this, mc.gameSettings))
            5 -> mc.displayGuiScreen(GuiBackground(this))
            7 -> mc.shutdown()
        }
    }
    override fun keyTyped(typedChar: Char, keyCode: Int) {}
}