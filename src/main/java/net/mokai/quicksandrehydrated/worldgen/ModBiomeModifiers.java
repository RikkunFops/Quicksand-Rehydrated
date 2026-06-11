package net.mokai.quicksandrehydrated.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.worldgen.feature.QuicksandPitFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static net.mokai.quicksandrehydrated.util.ModBiomeTags.*;

public class ModBiomeModifiers {
    // CF -> PF -> BM
    public static final ResourceKey<BiomeModifier> ADD_QUICKSAND_PIT = registerKey("add_quicksand_pit");
    public static final ResourceKey<BiomeModifier> ADD_MUD_PIT = registerKey("add_mud_pit");
    public static final ResourceKey<BiomeModifier> ADD_BOG_PIT = registerKey("add_bog_pit");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_QUICKSAND_PIT, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(HAS_QUICKSAND_PIT),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.QUICKSAND_PIT_PLACED)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION
        ));
        context.register(ADD_MUD_PIT, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(HAS_MUD_PIT),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MUD_PIT_PLACED)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION
        ));
        context.register(ADD_BOG_PIT, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(HAS_BOG_PIT),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.BOG_PIT_PLACED)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION
        ));
    }

    public static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, name));
    }
}
