package net.mokai.quicksandrehydrated.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.worldgen.feature.QuicksandPitConfiguration;
import net.mokai.quicksandrehydrated.worldgen.feature.QuicksandPitFeature;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(
        ForgeRegistries.FEATURES, QuicksandRehydrated.MOD_ID);

    public static final RegistryObject<Feature<QuicksandPitConfiguration>> QUICKSAND_PIT = FEATURES.register(
        "quicksand_pit", () -> new QuicksandPitFeature(QuicksandPitConfiguration.CODEC));

    public static final RegistryObject<Feature<QuicksandPitConfiguration>> MUD_PIT = FEATURES.register(
        "mud_pit", () -> new QuicksandPitFeature(QuicksandPitConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
    
    /**
     * Registers the quicksand pit feature for world generation.
     * This is called during the common setup phase.
     */
    public static void registerWorldGeneration() { 
        // The actual world generation is handled by JSON files in:
        // - data/qsrehydrated/worldgen/configured_feature/quicksand_pit.json
        // - data/qsrehydrated/worldgen/placed_feature/quicksand_pit.json
        // - data/qsrehydrated/tags/worldgen/biome/has_quicksand_pit.json
        // - data/qsrehydrated/forge/biome_modifier/add_quicksand_pit.json
        
        System.out.println("==============================================");
        System.out.println("Quicksand pit feature registered for world generation");
        System.out.println("Feature: " + QUICKSAND_PIT.getId());
        System.out.println("Feature instance: " + QUICKSAND_PIT.get());
        System.out.println("Feature: " + MUD_PIT.getId());
        System.out.println("Feature instance: " + MUD_PIT.get());
        System.out.println("==============================================");
        
        // Ensure the feature is registered with the correct registry
        registerConditionally("quicksand_pit", QUICKSAND_PIT);
        registerConditionally("mud_pit", MUD_PIT);
    }

    private static void registerConditionally(String name, RegistryObject<Feature<QuicksandPitConfiguration>> feature) {
        var key = new ResourceLocation(QuicksandRehydrated.MOD_ID, name);
        try {
            if (!BuiltInRegistries.FEATURE.containsKey(key)) {
                System.out.println("Registering feature for world generation: " + name);
                Registry.register(BuiltInRegistries.FEATURE, key, feature.get());
            }
        } catch (Exception e) {
            // Log any errors that occur during registration
            System.err.println("Error registering quicksand pit feature for world generation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}