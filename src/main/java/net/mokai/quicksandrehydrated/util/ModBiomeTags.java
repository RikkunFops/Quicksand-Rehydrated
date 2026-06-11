package net.mokai.quicksandrehydrated.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;

public class ModBiomeTags {

    // custom qsrehydrated biometags
    public static final TagKey<Biome> HAS_QUICKSAND_PIT =
            TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "has_quicksand_pit"));

    public static final TagKey<Biome> HAS_MUD_PIT =
            TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "has_mud_pit"));

    public static final TagKey<Biome> HAS_BOG_PIT =
            TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "has_bog_pit"));
}
