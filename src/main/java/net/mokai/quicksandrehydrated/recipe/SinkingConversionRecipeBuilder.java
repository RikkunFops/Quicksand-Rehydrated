package net.mokai.quicksandrehydrated.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SinkingConversionRecipeBuilder implements RecipeBuilder {
    private final Ingredient inputItem;
    private final ItemStack result;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    
    public SinkingConversionRecipeBuilder(ItemStack result, Ingredient inputItem) {
        this.result = result;
        this.inputItem = inputItem;
    }

    // Factory method for single ingredient
    public static SinkingConversionRecipeBuilder of(ItemStack result, Ingredient ingredient, BlockState blockState) {
        return new SinkingConversionRecipeBuilder(result, ingredient);
    }

    // Factory method for item(s)
    public static SinkingConversionRecipeBuilder ofItems(ItemStack result, BlockState blockState, Item... items) {
        Ingredient combined = Ingredient.of(items);
        return new SinkingConversionRecipeBuilder(result, combined);
    }

    // Factory method for single block
    public static SinkingConversionRecipeBuilder ofBlock(ItemStack result, Ingredient ingredient, Block block) {
        return new SinkingConversionRecipeBuilder(result, ingredient);
    }

    // Builder pattern for complex configurations
    public static Builder builder(ItemStack result) {
        return new Builder(result);
    }

    public static class Builder {
        private final ItemStack result;
        private Ingredient inputItem;

        public Builder(ItemStack result) {
            this.result = result;
        }

        public Builder withIngredient(Ingredient ingredient) {
            this.inputItem = ingredient;
            return this;
        }

        public Builder withItems(Item... items) {
            this.inputItem = Ingredient.of(items);
            return this;
        }

        public Builder withTag(TagKey<Item> tag) {
            this.inputItem = Ingredient.of(tag);
            return this;
        }

        public SinkingConversionRecipeBuilder build() {
            if (this.inputItem == null) {
                throw new IllegalStateException("Input ingredient must be set before building");
            }
            return new SinkingConversionRecipeBuilder(result, inputItem);
        }
    }

    @Override
    public SinkingConversionRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String s) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
        Advancement.Builder advancement = recipeOutput.advancement()
                .rewards(AdvancementRewards.Builder.recipe(resourceLocation));
        
        // Add all stored criteria to the advancement
        for (Map.Entry<String, Criterion<?>> entry : criteria.entrySet()) {
            advancement.addCriterion(entry.getKey(), entry.getValue());
        }
        
        // Set requirements to match the criteria names
        if (!criteria.isEmpty()) {
            advancement.requirements(AdvancementRequirements.allOf(List.of(criteria.keySet().toArray(new String[0]))));
        } else {
            advancement.requirements(AdvancementRequirements.EMPTY);
        }
        
        SinkingPotionConversionRecipe recipe = new SinkingPotionConversionRecipe(this.inputItem, this.result);
        recipeOutput.accept(resourceLocation, recipe, advancement.build(resourceLocation.withPrefix("recipes/")));
    }


}
