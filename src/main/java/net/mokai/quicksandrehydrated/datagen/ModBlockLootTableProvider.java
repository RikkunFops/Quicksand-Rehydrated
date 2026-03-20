package net.mokai.quicksandrehydrated.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.mokai.quicksandrehydrated.registry.ModBlocks;
import net.mokai.quicksandrehydrated.registry.QuicksandRegistry;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        // Generate dropSelf loot tables for all known blocks
        for (Block block : getKnownBlocks()) {
            dropSelf(block);
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return getAllKnownBlocks();
    }

    private Iterable<Block> getAllKnownBlocks() {
        return new java.util.ArrayList<Block>() {{
            // QuicksandRegistry blocks
            add(QuicksandRegistry.QUICKSAND.get());
            add(QuicksandRegistry.LIVING_SLIME.get());
            add(QuicksandRegistry.MOSSY_PEAT_BOG.get());
            add(QuicksandRegistry.PEAT_BOG.get());
            add(QuicksandRegistry.BOG.get());
            add(QuicksandRegistry.THIN_MUD.get());
            add(QuicksandRegistry.SHALLOW_MUD.get());
            add(QuicksandRegistry.DEEP_MUD.get());
            add(QuicksandRegistry.BOTTOMLESS_MUD.get());
            add(QuicksandRegistry.TIDAL_MUD.get());
            add(QuicksandRegistry.SOFT_QUICKSAND.get());

            // Quickrug blocks
            add(QuicksandRegistry.WHITE_QUICKRUG.get());
            add(QuicksandRegistry.ORANGE_QUICKRUG.get());
            add(QuicksandRegistry.MAGENTA_QUICKRUG.get());
            add(QuicksandRegistry.LIGHT_BLUE_QUICKRUG.get());
            add(QuicksandRegistry.YELLOW_QUICKRUG.get());
            add(QuicksandRegistry.LIME_QUICKRUG.get());
            add(QuicksandRegistry.PINK_QUICKRUG.get());
            add(QuicksandRegistry.GRAY_QUICKRUG.get());
            add(QuicksandRegistry.LIGHT_GRAY_QUICKRUG.get());
            add(QuicksandRegistry.CYAN_QUICKRUG.get());
            add(QuicksandRegistry.PURPLE_QUICKRUG.get());
            add(QuicksandRegistry.BLUE_QUICKRUG.get());
            add(QuicksandRegistry.BROWN_QUICKRUG.get());
            add(QuicksandRegistry.GREEN_QUICKRUG.get());
            add(QuicksandRegistry.RED_QUICKRUG.get());
            add(QuicksandRegistry.BLACK_QUICKRUG.get());

            // ModBlocks plant blocks
            add(ModBlocks.DUCKWEED.get());
            add(ModBlocks.DUCKWEED_FLOWERS.get());
            add(ModBlocks.PEAT_BOG_BUSH.get());
            add(ModBlocks.FERN_BUSH.get());
            add(ModBlocks.CATTAIL_REEDS.get());
            add(ModBlocks.BRANCH.get());
        }};
    }
}
