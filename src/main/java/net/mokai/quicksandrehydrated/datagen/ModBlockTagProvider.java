package net.mokai.quicksandrehydrated.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.registry.ModBlocks;
import net.mokai.quicksandrehydrated.registry.QuicksandRegistry;
import net.mokai.quicksandrehydrated.util.ModBlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, QuicksandRehydrated.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Custom qsrehydrated namespace tags
        tag(ModBlockTags.QUICKSAND_DROWNABLE)
                .add(QuicksandRegistry.QUICKSAND.get())
                .add(QuicksandRegistry.LIVING_SLIME.get())
                .add(QuicksandRegistry.SOFT_QUICKSAND.get())
                .add(QuicksandRegistry.BOG.get())
                .add(QuicksandRegistry.PEAT_BOG.get())
                .add(QuicksandRegistry.MOSSY_PEAT_BOG.get())
                .add(QuicksandRegistry.THIN_MUD.get())
                .add(QuicksandRegistry.SHALLOW_MUD.get())
                .add(QuicksandRegistry.DEEP_MUD.get())
                .add(QuicksandRegistry.BOTTOMLESS_MUD.get())
                .add(QuicksandRegistry.TIDAL_MUD.get())
                .add(QuicksandRegistry.BLACK_QUICKRUG.get())
                .add(QuicksandRegistry.BLUE_QUICKRUG.get())
                .add(QuicksandRegistry.BROWN_QUICKRUG.get())
                .add(QuicksandRegistry.CYAN_QUICKRUG.get())
                .add(QuicksandRegistry.GRAY_QUICKRUG.get())
                .add(QuicksandRegistry.GREEN_QUICKRUG.get())
                .add(QuicksandRegistry.LIGHT_BLUE_QUICKRUG.get())
                .add(QuicksandRegistry.LIGHT_GRAY_QUICKRUG.get())
                .add(QuicksandRegistry.LIME_QUICKRUG.get())
                .add(QuicksandRegistry.MAGENTA_QUICKRUG.get())
                .add(QuicksandRegistry.ORANGE_QUICKRUG.get())
                .add(QuicksandRegistry.PINK_QUICKRUG.get())
                .add(QuicksandRegistry.PURPLE_QUICKRUG.get())
                .add(QuicksandRegistry.RED_QUICKRUG.get())
                .add(QuicksandRegistry.WHITE_QUICKRUG.get())
                .add(QuicksandRegistry.YELLOW_QUICKRUG.get());

        tag(ModBlockTags.DUCKWEED)
                .add(QuicksandRegistry.THIN_MUD.get())
                .add(QuicksandRegistry.SHALLOW_MUD.get())
                .add(QuicksandRegistry.DEEP_MUD.get())
                .add(QuicksandRegistry.BOTTOMLESS_MUD.get())
                .add(QuicksandRegistry.BOG.get());

        tag(ModBlockTags.QUICKSAND_AO_OVERRIDE)
                .add(QuicksandRegistry.QUICKSAND.get())
                .add(QuicksandRegistry.THIN_MUD.get())
                .add(QuicksandRegistry.SHALLOW_MUD.get())
                .add(QuicksandRegistry.DEEP_MUD.get())
                .add(QuicksandRegistry.BOTTOMLESS_MUD.get())
                .add(QuicksandRegistry.TIDAL_MUD.get());

        tag(ModBlockTags.PEAT_BOG_BUSH)
                .add(QuicksandRegistry.PEAT_BOG.get())
                .add(QuicksandRegistry.MOSSY_PEAT_BOG.get());

        tag(ModBlockTags.FERN_BUSH)
                .add(QuicksandRegistry.BOG.get());

        // Minecraft namespace mineable tags
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(QuicksandRegistry.QUICKSAND.get())
                .add(QuicksandRegistry.SOFT_QUICKSAND.get());

     

        // Wool category for quickrug blocks
        tag(BlockTags.WOOL)
                .add(QuicksandRegistry.WHITE_QUICKRUG.get())
                .add(QuicksandRegistry.ORANGE_QUICKRUG.get())
                .add(QuicksandRegistry.MAGENTA_QUICKRUG.get())
                .add(QuicksandRegistry.LIGHT_BLUE_QUICKRUG.get())
                .add(QuicksandRegistry.YELLOW_QUICKRUG.get())
                .add(QuicksandRegistry.LIME_QUICKRUG.get())
                .add(QuicksandRegistry.PINK_QUICKRUG.get())
                .add(QuicksandRegistry.GRAY_QUICKRUG.get())
                .add(QuicksandRegistry.LIGHT_GRAY_QUICKRUG.get())
                .add(QuicksandRegistry.CYAN_QUICKRUG.get())
                .add(QuicksandRegistry.PURPLE_QUICKRUG.get())
                .add(QuicksandRegistry.BLUE_QUICKRUG.get())
                .add(QuicksandRegistry.BROWN_QUICKRUG.get())
                .add(QuicksandRegistry.GREEN_QUICKRUG.get())
                .add(QuicksandRegistry.RED_QUICKRUG.get())
                .add(QuicksandRegistry.BLACK_QUICKRUG.get());
    }
}
