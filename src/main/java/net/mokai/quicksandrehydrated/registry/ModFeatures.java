package net.mokai.quicksandrehydrated.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.worldgen.feature.QuicksandPitConfiguration;
import net.mokai.quicksandrehydrated.worldgen.feature.QuicksandPitFeature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.xml.catalog.CatalogFeatures;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, QuicksandRehydrated.MOD_ID);

    public static final DeferredHolder<Feature<?>, QuicksandPitFeature> QUICKSAND_PIT_FEATURE =
            FEATURES.register("quicksand_pit",
                    () -> new QuicksandPitFeature(QuicksandPitConfiguration.CODEC));
}

