package net.ccbluex.liquidbounce.file.configs

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.special.*
import net.ccbluex.liquidbounce.file.FileConfig
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.ui.client.GuiBackground
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import java.io.File
import java.net.Proxy

class SpecialConfig(file: File) : FileConfig(file) {
    var useGlyphFontRenderer = true

    override fun loadConfig(config: String) {
        val json = JsonParser().parse(config).asJsonObject

        LiquidBounce.commandManager.prefix = '.'
        AutoReconnect.delay = 5000
        AntiForge.enabled = true
        AntiForge.blockFML = true
        AntiForge.blockProxyPacket = true
        AntiForge.blockPayloadPackets = true
        GuiBackground.enabled = true
        GuiBackground.particles = false
        GuiAltManager.randomAltField.text = "%n%s%n%s%n%s%n%s%n%s"
        useGlyphFontRenderer = true

        if (json.has("prefix")) {
            LiquidBounce.commandManager.prefix = json.get("prefix").asCharacter
        }
        if (json.has("auto-reconnect")) {
            AutoReconnect.delay = json.get("auto-reconnect").asInt
        }
        if (json.has("alt-field")) {
            GuiAltManager.randomAltField.text = json.get("alt-field").asString
        }
        if (json.has("use-glyph-fontrenderer")) {
            useGlyphFontRenderer = json.get("use-glyph-fontrenderer").asBoolean
        }
        if (json.has("anti-forge")) {
            val jsonValue = json.getAsJsonObject("anti-forge")

            if (jsonValue.has("enable")) {
                AntiForge.enabled = jsonValue.get("enable").asBoolean
            }
            if (jsonValue.has("block-fml")) {
                AntiForge.blockFML = jsonValue.get("block-fml").asBoolean
            }
            if (jsonValue.has("block-proxy")) {
                AntiForge.blockProxyPacket = jsonValue.get("block-proxy").asBoolean
            }
            if (jsonValue.has("block-payload")) {
                AntiForge.blockPayloadPackets = jsonValue.get("block-payload").asBoolean
            }
        }

        if (json.has("background")) {
            val jsonValue = json.getAsJsonObject("background")

            if (jsonValue.has("enable")) {
                GuiBackground.enabled = jsonValue.get("enable").asBoolean
            }
            if (jsonValue.has("particles")) {
                GuiBackground.particles = jsonValue.get("particles").asBoolean
            }
            if (jsonValue.has("gradient")) {
                val name = jsonValue.get("gradient").asString
                GradientBackground.nowGradient = GradientBackground.gradients.find { it.name == name } ?: GradientBackground.gradients.first()
            }
            if (jsonValue.has("gradient-side")) {
                val side = jsonValue.get("gradient-side").asString
                GradientBackground.gradientSide = GradientBackground.gradientSides.find { it.name == side } ?: GradientBackground.gradientSides.first()
            }
            if (jsonValue.has("gradient-animated")) {
                GradientBackground.animated = jsonValue.get("gradient-animated").asBoolean
            }
        }
    }

    override fun saveConfig(): String {
        val json = JsonObject()

        json.addProperty("prefix", LiquidBounce.commandManager.prefix)
        json.addProperty("auto-reconnect", AutoReconnect.delay)
        json.addProperty("alt-field", GuiAltManager.randomAltField.text)
        json.addProperty("use-glyph-fontrenderer", useGlyphFontRenderer)

        val antiForgeJson = JsonObject()
        antiForgeJson.addProperty("enable", AntiForge.enabled)
        antiForgeJson.addProperty("block-fml", AntiForge.blockFML)
        antiForgeJson.addProperty("block-proxy", AntiForge.blockProxyPacket)
        antiForgeJson.addProperty("block-payload", AntiForge.blockPayloadPackets)
        json.add("anti-forge", antiForgeJson)

        val backgroundJson = JsonObject()
        backgroundJson.addProperty("enable", GuiBackground.enabled)
        backgroundJson.addProperty("particles", GuiBackground.particles)
        backgroundJson.addProperty("gradient", GradientBackground.nowGradient.name)
        backgroundJson.addProperty("gradient-side", GradientBackground.gradientSide.name)
        backgroundJson.addProperty("gradient-animated", GradientBackground.animated)
        json.add("background", backgroundJson)

        return FileManager.PRETTY_GSON.toJson(json)
    }
}