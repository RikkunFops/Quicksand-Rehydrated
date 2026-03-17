package net.mokai.quicksandrehydrated.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

public class BubbleModel<T extends Entity> extends EntityModel<T> {
    private static final String BONE = "bone";

    public final ModelPart root;
    private final ModelPart bone;

    public BubbleModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.bone = root.getChild(BONE);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(BONE, CubeListBuilder.create()
                        .texOffs(3, 3).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
                        .texOffs(4, 4).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(2, 2).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 12.0F, new CubeDeformation(0.0F))
                        .texOffs(4, 4).addBox(-6.0F, -4.0F, -4.0F, 12.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    public static LayerDefinition create() {
        return createBodyLayer();
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int packedColor) {
        this.bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
    }
}