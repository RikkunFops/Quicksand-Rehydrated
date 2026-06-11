package net.mokai.quicksandrehydrated.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.mokai.quicksandrehydrated.registry.ModRecipes;



public record SinkingPotionConversionRecipe(Ingredient inputItem,
                                            ItemStack result) implements Recipe<SinkingConversionInput> {

    @Override
    public boolean matches(SinkingConversionInput input, Level level) {
        if (level.isClientSide()) {
            return false;
        }
        return inputItem.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(SinkingConversionInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.inputItem);
        return list;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SINKING_CONVERSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.SINKING_CONVERSION.get();
    }

    public static class SinkingPotionConversionSerializer implements RecipeSerializer<SinkingPotionConversionRecipe> {
        public static final MapCodec<SinkingPotionConversionRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(SinkingPotionConversionRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(SinkingPotionConversionRecipe::result)
        ).apply(inst, SinkingPotionConversionRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SinkingPotionConversionRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, SinkingPotionConversionRecipe::inputItem,
                        ItemStack.STREAM_CODEC, SinkingPotionConversionRecipe::result,
                        SinkingPotionConversionRecipe::new
                );


        @Override
        public MapCodec<SinkingPotionConversionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SinkingPotionConversionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

}

