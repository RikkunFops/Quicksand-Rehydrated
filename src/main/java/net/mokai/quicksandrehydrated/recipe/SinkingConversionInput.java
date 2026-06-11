package net.mokai.quicksandrehydrated.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.state.BlockState;

public record SinkingConversionInput(ItemStack stack) implements RecipeInput {

    @Override
    public ItemStack getItem(int slot) {
        return stack;
    }

    @Override
    public int size() {
        return 1;
    }
}
