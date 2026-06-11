package net.mokai.quicksandrehydrated;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.mokai.quicksandrehydrated.entity.coverage.WashingSystem;
import net.mokai.quicksandrehydrated.registry.*;
import net.mokai.quicksandrehydrated.worldgen.placement.ModPlacementModifierTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;


import net.mokai.quicksandrehydrated.util.CameraBoxDimensions;

import static net.mokai.quicksandrehydrated.util.ModTags.Blocks.QUICKSAND_DROWNABLE;

@Mod(QuicksandRehydrated.MOD_ID)
public class QuicksandRehydrated {

    public static final String MOD_ID = "qsrehydrated";

	public QuicksandRehydrated(IEventBus modBus) {
		modBus.register(ClientMod.class);

		QuicksandRegistry.register(modBus);

		ModEntityTypes.register(modBus);
		ModItems.register(modBus);
		ModParticles.register(modBus);
		ModSounds.register(modBus);
		ModBlocks.register(modBus);
		ModFeatures.FEATURES.register(modBus);
		ModPlacementModifierTypes.register(modBus);
		ModRecipes.register(modBus);
		ModCreativeModeTab.register(modBus);
    }
    
    private static boolean boxOverlapsQuicksandBlock(Level level, AABB box) {
    	BlockPos minXYZ = BlockPos.containing(box.minX,box.minY,box.minZ);
    	BlockPos maxXYZ = BlockPos.containing(box.maxX,box.maxY,box.maxZ);
    	for (BlockPos blockPos: BlockPos.betweenClosed(minXYZ, maxXYZ)) {
    		BlockState blockState = level.getBlockState(blockPos);
    		if (!blockState.isAir() && blockState.is(QUICKSAND_DROWNABLE)) {
    			AABB boundingBox = blockState.getShape(level, blockPos).bounds().move(blockPos);
    			if (boundingBox.intersects(box )) {
    				return true;
    			}
    		}
    	}
    	return false;
    }

	@EventBusSubscriber(modid = QuicksandRehydrated.MOD_ID)
	public static class GameModEvents {
    	@SubscribeEvent
    	public static void LivingBreathingEvent(LivingBreatheEvent event) {
    		LivingEntity entity = event.getEntity();
    		
            double w = CameraBoxDimensions.FULL_WIDTH;
            double h = CameraBoxDimensions.FULL_HEIGHT;
            AABB cameraBox = AABB.ofSize(entity.getEyePosition(), w, h, w);
            if (boxOverlapsQuicksandBlock(entity.level(), cameraBox)) {
            	event.setCanBreathe(false);
            }
    	}

		@SubscribeEvent
		public static void onPlayerTick(PlayerTickEvent.Post event) {
			Player player = event.getEntity();
			Level level = player.level();

			WashingSystem.applyWashingEffect(player,level);
		}


    }
    
}