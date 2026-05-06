package net.mokai.quicksandrehydrated.util.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

public final class CuriosCompat {
    private CuriosCompat() {
    }

    public static void registerSlotTypes() {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("charm")
                        .size(2)
                        .icon(new ResourceLocation("curios", "slot/empty_charm_slot"))
                        .build());
    }

    public static boolean isEquipped(Player player, Item item) {
        return !CuriosApi.getCuriosHelper().findCurios(player, item).isEmpty();
    }
}
