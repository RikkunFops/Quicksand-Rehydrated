package net.mokai.quicksandrehydrated.worldgen;

import java.util.function.Consumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;
import static terrablender.api.ParameterUtils.*;

public class ModRegion extends Region {

    public ModRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();

        new ParameterPointListBuilder()
            // Matches mangrove swamp
            .temperature(Temperature.span(Temperature.WARM, Temperature.HOT))
            // Surprised that swamps don't depend on humidity at all tbh?
            // .humidity(Humidity.span(Humidity.ARID, Humidity.DRY))
            .continentalness(Continentalness.COAST)
            // swamp and mangrove swamp only spawn at erosion 6
            .erosion(Erosion.EROSION_6)
            .depth(Depth.SURFACE, Depth.FLOOR)
            // TODO: Maybe make the LOW_SLICE_VARIANT_ASCENDING a different, rarer biome?
            .weirdness(Weirdness.VALLEY, Weirdness.LOW_SLICE_NORMAL_DESCENDING, Weirdness.LOW_SLICE_VARIANT_ASCENDING)
            .build().forEach(point -> builder.add(point, ModBiomes.MUDFLAT));

        // Add our points to the mapper
        builder.build().forEach(mapper);
    }
}