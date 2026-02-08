package net.mokai.quicksandrehydrated;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.mokai.quicksandrehydrated.entity.coverage.WashingSystem;
import net.mokai.quicksandrehydrated.loot.ModLootModifiers;
import net.mokai.quicksandrehydrated.networking.ModMessages;
import net.mokai.quicksandrehydrated.registry.*;
import net.mokai.quicksandrehydrated.screen.MixerScreen;
import net.mokai.quicksandrehydrated.screen.ModMenuTypes;
import net.mokai.quicksandrehydrated.util.Tristate;
import net.mokai.quicksandrehydrated.worldgen.ModRegion;
import net.mokai.quicksandrehydrated.worldgen.ModSurfaceRuleData;
import net.mokai.quicksandrehydrated.worldgen.placement.ModPlacementModifierTypes;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;


import static net.mokai.quicksandrehydrated.util.ModTags.Blocks.QUICKSAND_DROWNABLE;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(QuicksandRehydrated.MOD_ID)
public class QuicksandRehydrated {

    public static final String MOD_ID = "qsrehydrated";

    public QuicksandRehydrated() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        QuicksandRegistry.register(modEventBus);
        ModFluids.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModParticles.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeModeTab.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModPlacementModifierTypes.register(modEventBus);


        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        //modEventBus.addListener(this::addCreative);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();
            // Register world generation for quicksand pits
            ModFeatures.registerWorldGeneration();
            Regions.register(new ModRegion(new ResourceLocation(MOD_ID, "overworld_1"), 2));
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModSurfaceRuleData.makeRules());
        });
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            //ItemBlockRenderTypes.setRenderLayer(ModFluids.DRY_QUICKSAND.get(), RenderType.solid());
            MenuScreens.register(ModMenuTypes.MIXER_MENU.get(), MixerScreen::new);

        }
    }

    private static Tristate entityCanBreatheWithOffset(LivingEntity entity, double upwardsYOffset) {
        // Screen goes dark when the eye position is ~0.05 units above the quicksand block, so we adjust drowning behavior to match
        // Found this value by experimentation and logging, couldn't actually find the code that handles the screen going dark, so this is an educated guess
        // If anyone gets a better measurement or finds a definitive source for this value please update it
        double cameraYOffset = -0.05;
        Vec3 position = entity.getEyePosition().relative(Direction.UP, upwardsYOffset + cameraYOffset);
        BlockPos blockPosition = BlockPos.containing(position);
        BlockState blockState = entity.level().getBlockState(blockPosition);
        if (blockState.isAir()) {
            return Tristate.YES;
        }
        // Previously also had an "instanceof QuicksandBase" check, but I'm not sure if we need that
        if (blockState.is(QUICKSAND_DROWNABLE)) {
            // Quicksand blocks have an offset from the default height of 1, which at the moment only applies to collisions, and doesn't affect the block visually.
            // I've intentionally ignored the offset here since drowning should start when the (first-person) camera visually goes under.
            return Tristate.NO;
        }
        FluidState fluidState = blockState.getFluidState();
        if (fluidState.getFluidType().canDrownIn(entity)) {
            // Trying to match minecraft's behavior, but there seems to be a mismatch for water height
            // Apparently need to add a small amount to the fluid's height to compensate?
            // Notably, minecraft says you start drowning *before* the camera actually goes under.
            // Measured this out while testing with Water (both source blocks and flowing water at all heights)
            // Haven't tested if this impacts other fluids
            // If anyone gets a better measurement or finds a definitive source for this value please update it
            double liquidHeightOffset = 0.05;
            // System.out.printf("Fluid Height: %f %f\n", position.y - blockPosition.getY(), fluidState.getOwnHeight());
            return (position.y < blockPosition.getY() + fluidState.getOwnHeight() + liquidHeightOffset) ? Tristate.NO : Tristate.YES;
        }
        // Some other situation, assume that minecraft's built in drowning and any other mods will handle it
        // TODO: Non-solid "breathable" blocks like signs will hit this point, should work on handling this in case the snorkel is sitting in one
        return Tristate.MAYBE;
    }

    private static boolean entityIsHoldingReed(LivingEntity entity) {
        Iterable<ItemStack> handitems = entity.getHandSlots();
        for (ItemStack item : handitems) {
            if (item.getItem().equals(ModItems.BREATHING_REED.get())) {
                return true;
            }
        }
        return false;
    }

    private static boolean entityIsWearingSnorkel(LivingEntity entity) {
        Iterable<ItemStack> armorItems = entity.getArmorSlots();
        for (ItemStack item : armorItems) {
            // TODO: Check if this accounts for baubles. Also, add Baubles compatibility in the first place-
            if (item.getItem().equals(ModItems.SNORKEL_MASK.get())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set entity to drown if their breathing point is in quicksand. This position is usually the eye position, but
     * the Snorkel and Breathing Reed increase this by +1.5 and +2 blocks respectively.
     */
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class GameModEvents {
        @SubscribeEvent
        public static void onLivingBreatheEvent(LivingBreatheEvent event) {
            LivingEntity entity = event.getEntity();

            double reedHeight = 2;
            double snorkelHeight = 1.5; // SET TO BE CONFIGURABLE

            Tristate canBreathNormally = entityCanBreatheWithOffset(entity, 0);
            boolean canBreatheThroughReed = entityIsHoldingReed(entity) && entityCanBreatheWithOffset(entity, reedHeight) == Tristate.YES;
            boolean canBreatheThroughSnokel = entityIsWearingSnorkel(entity) && entityCanBreatheWithOffset(entity, snorkelHeight) == Tristate.YES;

            // if (entity instanceof Player) {
            //     double eyesAboveBlock = entity.getEyeY() - Math.floor(entity.getEyeY());
            //     System.out.printf(
            //         "onLivingBreatheEvent: event.canBreathe: %b, event.canRefillAir: %b, canBreathNormally: %s, canBreatheThroughReed: %b, canBreatheThroughSnokel: %b, eyesAboveBlock: %f\n",
            //         event.canBreathe(),
            //         event.canRefillAir(),
            //         canBreathNormally.name(),
            //         canBreatheThroughReed,
            //         canBreatheThroughSnokel,
            //         eyesAboveBlock
            //     );
            // }

            if (canBreatheThroughReed || canBreatheThroughSnokel || canBreathNormally == Tristate.YES) {
                // If we're certain we have a source of air, we can breathe
                event.setCanBreathe(true);
                event.setCanRefillAir(true);
            } else if (canBreathNormally == Tristate.NO) {
                // We're definitely suffocating, mrrrrhf~
                event.setCanBreathe(false);
                event.setCanRefillAir(false);
            }

            // canBreathNormally is MAYBE
            // Got a situation we're not sure about, don't modify the event at all and assume something else handles it
        }
        
        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            // Process at the end of the tick
            if (event.phase == TickEvent.Phase.END) {
                Player player = event.player;
                Level level = player.level();
                
                // Apply washing effect if player is in water
                // This needs to run on both client and server sides
                // - Server side: for actual coverage removal logic
                // - Client side: for particle effects
                WashingSystem.applyWashingEffect(player, level);
            }
        }

        // Let's temporarily comment out this event to see if it is the cause of the problem.
        /*
        @SubscribeEvent
        public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
            // Your custom logic goes here
            LivingEntity entity = event.getEntity();

            if (entity.level().isClientSide()) {
                System.out.print("[C] ");
            }
            else {
                System.out.print("[S] ");
            }
            System.out.println(entity.getName() + " jumped!");

            // Check whether the entity is actually on a block of quicksand.
            // Use the current position of the entity instead of getOnPosLegacy()
            BlockPos entityPos = entity.blockPosition().below();
            BlockState blockState = entity.level().getBlockState(entityPos);

            if (blockState.getBlock() instanceof QuicksandBase) {
                QuicksandBase quicksand = (QuicksandBase) blockState.getBlock();
                quicksand.sinkableJumpOff(blockState, entity.level(), entityPos, entity);
            }
        }
        */

    }






}
