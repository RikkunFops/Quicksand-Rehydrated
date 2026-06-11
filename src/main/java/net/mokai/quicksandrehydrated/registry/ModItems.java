package net.mokai.quicksandrehydrated.registry;

import net.minecraft.sounds.Music;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.core.registries.Registries;
import net.mokai.quicksandrehydrated.item.QuicksandBook;
import net.mokai.quicksandrehydrated.item.potion.QuicksandPotion;
import net.mokai.quicksandrehydrated.item.potion.QuicksandPotionThrowable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(QuicksandRehydrated.MOD_ID);



    public static final DeferredItem<Item> MUSIC_DISC =
            ITEMS.register("music_disc_flight", () -> new Item( new Item.Properties().jukeboxPlayable(ModSounds.FLIGHT_SONG_KEY).stacksTo(1)));
    public static final DeferredItem<Item> QUICKSAND_POTION =
            ITEMS.register("potion_of_sinking", () ->
                    new QuicksandPotion(new Item.Properties( )
                            .stacksTo(1)
                            ));
    public static final DeferredItem<Item> QUICKSAND_POTION_THROWBALE =
            ITEMS.register("splash_potion_of_sinking", () ->
                    new QuicksandPotionThrowable(new Item.Properties()
                            .stacksTo(1)
                    ));
    public static final DeferredItem<Item> QUICKSAND_BOOK =
            ITEMS.register("quicksand_book", () ->
                    new QuicksandBook(new Item.Properties()
                            .stacksTo(1)));

    public static Collection<ItemStack> setupCreativeGroups() {
        CREATIVELIST = new ArrayList<>();

        addItem(QUICKSAND_BOOK);
        addItem(QUICKSAND_POTION);
        addItem(QUICKSAND_POTION_THROWBALE);
        addItem(MUSIC_DISC);
        return CREATIVELIST;
    }
    private static Collection<ItemStack> CREATIVELIST;

    public static void addItem(DeferredItem<?> block) {
        CREATIVELIST.add(new ItemStack(block.get()));
    }

    public static void register(IEventBus event) {
        ITEMS.register(event);
    }
}
