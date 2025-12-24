package net.mokai.quicksandrehydrated.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.registry.ModPlacedFeatures;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_QUICKSAND_PIT = registerKey("add_quicksand_pit");
    
    // Tags for desert biomes
    public static final TagKey<Biome> DESERT_BIOMES = TagKey.create(
            Registries.BIOME, new ResourceLocation("minecraft", "is_desert"));
            
    // Tags for all overworld biomes
    public static final TagKey<Biome> OVERWORLD_BIOMES = TagKey.create(
            Registries.BIOME, new ResourceLocation("minecraft", "is_overworld"));

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        // Let's add quicksand pools to all overworld biomes for testing purposes.
        // This will dramatically increase the chance of finding quicksand pools.
        try {
            context.register(ADD_QUICKSAND_PIT, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    biomes.getOrThrow(OVERWORLD_BIOMES), // We use all biomes of the overworld for testing.
                    HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.QUICKSAND_PIT_PLACED_KEY)),
                    GenerationStep.Decoration.TOP_LAYER_MODIFICATION
            ));
            
            System.out.println("[ModBiomeModifiers] Registered biome modifier for quicksand pools");
            System.out.println("[ModBiomeModifiers] Feature key: " + ModPlacedFeatures.QUICKSAND_PIT_PLACED_KEY);
        } catch (Exception e) {
            System.err.println("[ModBiomeModifiers] ERROR while registering the biome modifier: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, 
                new ResourceLocation(QuicksandRehydrated.MOD_ID, name));
    }
}