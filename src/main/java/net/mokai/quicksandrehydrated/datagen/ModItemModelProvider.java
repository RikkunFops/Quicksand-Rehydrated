package net.mokai.quicksandrehydrated.datagen;

import net.minecraft.data.PackOutput;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.registry.ModBlocks;
import net.mokai.quicksandrehydrated.registry.ModItems;
import net.mokai.quicksandrehydrated.registry.QuicksandRegistry;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, QuicksandRehydrated.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Block items generated through BlockStateProvider will auto-generate models
        // This provider can be used for any custom item models or overrides
        
        // Example: if you have standalone items (non-block items), register them here
        // simpleItem(ModItems.SOME_ITEM);
    }

    private void simpleItem(DeferredItem<?> item) {
        withExistingParent(item.getId().getPath(), mcLoc("item/handheld"))
                .texture("layer0", modLoc("item/" + item.getId().getPath()));
    }
}
