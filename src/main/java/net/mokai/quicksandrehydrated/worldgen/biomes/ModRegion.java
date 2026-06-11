package net.mokai.quicksandrehydrated.worldgen.biomes;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.neoforged.bus.api.IEventBus;
import org.spongepowered.asm.util.IConsumer;
import terrablender.api.*;

import java.util.function.Consumer;

public class ModRegion extends Region {
    public ModRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();

        new ParameterUtils.ParameterPointListBuilder()
                // Matches mangrove swamp
                .temperature(ParameterUtils.Temperature.span(ParameterUtils.Temperature.WARM, ParameterUtils.Temperature.HOT))
                // Surprised that swamps don't depend on humidity at all tbh?
                // .humidity(Humidity.span(Humidity.ARID, Humidity.DRY))
                .continentalness(ParameterUtils.Continentalness.COAST)
                // swamp and mangrove swamp only spawn at erosion 6
                .erosion(ParameterUtils.Erosion.EROSION_6)
                .depth(ParameterUtils.Depth.SURFACE, ParameterUtils.Depth.FLOOR)
                // TODO: Maybe make the LOW_SLICE_VARIANT_ASCENDING a different, rarer biome?
                .weirdness(ParameterUtils.Weirdness.VALLEY, ParameterUtils.Weirdness.LOW_SLICE_NORMAL_DESCENDING, ParameterUtils.Weirdness.LOW_SLICE_VARIANT_ASCENDING)
                .build().forEach(point -> builder.add(point, ModBiomes.MUDFLAT));

        // Add our points to the mapper
        builder.build().forEach(mapper);
    }

    public static void register(IEventBus modBus) {
        Regions.register(
                new ModRegion(
                        ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "overworld"),
                        5
                )
        );
    }
}