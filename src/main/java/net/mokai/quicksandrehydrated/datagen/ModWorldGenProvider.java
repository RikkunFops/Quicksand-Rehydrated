package net.mokai.quicksandrehydrated.datagen;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.worldgen.ModBiomeModifiers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;

import net.minecraft.data.PackOutput;
import net.mokai.quicksandrehydrated.worldgen.ModConfiguredFeatures;
import net.mokai.quicksandrehydrated.worldgen.ModPlacedFeatures;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

    public ModWorldGenProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries) {

        super(
                output,
                registries,
                BUILDER,
                Set.of(QuicksandRehydrated.MOD_ID)
        );
    }
}
