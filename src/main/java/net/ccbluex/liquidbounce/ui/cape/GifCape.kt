// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.ui.cape

import at.dhyan.open_imaging.GifDecoder
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.util.ResourceLocation
import java.io.InputStream

class GifCape(name: String, imageIS: InputStream) : DynamicCape(name) {

    init {
        val gif = GifDecoder.read(imageIS)
        imageIS.close()

        var delay = 0
        for(i in 0 until gif.frameCount) {
            frames.add(gif.getFrame(i))

            delay += gif.getDelay(i) * 10
            delays.add(delay)
        }

        playTime = delay

        val mc = Minecraft.getMinecraft()
        frames.forEachIndexed { index, image ->
            mc.textureManager.loadTexture(ResourceLocation(path + index), DynamicTexture(image))
        }
    }
}