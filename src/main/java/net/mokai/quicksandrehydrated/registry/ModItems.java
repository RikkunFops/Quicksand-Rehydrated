package net.mokai.quicksandrehydrated.registry;

import net.minecraft.world.item.alchemy.Potion;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, QuicksandRehydrated.MOD_ID);

    public static void register(IEventBus event) {
        ITEMS.register(event);
    }
}
