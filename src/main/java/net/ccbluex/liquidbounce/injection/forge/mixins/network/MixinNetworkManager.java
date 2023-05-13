// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.injection.forge.mixins.network;

import io.netty.channel.*;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.modules.client.Animations;
import net.ccbluex.liquidbounce.features.module.modules.misc.SilentDisconnect;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {

    /**
     * show player head in tab bar
     */
    @Inject(method = "getIsencrypted", at = @At("HEAD"), cancellable = true)
    private void getIsencrypted(CallbackInfoReturnable<Boolean> cir) {
        if(Animations.INSTANCE.getFlagRenderTabOverlay()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
        if(PacketUtils.INSTANCE.getPacketType(packet) != PacketUtils.PacketType.SERVERSIDE)
            return;

        final PacketEvent event = new PacketEvent(packet, PacketEvent.Type.RECEIVE);
        LiquidBounce.eventManager.callEvent(event);

        if(event.isCancelled())
            callback.cancel();
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, CallbackInfo callback) {
        if(PacketUtils.INSTANCE.getPacketType(packet) != PacketUtils.PacketType.CLIENTSIDE)
            return;

        if(!PacketUtils.INSTANCE.handleSendPacket(packet)){
            final PacketEvent event = new PacketEvent(packet, PacketEvent.Type.SEND);
            LiquidBounce.eventManager.callEvent(event);

            if(event.isCancelled())
                callback.cancel();
        }
    }

    @Redirect(method = "checkDisconnected", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;)V"))
    public void checkDisconnectedLoggerWarn(Logger instance, String s) {
        if(!LiquidBounce.moduleManager.getModule(SilentDisconnect.class).getState()) {
            instance.warn(s); // it will spam "handleDisconnection() called twice" in console if SilentDisconnect is enabled
        }
    }
}