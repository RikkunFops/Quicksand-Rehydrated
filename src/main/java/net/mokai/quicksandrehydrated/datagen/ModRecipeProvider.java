package net.mokai.quicksandrehydrated.datagen;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.recipe.SinkingConversionRecipeBuilder;
import net.mokai.quicksandrehydrated.recipe.SinkingPotionConversionRecipe;
import net.mokai.quicksandrehydrated.registry.ModRecipes;
import net.mokai.quicksandrehydrated.registry.QuicksandRegistry;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        SinkingConversionRecipeBuilder.builder(new ItemStack(QuicksandRegistry.QUICKSAND.get()))
                .withTag(ItemTags.SAND)
                .build()
                .unlockedBy("has_sand", has(Blocks.SAND))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "quicksand_from_sand"));
        SinkingConversionRecipeBuilder.builder(new ItemStack(QuicksandRegistry.BOTTOMLESS_MUD.get()))
                .withItems(QuicksandRegistry.DEEP_MUD.asItem())
                .build()
                .unlockedBy("has_sand", has(Blocks.SAND))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "bottomlessmud_from_deepmud"));
        SinkingConversionRecipeBuilder.builder(new ItemStack(QuicksandRegistry.PEAT_BOG.get()))
                .withItems(Blocks.DIRT.asItem())
                .build()
                .unlockedBy("has_sand", has(Blocks.SAND))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "peatbog_from_dirt"));
        SinkingConversionRecipeBuilder.builder(new ItemStack(QuicksandRegistry.MOSSY_PEAT_BOG.get()))
                .withItems(Blocks.GRASS_BLOCK.asItem())
                .build()
                .unlockedBy("has_sand", has(Blocks.SAND))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "mossypeat_from_grass"));
        SinkingConversionRecipeBuilder.builder(new ItemStack(QuicksandRegistry.THIN_MUD.get()))
                .withItems(Blocks.MUD.asItem())
                .build()
                .unlockedBy("has_sand", has(Blocks.SAND))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "thinmud_from_mud"));
        SinkingConversionRecipeBuilder.builder(new ItemStack(Blocks.MUD.asItem()))
                .withItems(Blocks.POPPY.asItem())
                .build()
                .unlockedBy("has_sand", has(Blocks.SAND))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "rafflesia_from_poppy"));
        SinkingConversionRecipeBuilder.builder(new ItemStack(QuicksandRegistry.SHALLOW_MUD.get()))
                .withItems(QuicksandRegistry.DEEP_MUD.asItem())
                .build()
                .unlockedBy("has_sand", has(Blocks.SAND))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "deepmud_from_shallowmud"));
        SinkingConversionRecipeBuilder.builder(new ItemStack(QuicksandRegistry.SHALLOW_MUD.get()))
                .withItems(QuicksandRegistry.THIN_MUD.asItem())
                .build()
                .unlockedBy("has_sand", has(Blocks.SAND))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "shallowmud_from_thinmud"));
        SinkingConversionRecipeBuilder.builder(new ItemStack(QuicksandRegistry.LIVING_SLIME.get()))
                .withItems(Blocks.SLIME_BLOCK.asItem())
                .build()
                .unlockedBy("has_sand", has(Blocks.SAND))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "livingslime_from_slimeblock"));





    }
}
