package net.mokai.quicksandrehydrated.client.compat;

import net.mokai.quicksandrehydrated.client.render.curios.CharmCurioRenderer;
import net.mokai.quicksandrehydrated.registry.ModItems;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public final class CuriosClientCompat {
    private CuriosClientCompat() {
    }

    public static void registerRenderers() {
        CuriosRendererRegistry.register(ModItems.HEAVY_CHARM.get(), CharmCurioRenderer::new);
        CuriosRendererRegistry.register(ModItems.LIGHT_CHARM.get(), CharmCurioRenderer::new);
    }
}

