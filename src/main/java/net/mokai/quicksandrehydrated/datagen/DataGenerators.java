package net.mokai.quicksandrehydrated.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@EventBusSubscriber(modid = QuicksandRehydrated.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Server-side data generators
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));

        generator.addProvider(event.includeServer(), new ModDatapackProvider(output, lookupProvider));
        
        ModBlockTagProvider blockTagProvider = new ModBlockTagProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagProvider);
        generator.addProvider(event.includeServer(), new ModItemTagProvider(output, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper));
        
        // Client-side data generators
        // DISABLED: ModBlockStateProvider - blockstates are hand-crafted with complex state variants
        // generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFileHelper));
    }
}