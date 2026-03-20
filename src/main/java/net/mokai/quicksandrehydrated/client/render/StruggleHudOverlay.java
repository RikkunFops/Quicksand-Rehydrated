package net.mokai.quicksandrehydrated.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.entity.entityQuicksandVar;
import net.mokai.quicksandrehydrated.entity.playerStruggling;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = QuicksandRehydrated.MOD_ID)
public class StruggleHudOverlay {

    private static final ResourceLocation FILLED_STRUGGLE = ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "textures/gui/struggle_meter_full.png");
    private static final ResourceLocation EMPTY_STRUGGLE = ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "textures/gui/struggle_meter_empty.png");

    @SubscribeEvent
    static void renderStruggleMeter(RenderGuiLayerEvent.Post event) {
        Player p = Minecraft.getInstance().player;
        if (p == null || !(p instanceof entityQuicksandVar es) || !(p instanceof playerStruggling strugglingPlayer)) {
            return;
        }

        if (!es.getInQuicksand()) {
            return;
        }

        GuiGraphics guiGraphics = event.getGuiGraphics();
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();

        int bar_x = (width / 2) - 91;
        int bar_y = height - 29;

        int bar_w = 182;
        int bar_h = 5;

        guiGraphics.blit(EMPTY_STRUGGLE, bar_x, bar_y, 0, 0, bar_w, bar_h, bar_w, bar_h);

        float percent = (float) strugglingPlayer.getStruggleHold() / 20.0f;
        int pixels_wide = (int) (182 * percent);

        if (pixels_wide > 182) {
            pixels_wide = 182;
        }

        guiGraphics.blit(FILLED_STRUGGLE, bar_x, bar_y, 0, 0, pixels_wide, bar_h, bar_w, bar_h);
    }
}
