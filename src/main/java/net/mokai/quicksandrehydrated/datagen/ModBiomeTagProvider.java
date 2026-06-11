package net.mokai.quicksandrehydrated.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.util.ModBiomeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends BiomeTagsProvider {
    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, QuicksandRehydrated.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModBiomeTags.HAS_QUICKSAND_PIT)
                .add(Biomes.DESERT)
                .add(Biomes.BADLANDS)
                .add(Biomes.WOODED_BADLANDS)
                .add(Biomes.ERODED_BADLANDS)
                .add(Biomes.BEACH)
                .add(Biomes.PLAINS)
                .add(Biomes.SAVANNA)
                .add(Biomes.SAVANNA_PLATEAU)
                .add(Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.MEADOW)
                .replace(false);

        tag(ModBiomeTags.HAS_MUD_PIT)
                .add(Biomes.MANGROVE_SWAMP)
                .replace(false);

        tag(ModBiomeTags.HAS_BOG_PIT)
                .add(Biomes.SWAMP)
                .replace(false);
    }


}
