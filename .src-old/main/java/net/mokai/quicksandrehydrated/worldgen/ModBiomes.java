package net.mokai.quicksandrehydrated.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;

public class ModBiomes {
    public static final ResourceKey<Biome> MUDFLAT = register("mudflat");

    private static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(QuicksandRehydrated.MOD_ID, name));
    }
}