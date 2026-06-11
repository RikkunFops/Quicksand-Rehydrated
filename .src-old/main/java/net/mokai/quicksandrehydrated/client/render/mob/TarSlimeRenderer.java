package net.mokai.quicksandrehydrated.client.render.mob;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.minecraft.world.entity.monster.Slime;

public class TarSlimeRenderer extends SlimeRenderer {
    private static final ResourceLocation TEX =
            new ResourceLocation(QuicksandRehydrated.MOD_ID, "textures/entity/tar_slime.png");

    public TarSlimeRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(Slime entity) {
        return TEX;
    }
}
