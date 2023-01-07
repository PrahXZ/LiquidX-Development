// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.injection.forge.mixins.patcher.bugfixes;

import net.minecraft.client.renderer.BlockFluidRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BlockFluidRenderer.class)
public class MixinBlockFluidRenderer {
    @ModifyConstant(method = "renderFluid", constant = @Constant(floatValue = 0.001F))
    private float fixFluidStitching(float original) {
        return 0.0F;
    }
}
