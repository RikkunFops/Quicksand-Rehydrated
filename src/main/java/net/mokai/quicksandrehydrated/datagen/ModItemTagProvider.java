package net.mokai.quicksandrehydrated.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.registry.ModBlocks;
import net.mokai.quicksandrehydrated.registry.QuicksandRegistry;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<net.minecraft.world.level.block.Block>> blockTagsProvider,
                              @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagsProvider, QuicksandRehydrated.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Wool items for quickrug
        tag(ItemTags.WOOL)
                .add(QuicksandRegistry.WHITE_QUICKRUG.asItem())
                .add(QuicksandRegistry.ORANGE_QUICKRUG.asItem())
                .add(QuicksandRegistry.MAGENTA_QUICKRUG.asItem())
                .add(QuicksandRegistry.LIGHT_BLUE_QUICKRUG.asItem())
                .add(QuicksandRegistry.YELLOW_QUICKRUG.asItem())
                .add(QuicksandRegistry.LIME_QUICKRUG.asItem())
                .add(QuicksandRegistry.PINK_QUICKRUG.asItem())
                .add(QuicksandRegistry.GRAY_QUICKRUG.asItem())
                .add(QuicksandRegistry.LIGHT_GRAY_QUICKRUG.asItem())
                .add(QuicksandRegistry.CYAN_QUICKRUG.asItem())
                .add(QuicksandRegistry.PURPLE_QUICKRUG.asItem())
                .add(QuicksandRegistry.BLUE_QUICKRUG.asItem())
                .add(QuicksandRegistry.BROWN_QUICKRUG.asItem())
                .add(QuicksandRegistry.GREEN_QUICKRUG.asItem())
                .add(QuicksandRegistry.RED_QUICKRUG.asItem())
                .add(QuicksandRegistry.BLACK_QUICKRUG.asItem());
    }
}
