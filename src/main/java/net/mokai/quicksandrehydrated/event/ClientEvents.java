package net.mokai.quicksandrehydrated.event;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.client.render.ModRenderTypes;
import net.mokai.quicksandrehydrated.client.render.StruggleHudOverlay;
import net.mokai.quicksandrehydrated.client.render.coverage.CoverageAtlasHolder;
import net.mokai.quicksandrehydrated.client.render.coverage.CoverageLayer;
import net.mokai.quicksandrehydrated.client.render.coverage.PlayerCoverageDefaultModel;
import net.mokai.quicksandrehydrated.client.render.coverage.PlayerCoverageSlimModel;
import net.mokai.quicksandrehydrated.entity.playerStruggling;
import net.mokai.quicksandrehydrated.networking.ModMessages;
import net.mokai.quicksandrehydrated.networking.packet.StruggleDownC2SPacket;
import net.mokai.quicksandrehydrated.networking.packet.StruggleReleaseC2SPacket;
import net.mokai.quicksandrehydrated.registry.ModModelLayers;
import net.mokai.quicksandrehydrated.util.Keybinding;

import java.io.IOException;

public class ClientEvents {

    @EventBusSubscriber(modid = QuicksandRehydrated.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {

            if (Keybinding.STRUGGLE_KEY.isDown()) {
                ((playerStruggling) Minecraft.getInstance().player).BeginStruggle();
            } else {
                System.out.println("Struggle key not held");
            }

        }

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Pre event) {

            Player player = event.getEntity();
            playerStruggling strugglingPlayer = (playerStruggling) player;

            if (player.level().isClientSide()) {

                boolean keyDown = Keybinding.STRUGGLE_KEY.isDown();
                boolean flagHolding = strugglingPlayer.getHoldingStruggle();

                if (keyDown && !flagHolding) {
                    // key IS down this tick, flagHolding is NOT
                    ModMessages.sendToServer(new StruggleDownC2SPacket());
                } else if (!keyDown && flagHolding) {
                    // key is NOT DOWN this tick, just released
                    ModMessages.sendToServer(new StruggleReleaseC2SPacket());
                }

                strugglingPlayer.setHoldingStruggle(keyDown);

            }

        }

    }

    @EventBusSubscriber(modid = QuicksandRehydrated.MOD_ID, value = Dist.CLIENT)
    public static class ClientModBusEvents {


        @SubscribeEvent
        public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(new CoverageAtlasHolder(Minecraft.getInstance().getTextureManager()));
        }

        @SubscribeEvent
        public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions e) {
            e.registerLayerDefinition(ModModelLayers.COVERAGE_LAYER_DEFAULT, PlayerCoverageDefaultModel::createBodyLayer);
            e.registerLayerDefinition(ModModelLayers.COVERAGE_LAYER_SLIM, PlayerCoverageSlimModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void addLayers(EntityRenderersEvent.AddLayers e) {
            System.out.println("registering coverage layers");

            var renderer = e.getRenderer(net.minecraft.world.entity.EntityType.PLAYER);
            PlayerRenderer pr = (PlayerRenderer) (net.minecraft.client.renderer.entity.EntityRenderer<?>) renderer;
            if (pr != null) {
                var model = pr.getModel();
                if (model instanceof PlayerModel<?> playerModel) {
                    try {
                        java.lang.reflect.Field slimField = PlayerModel.class.getDeclaredField("slim");
                        slimField.setAccessible(true);
                        boolean isSlim = (boolean) slimField.get(playerModel);
                        pr.addLayer(new CoverageLayer(pr, isSlim));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }



        @SubscribeEvent
        public static void shaderRegistry(RegisterShadersEvent event) throws IOException
        {
            // Adds a shader to the list, the callback runs when loading is complete.
            event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID,"rendertype_coverage"), DefaultVertexFormat.NEW_ENTITY), (thang) -> {
                ModRenderTypes.CustomRenderTypes.coverageShader = thang;
            });
        }

        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            // Register quicksand bubble particles
            // NOTE: ModParticles and QuicksandBubbleParticle must be implemented
            // event.registerSpriteSet(ModParticles.QUICKSAND_BUBBLE_PARTICLES.get(),
            //        QuicksandBubbleParticle.Provider::new);
            // Washing particles removed
        }

    }
}