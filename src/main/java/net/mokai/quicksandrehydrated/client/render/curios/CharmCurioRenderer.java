package net.mokai.quicksandrehydrated.client.render.curios;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CharmCurioRenderer implements ICurioRenderer {
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext,
                                                                          PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent,
                                                                          MultiBufferSource buffer, int light, float limbSwing,
                                                                          float limbSwingAmount, float partialTicks, float ageInTicks,
                                                                          float netHeadYaw, float headPitch) {
        if (!(renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel)) {
            return;
        }

        poseStack.pushPose();
        @SuppressWarnings("unchecked")
        HumanoidModel<LivingEntity> bodyModel = (HumanoidModel<LivingEntity>) humanoidModel;
        ICurioRenderer.followBodyRotations(slotContext.entity(), bodyModel);
        bodyModel.body.translateAndRotate(poseStack);
        poseStack.translate(0.0F, 0.1F, -0.125F);
        poseStack.scale(0.2F, 0.2F, 0.5F);
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180.0F));

        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                light,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                slotContext.entity().level(),
                0
        );
        poseStack.popPose();
    }
}

