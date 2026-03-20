package net.mokai.quicksandrehydrated.client.render.coverage;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.entity.coverage.CoverageEntry;
import net.mokai.quicksandrehydrated.entity.coverage.PlayerCoverage;
import net.mokai.quicksandrehydrated.entity.playerStruggling;
import net.mokai.quicksandrehydrated.registry.ModModelLayers;

import java.util.ArrayList;
import java.util.List;

public class CoverageLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    PlayerCoverageDefaultModel coverageModel;
    private final DynamicTexture texture;
    TextureManager textureManager;
    ResourceLocation resourcelocation;

    public CoverageLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer, boolean pSlim) {
        super(pRenderer);

        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        if (!pSlim) {
            this.coverageModel = new PlayerCoverageDefaultModel(modelSet.bakeLayer(ModModelLayers.COVERAGE_LAYER_DEFAULT));
        } else {
            this.coverageModel = new PlayerCoverageSlimModel(modelSet.bakeLayer(ModModelLayers.COVERAGE_LAYER_SLIM));
        }

        this.texture = new DynamicTexture(64, 64, true);
        this.textureManager = Minecraft.getInstance().textureManager;
        this.resourcelocation = Minecraft.getInstance().textureManager.register("coverage", this.texture);

    }

    private void updateTexture(PlayerCoverage coverage) {

        // Step one: make array with 32 values.
        // Each value points to a TextureAtlasSprite,
        // which is the texture that should be applied at that depth.
        TextureAtlasSprite[] coverageByPixel = new TextureAtlasSprite[32];

        for (CoverageEntry entry : coverage.coverageEntries) {
            // both begin and end are inclusive
            for (int i = entry.begin; i <= entry.end; i++) {

                try {
                    coverageByPixel[i] = CoverageAtlasHolder.singleton.get(entry.texture);
                } catch (Exception e) {
                    // If texture can't be loaded, skip this entry (draws empty)
                    continue;
                }

            }
        }



        // Step two: go through every pixel and determine it's depth
        // get the texture that should be applied at that depth.
        // if there is no texture to be applied, it sets to the pixel to 0 alpha

        TextureAtlasSprite depthMask = CoverageAtlasHolder.singleton.get(new ResourceLocation(QuicksandRehydrated.MOD_ID, "coverage_mask"));
        NativeImage img = this.texture.getPixels();

        if (img == null) return;

        for (int i = 0; i < 64; ++i) {
            for (int k = 0; k < 64; ++k) {

                // get color from mask
                int depthRGBA = depthMask.getPixelRGBA(0, i, k);

                // then it's depth as a float (0.0 to 1.0)
                float depthFloat = (float) FastColor.ARGB32.alpha(depthRGBA) / 255.0F;

                // then scale it (0.0 to 31.0)
                int depthIndex = (int) (depthFloat*31.0); // floor it?? FLOOR IT!!

                // floor to int; that is 0 to 31
                depthIndex = Math.max(0, Math.min(31, depthIndex));

                // get the texture that should be here
                TextureAtlasSprite coverageTexture = coverageByPixel[depthIndex];

                // do not do anything if its null
                if (coverageTexture == null)  {
                    int emptyColor = FastColor.ARGB32.color(0, 0, 0, 0);
                    img.setPixelRGBA(i, k, emptyColor);
                    continue;
                }

                // get color, and set color
                int color_rgba = coverageTexture.getPixelRGBA(0, i, k);
                img.setPixelRGBA(i, k, color_rgba);

            }
        }

        this.texture.upload();

    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, AbstractClientPlayer pAbstractPlayer, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        try {
            playerStruggling playerqs = (playerStruggling) pAbstractPlayer;
            
            // Check if player has coverage
            PlayerCoverage pC = playerqs.getCoverage();
            if (pC == null || pC.coverageEntries.isEmpty()) {
                return; // No coverage to render
            }
            
            // Update texture if needed
            if (pC.renderUpdate) {
                pC.renderUpdate = false;
                this.updateTexture(pC);
            }
            
            // Get the appropriate model
            PlayerCoverageDefaultModel model = this.coverageModel;
            
            // Copy properties from parent model
            this.getParentModel().copyPropertiesTo(model);
            model.prepareMobModel(pAbstractPlayer, pLimbSwing, pLimbSwingAmount, pPartialTick);
            model.setupAnim(pAbstractPlayer, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            
            // Ensure visibility of all parts, including all second layers
            model.hat.visible = true;
            model.leftLeg.visible = true;
            model.rightLeg.visible = true;
            model.leftArm.visible = true;
            model.rightArm.visible = true;
            model.body.visible = true;
            model.head.visible = true;
            
            // Use a translucent render type for better blending
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucentCull(this.resourcelocation));
            
            // Render the coverage with full opacity
            model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, 
                LivingEntityRenderer.getOverlayCoords(pAbstractPlayer, 0.0F), 
                1.0F, 1.0F, 1.0F, 1.0F);
            
            // Debug: uncomment to print information about the coverage
            /*
            if (!pC.coverageEntries.isEmpty()) {
                System.out.println("Rendering coverage for player: " + pAbstractPlayer.getName().getString());
                System.out.println("Coverage entries: " + pC.coverageEntries.size());
                for (CoverageEntry entry : pC.coverageEntries) {
                    System.out.println("Entry: begin=" + entry.begin + ", end=" + entry.end +
                                      ", texture=" + entry.texture);
                }
            }
            */
        } catch (Exception e) {
            // Silently catch any exceptions to prevent rendering crashes
            // In a production environment, you might want to log this
            // e.printStackTrace(); // Uncomment for debugging
        }
    }

}
