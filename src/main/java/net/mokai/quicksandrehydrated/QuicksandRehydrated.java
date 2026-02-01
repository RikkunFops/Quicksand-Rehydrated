package net.mokai.quicksandrehydrated;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
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
import net.mokai.quicksandrehydrated.block.quicksands.core.QuicksandBase;
import net.mokai.quicksandrehydrated.entity.coverage.WashingSystem;
import net.mokai.quicksandrehydrated.loot.ModLootModifiers;
import net.mokai.quicksandrehydrated.networking.ModMessages;
import net.mokai.quicksandrehydrated.registry.*;
import net.mokai.quicksandrehydrated.screen.MixerScreen;
import net.mokai.quicksandrehydrated.screen.ModMenuTypes;
import net.mokai.quicksandrehydrated.worldgen.placement.ModPlacementModifierTypes;

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


    public static boolean IsInQuicksandSpotCheck(LivingEntity entity, double offset) { // TODO: This should probably be in a helper class, not in the main mod definition.
        Vec3 breathingPos = entity.getEyePosition();
        BlockPos breathingBlockPos = new BlockPos((int) Math.floor(breathingPos.x()), (int) Math.floor(breathingPos.y()+offset), (int) Math.floor(breathingPos.z()));
        BlockState breathingBlockState = entity.level().getBlockState(breathingBlockPos);

        if (breathingBlockState.is(QUICKSAND_DROWNABLE) && breathingBlockState.getBlock() instanceof QuicksandBase) {
            double quicksandLevel = ((QuicksandBase) breathingBlockState.getBlock()).getOffset(breathingBlockState);
            return (breathingBlockPos.getY() < breathingBlockPos.getY() + 1 - quicksandLevel);  // Checks in case we're in a partial quicksand block.
        } else {
            return false;
        }
    }

    public static boolean IsInWaterSpotCheck(LivingEntity entity, double offset) {
        Vec3 breathingPos = entity.getEyePosition();
        BlockPos breathingBlockPos = new BlockPos((int) Math.floor(breathingPos.x()), (int) Math.floor(breathingPos.y()+offset), (int) Math.floor(breathingPos.z()));
        BlockState breathingBlockState = entity.level().getBlockState(breathingBlockPos);
        FluidState fs = breathingBlockState.getFluidState();


        if (fs.getFluidType().canDrownIn(entity)) {
            double fluidLevel = fs.getOwnHeight();
            return (breathingPos.y < breathingBlockPos.getY() + fluidLevel);
        } else {
            return false;
        }
    }

    /**
     * Set entity to drown if their breathing point is in quicksand. This position is usually the eye position, but
     * the Snorkel and Breathing Reed increase this by +1 and +2 blocks respectively.
     * Yes, this means if your head is fine but your snorkel is in quicksand, you'll start drowning.
     */

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class GameModEvents {
        @SubscribeEvent
        public static void onLivingBreatheEvent(LivingBreatheEvent event) {
            LivingEntity entity = event.getEntity();

            boolean canBreatheUnderwater = entity.canDrownInFluidType(Fluids.WATER.getFluidType());
            boolean isDrowning = !event.canBreathe();
            boolean isDrowningInQuicksand = IsInQuicksandSpotCheck(entity, 0);


            boolean isHoldingReed = false; // Check inventory slots for the reed in hand, and mask anywhere in armor. This should handle baubles?
            boolean isWearingSnorkel = false;
            double reedHeight = 2;
            double snorkelHeight = 1.5; // SET TO BE CONFIGURABLE

            if (isDrowning || isDrowningInQuicksand || canBreatheUnderwater) {
                Iterable<ItemStack> handitems = entity.getHandSlots();
                for (ItemStack item : handitems) {
                    if (item.getItem().equals(ModItems.BREATHING_REED.get())) {
                        isHoldingReed = true;
                    }
                }
                Iterable<ItemStack> armorItems = entity.getArmorSlots();
                for (ItemStack item : armorItems) {
                    if (item.getItem().equals(ModItems.SNORKEL_MASK.get())) { // TODO: Check if this accounts for baubles. Also, add Baubles compatibility in the first place-
                        isWearingSnorkel = true;
                    }
                }

                double breathoffset = 0;
                if (isHoldingReed) {
                    breathoffset = reedHeight; // Default to using the reed.
                } else if (isWearingSnorkel) {
                    breathoffset = snorkelHeight;
                }

                Vec3 testpos = entity.getEyePosition();
                BlockPos breathingBlockPos = new BlockPos((int) Math.floor(testpos.x()), (int) Math.floor(testpos.y()+breathoffset), (int) Math.floor(testpos.z()));
                BlockState breathingBlockState = entity.level().getBlockState(breathingBlockPos);

                if (!IsInQuicksandSpotCheck(entity, breathoffset) && !IsInWaterSpotCheck(entity, breathoffset) && !breathingBlockState.isSuffocating(entity.level(), breathingBlockPos)) {
                    event.setCanBreathe(true);
                    event.setCanRefillAir(true);
                }
            }
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
