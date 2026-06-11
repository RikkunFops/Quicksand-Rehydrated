package net.mokai.quicksandrehydrated.client.render.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.client.render.ModModelLayers;
import net.mokai.quicksandrehydrated.entity.EntityCaveBlob;

public class CaveBlobRenderer extends MobRenderer<EntityCaveBlob, CaveBlobModel<EntityCaveBlob>> {

    private static final ResourceLocation TEX =
            new ResourceLocation(QuicksandRehydrated.MOD_ID, "textures/entity/cave_blob.png");

    private static final Axis FLIP_AXIS = Axis.XP;
    private static final float FLIP_DEGREES = 180f;


    private static final float SHELL_Y_OFFSET = 1.50f;
    private static final float SHELL_SCALE    = 1.005f;

    private final CaveBlobModel<EntityCaveBlob> outerModel;

    public CaveBlobRenderer(EntityRendererProvider.Context ctx,
                            CaveBlobModel<EntityCaveBlob> innerModel) {
        super(ctx, innerModel, 0.6f);
        this.outerModel = new CaveBlobModel<>(ctx.bakeLayer(ModModelLayers.CAVE_BLOB_CLEAR_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCaveBlob entity) {
        return TEX;
    }

    @Override
    public void render(EntityCaveBlob entity, float yaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);

        float ageInTicks = entity.tickCount + partialTicks;
        outerModel.setupAnim(entity, 0f, 0f, ageInTicks, 0f, 0f);

        poseStack.pushPose();

        poseStack.translate(0.0F, SHELL_Y_OFFSET, 0.0F);
        poseStack.mulPose(FLIP_AXIS.rotationDegrees(FLIP_DEGREES));
        poseStack.scale(SHELL_SCALE, SHELL_SCALE, SHELL_SCALE);

        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(TEX));
        outerModel.renderToBuffer(
                poseStack, vc, packedLight, getOverlayCoords(entity, 0.0F),
                1F, 1F, 1F, 0.58F
        );

        poseStack.popPose();
    }
}
