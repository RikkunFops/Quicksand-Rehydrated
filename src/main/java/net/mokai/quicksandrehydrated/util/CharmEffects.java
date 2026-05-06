package net.mokai.quicksandrehydrated.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.mokai.quicksandrehydrated.registry.ModItems;
import net.mokai.quicksandrehydrated.util.compat.CuriosCompat;

public final class CharmEffects {
    private static final double HEAVY_MULTIPLIER = 2.0;
    private static final double LIGHT_MULTIPLIER = 0.5;

    private CharmEffects() {
    }

    public static double getSinkSpeedMultiplier(Player player) {
        boolean hasHeavy = hasCharm(player, ModItems.HEAVY_CHARM.get());
        boolean hasLight = hasCharm(player, ModItems.LIGHT_CHARM.get());

        if (hasHeavy == hasLight) {
            return 1.0;
        }
        return hasHeavy ? HEAVY_MULTIPLIER : LIGHT_MULTIPLIER;
    }

    private static boolean hasCharm(Player player, Item item) {
        return isHeld(player, item) || isEquippedInCurios(player, item);
    }

    private static boolean isHeld(Player player, Item item) {
        return player.getMainHandItem().is(item) || player.getOffhandItem().is(item);
    }

    private static boolean isEquippedInCurios(Player player, Item item) {
        if (!ModList.get().isLoaded("curios")) {
            return false;
        }
        return CuriosCompat.isEquipped(player, item);
    }
}
