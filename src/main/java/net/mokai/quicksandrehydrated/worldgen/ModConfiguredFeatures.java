package net.mokai.quicksandrehydrated.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.registry.ModBlocks;
import net.mokai.quicksandrehydrated.registry.ModFeatures;
import net.mokai.quicksandrehydrated.registry.QuicksandRegistry;
import net.mokai.quicksandrehydrated.worldgen.feature.QuicksandPitConfiguration;
import net.mokai.quicksandrehydrated.worldgen.feature.QuicksandPitFeature;

import java.lang.module.Configuration;
import java.util.Arrays;
import java.util.List;


import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderGetter;
import net.mokai.quicksandrehydrated.worldgen.placement.QuicksandPitPlacement;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?,?>> QUICKSAND_PIT_KEY = registerKey("quicksand_pit");
    public static final ResourceKey<ConfiguredFeature<?,?>> MUD_PIT_KEY = registerKey("mud_pit");


    public static void bootstrap(BootstrapContext<ConfiguredFeature<?,?>> context) {
        HolderGetter<Feature<?>> featureLookup = context.lookup(Registries.FEATURE);
        
        // Get the registered QuicksandPitFeature from the registry
        var quicksandPitFeatureHolder = featureLookup.getOrThrow(
            ResourceKey.create(Registries.FEATURE, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "quicksand_pit"))
        );

        // Create a QuicksandPitConfiguration with desired parameters
        QuicksandPitConfiguration quicksand_config = new QuicksandPitConfiguration(
                QuicksandRegistry.QUICKSAND.get(),      // Block type for the pit
                QuicksandPitConfiguration.DEFAULT_MIN_RADIUS ,      // min radius
                QuicksandPitConfiguration.DEFAULT_MAX_RADIUS,     // max radius
                QuicksandPitConfiguration.DEFAULT_MIN_DEPTH,      // min depth
                QuicksandPitConfiguration.DEFAULT_MAX_DEPTH,      // max depth
                QuicksandPitConfiguration.DEFAULT_IRREGULARITY,   // irregularity
                false,   // has border
                java.util.Optional.empty(),   // border block (empty = same as pit)
                java.util.Optional.empty(),   // replaceable blocks (empty = use default)
                QuicksandPitConfiguration.DEFAULT_MIN_HEIGHT,     // min height
                QuicksandPitConfiguration.DEFAULT_MAX_HEIGHT      // max height
        );

        QuicksandPitConfiguration mud_config = new QuicksandPitConfiguration(
                QuicksandRegistry.SHALLOW_MUD.get(),      // block
                3,                                         // min radius
                6,                                         // max radius
                3,                                         // min depth
                5,                                         // max depth
                0.8f,                                      // irregularity
                true,                                      // has border
                java.util.Optional.of(QuicksandRegistry.THIN_MUD.get()),  // border block
                java.util.Optional.of(Arrays.asList(Blocks.MUD)),         // replaceable blocks
                62,                                        // min height
                320                                        // max height
        );

        context.register(QUICKSAND_PIT_KEY, new ConfiguredFeature<>((QuicksandPitFeature) quicksandPitFeatureHolder.value(), quicksand_config));
        context.register(MUD_PIT_KEY, new ConfiguredFeature<>((QuicksandPitFeature) quicksandPitFeatureHolder.value(), mud_config));
    }
    public static ResourceKey<ConfiguredFeature<?,?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, name));
    }
}
