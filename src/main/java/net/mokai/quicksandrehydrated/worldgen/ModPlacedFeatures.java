package net.mokai.quicksandrehydrated.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.registry.ModFeatures;
import net.mokai.quicksandrehydrated.worldgen.feature.QuicksandPitConfiguration;
import net.mokai.quicksandrehydrated.worldgen.feature.QuicksandPitFeature;
import net.mokai.quicksandrehydrated.worldgen.placement.QuicksandPitPlacement;
import net.neoforged.fml.common.Mod;

import java.util.List;


public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> QUICKSAND_PIT_PLACED = registerKey("quicksand_pit_placed");
    public static final ResourceKey<PlacedFeature> MUD_PIT_PLACED = registerKey("mud_pit_placed");


    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, QUICKSAND_PIT_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.QUICKSAND_PIT_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(2),
                        InSquarePlacement.spread(),
                        QuicksandPitPlacement.INSTANCE,
                        BiomeFilter.biome(),
                        CountPlacement.of(2),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG)
                ));

        register(context, MUD_PIT_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.MUD_PIT_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(2),
                        InSquarePlacement.spread(),
                        QuicksandPitPlacement.INSTANCE,
                        BiomeFilter.biome(),
                        CountPlacement.of(2),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG)
                ));
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?,?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
