package net.mokai.quicksandrehydrated.registry;

import net.minecraft.sounds.Music;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(QuicksandRehydrated.MOD_ID);

    public static final DeferredItem<Item> MUSIC_DISC =
            ITEMS.register("music_disc_flight", () -> new Item( new Item.Properties().jukeboxPlayable(ModSounds.FLIGHT_SONG_KEY).stacksTo(1)));

    public static void register(IEventBus event) {
        ITEMS.register(event);
    }
}
