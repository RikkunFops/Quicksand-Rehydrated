package net.mokai.quicksandrehydrated.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.registry.ModBlocks;
import net.mokai.quicksandrehydrated.registry.QuicksandRegistry;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, QuicksandRehydrated.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // NOTE: Blockstates are hand-crafted in src/main/resources/assets/qsrehydrated/blockstates/
        // They contain complex state-dependent variants (e.g., quicksand liquefaction, cattails layer)
        // Datagen would overwrite these with generic cube_all models.
        // Block state models are NOT generated - they must be maintained manually.
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
