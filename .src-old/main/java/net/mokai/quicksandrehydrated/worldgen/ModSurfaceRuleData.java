package net.mokai.quicksandrehydrated.worldgen;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.registry.QuicksandRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ModSurfaceRuleData {

    private static final SurfaceRules.RuleSource AIR = makeStateRule(Blocks.AIR);
    private static final SurfaceRules.RuleSource WATER = makeStateRule(Blocks.WATER);
    private static final SurfaceRules.RuleSource MUD = makeStateRule(Blocks.MUD);
    private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
    private static final SurfaceRules.RuleSource TIDAL_MUD = makeStateRule(QuicksandRegistry.TIDAL_MUD.get());

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }

    public static SurfaceRules.RuleSource makeRules() {
        var noise_1 = ResourceKey.create(Registries.NOISE, new ResourceLocation(QuicksandRehydrated.MOD_ID, "mudflat_noise"));
        var noise_2 = ResourceKey.create(Registries.NOISE, new ResourceLocation(QuicksandRehydrated.MOD_ID, "mudflat_noise_2"));

        SurfaceRules.ConditionSource isAbove62 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
        SurfaceRules.ConditionSource isAbove63 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
        SurfaceRules.ConditionSource isAbove64 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(64), 0);

        var mudflatDetails = SurfaceRules.sequence(
            // Flatten the terrain
            SurfaceRules.ifTrue(isAbove64, AIR),
            // Sand for variety, intentionally kinda rare though
            SurfaceRules.ifTrue(SurfaceRules.noiseCondition(noise_1, 0.6, 1.0f), SAND),
            // Carve out channels of mud + water through the landscape
            SurfaceRules.ifTrue(
                SurfaceRules.noiseCondition(noise_1, -0.06f, 0.06f),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(isAbove63, AIR),
                    SurfaceRules.ifTrue(isAbove62, SurfaceRules.ifTrue(SurfaceRules.noiseCondition(noise_1, -0.02f, 0.02f), WATER)),
                    TIDAL_MUD
                )
            ),
            // Carve out channels of just mud
            SurfaceRules.ifTrue(
                SurfaceRules.noiseCondition(noise_2, -0.06f, 0.06f),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(isAbove63, AIR),
                    TIDAL_MUD
                )
            ),
            SurfaceRules.ifTrue(isAbove63, MUD),
            // Stuff at the waterline is commonly sinkable
            SurfaceRules.ifTrue(SurfaceRules.noiseCondition(noise_1, -0.15f, 0.15f), TIDAL_MUD),
            SurfaceRules.ifTrue(SurfaceRules.noiseCondition(noise_2, -0.15f, 0.15f), TIDAL_MUD),
            // Everything else is solid mud
            MUD
        );

        var basicSurface = SurfaceRules.sequence(
            SurfaceRules.ifTrue(SurfaceRules.noiseCondition(noise_1, 0.6, 1.0f), SAND),
            MUD
        );

        // Is there a better way to write this?
        var mudflatRules = SurfaceRules.sequence(
            SurfaceRules.ifTrue(isAbove62, mudflatDetails),
            SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, basicSurface),
            SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, basicSurface)
            // Intentionally have a fallthrough
        );

        // Might need to add biome test later, but for now since this is the only biome in the region we don't need it
        return mudflatRules;

        // return SurfaceRules.sequence(
        //     SurfaceRules.ifTrue(SurfaceRules.isBiome(TestBiomes.MUDFLAT), mudflatRules)
        // );
    }
}