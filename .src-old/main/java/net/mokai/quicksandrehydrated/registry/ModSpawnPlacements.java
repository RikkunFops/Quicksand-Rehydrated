package net.mokai.quicksandrehydrated.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.mokai.quicksandrehydrated.entity.EntityTarSlime;

import static net.mokai.quicksandrehydrated.QuicksandRehydrated.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModSpawnPlacements {
    private ModSpawnPlacements() {}

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent e) {
        e.enqueueWork(() -> SpawnPlacements.register(
                ModEntityTypes.TAR_SLIME.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModSpawnPlacements::canSpawnTarSlime
        ));
    }

    private static boolean canSpawnTarSlime(
            net.minecraft.world.entity.EntityType<EntityTarSlime> type,
            ServerLevelAccessor level,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource rng) {
        return level.getRawBrightness(pos, 0) <= 7; // darker spots
    }
}
