package net.mokai.quicksandrehydrated.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.recipe.SinkingPotionConversionRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, QuicksandRehydrated.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, QuicksandRehydrated.MOD_ID);

    public static final DeferredHolder<RecipeType<?>,RecipeType<SinkingPotionConversionRecipe>> SINKING_CONVERSION =
            RECIPE_TYPES.register(
                    "sinking_potion_conversion",
                    () -> new RecipeType<SinkingPotionConversionRecipe>() {
                        @Override
                        public String toString() {
                            return "sinking_potion_conversion";
                        }
                    }
            );

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SinkingPotionConversionRecipe>> SINKING_CONVERSION_SERIALIZER =
            RECIPE_SERIALIZERS.register("sinking_potion_conversion", SinkingPotionConversionRecipe.SinkingPotionConversionSerializer::new);

    public static void register(IEventBus modBus) {
        RECIPE_SERIALIZERS.register(modBus);
        RECIPE_TYPES.register(modBus);
    }
}
