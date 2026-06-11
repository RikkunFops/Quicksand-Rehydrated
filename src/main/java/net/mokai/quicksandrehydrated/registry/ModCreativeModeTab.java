package net.mokai.quicksandrehydrated.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.mokai.quicksandrehydrated.block.quicksands.Quicksand;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;

import java.util.function.Supplier;

public class ModCreativeModeTab {

    // Deferred register for creative tabs
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, QuicksandRehydrated.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> QUICKSAND_TAB = CREATIVE_TABS.register("quicksand", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("Quicksand Messes"))
                    .icon(() -> QuicksandRegistry.QUICKSAND.get().asItem().getDefaultInstance())
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.acceptAll(QuicksandRegistry.setupCreativeGroup());
                    })).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PLANTS_TAB = CREATIVE_TABS.register("plants", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("Quicksand Plants"))
                    .icon(() -> ModBlocks.CATTAIL_REEDS.get().asItem().getDefaultInstance())
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.acceptAll(ModBlocks.setupCreativeGroups());
                    })).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> QUICKSAND_ITEMS = CREATIVE_TABS.register("items", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("QSRH Items"))
                    .icon(() -> ModItems.QUICKSAND_POTION.get().asItem().getDefaultInstance())
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.acceptAll(ModItems.setupCreativeGroups());
                    })).build());


    public static void register(IEventBus event) {
        CREATIVE_TABS.register(event);
    }

    }
