// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.ui.client.gui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.gui.clickgui.ClickGui;
import net.ccbluex.liquidbounce.ui.client.gui.clickgui.style.styles.*;
import net.ccbluex.liquidbounce.ui.client.gui.clickgui.style.styles.CSGO.LightClickGUI;
import net.ccbluex.liquidbounce.ui.client.gui.clickgui.style.styles.LBPlus.NewUi;
import net.ccbluex.liquidbounce.ui.client.gui.options.modernuiLaunchOption;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.features.value.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "ClickGUI", category = ModuleCategory.CLIENT, keyBind = Keyboard.KEY_RSHIFT, canEnable = false)
public class ClickGUIModule extends Module {
    public ListValue styleValue = new ListValue("Style", new String[]{"CSGO", "Astolfo", "LB+", "LiquidBounce", "Glow", "Null", "Slowly"}, "LiquidBounce") {
        @Override
        protected void onChanged(final String oldValue, final String newValue) {
            updateStyle();
        }
    };


    public static final BoolValue backback = new BoolValue("Background Accent",true);

    public static final ListValue scrollMode = new ListValue("Scroll Mode", new String[]{"Screen Height", "Value"},"Value");

    public static final ListValue colormode = new ListValue("Setting Accent", new String[]{"White", "Color"},"Color");
    public static final IntegerValue clickHeight = new IntegerValue("Tab Height", 250, 100, 500);
    public final FloatValue scaleValue = new FloatValue("Scale", 0.70F, 0.7F, 2F);
    public final IntegerValue maxElementsValue = new IntegerValue("MaxElements", 15, 1, 20);
    public final ListValue backgroundValue = new ListValue("Background", new String[] {"Default", "Gradient", "None"}, "None");

    public final ListValue animationValue = new ListValue("Animation", new String[] {"Bread", "Slide", "LiquidBounce", "Zoom", "Ziul", "None"}, "Ziul");
    public static final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    public static final IntegerValue colorRedValue = (IntegerValue) new IntegerValue("R", 0, 0, 255).displayable(() -> !colorRainbow.get());
    public static final IntegerValue colorGreenValue = (IntegerValue) new IntegerValue("G", 160, 0, 255).displayable(() -> !colorRainbow.get());
    public static final IntegerValue colorBlueValue = (IntegerValue) new IntegerValue("B", 255, 0, 255).displayable(() -> !colorRainbow.get());
    public static final BoolValue fastRenderValue = new BoolValue("FastRender", false);
    public final BoolValue getClosePrevious = (BoolValue) new BoolValue("ClosePrevious",false);


    public static Color generateColor() {
        return colorRainbow.get() ? ColorUtils.INSTANCE.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }
    @Override
    public void onEnable() {

        if (styleValue.get().contains("CSGO")) {
            mc.displayGuiScreen(new LightClickGUI());
            this.setState(false);
        }  else if (styleValue.get().equalsIgnoreCase("LB+")){
            mc.displayGuiScreen(NewUi.getInstance());
        } else {
            updateStyle();
            mc.displayGuiScreen(modernuiLaunchOption.clickGui);
        }

    }

    private void updateStyle() {
        switch (styleValue.get().toLowerCase()) {
            case "liquidbounce":
                modernuiLaunchOption.clickGui.style = new LiquidBounceStyle();
                break;
            case "null":
                modernuiLaunchOption.clickGui.style = new NullStyle();
                break;
            case "slowly":
                modernuiLaunchOption.clickGui.style = new SlowlyStyle();
                break;
            case "glow":
                modernuiLaunchOption.clickGui.style = new GlowStyle();
                LiquidBounce.hud.addNotification(new Notification("ClickGUI", "can cause lag problems", NotifyType.WARNING,4000,500));
                break;
            case "astolfo":
                modernuiLaunchOption.clickGui.style = new AstolfoStyle();
                break;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onPacket(final PacketEvent event) {
        final Packet packet = event.getPacket();

        if (packet instanceof S2EPacketCloseWindow && mc.currentScreen instanceof ClickGui) {
            event.cancelEvent();
        }
    }
}
