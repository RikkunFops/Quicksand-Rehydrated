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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mokai.quicksandrehydrated.registry.*;
import net.mokai.quicksandrehydrated.util.Keybinding;
import net.mokai.quicksandrehydrated.worldgen.placement.ModPlacementModifierTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.client.KeyMapping;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent.Pre;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent.Post;
import net.minecraft.world.entity.player.Player;


import net.mokai.quicksandrehydrated.util.CameraBoxDimensions;

import static net.mokai.quicksandrehydrated.util.ModTags.Blocks.QUICKSAND_DROWNABLE;

@Mod(QuicksandRehydrated.MOD_ID)
public class QuicksandRehydrated {

    public static final String MOD_ID = "qsrehydrated";

	public QuicksandRehydrated(IEventBus modBus) {
		modBus.register(ClientMod.class);
		NeoForge.EVENT_BUS.register(GameModEvents.class);

		QuicksandRegistry.register(modBus);
		ModEntityTypes.register(modBus);
		ModItems.register(modBus);
		ModParticles.register(modBus);
		ModSounds.register(modBus);
		ModBlocks.register(modBus);
		ModFeatures.FEATURES.register(modBus);
		ModPlacementModifierTypes.register(modBus);
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

	public static class GameModEvents {
    	@SubscribeEvent
    	public static void OnLivingBreathe(LivingBreatheEvent event) {
    		LivingEntity entity = event.getEntity();
    		
            double w = CameraBoxDimensions.FULL_WIDTH;
            double h = CameraBoxDimensions.FULL_HEIGHT;
            AABB cameraBox = AABB.ofSize(entity.getEyePosition(), w, h, w);
            if (boxOverlapsQuicksandBlock(entity.level(), cameraBox)) {
            	event.setCanBreathe(false);
            }
    	}

    }
    
}