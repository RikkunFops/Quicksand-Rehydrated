package net.mokai.quicksandrehydrated.client.render.mob;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Slime;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;

public class SandBlobRenderer extends SlimeRenderer {

    private static final ResourceLocation TEX =
            new ResourceLocation(QuicksandRehydrated.MOD_ID, "textures/entity/sand_blob.png");

    public SandBlobRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(Slime entity) {
        return TEX;
    }
}
