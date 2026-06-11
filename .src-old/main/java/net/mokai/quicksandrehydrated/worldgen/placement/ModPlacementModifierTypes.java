package net.mokai.quicksandrehydrated.worldgen.placement;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Register for custom placement modifier types
 */
public class ModPlacementModifierTypes {
    // Create a deferred registry for placement modifiers
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = 
            DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, QuicksandRehydrated.MOD_ID);
    
    // Register our custom placement modifier
    public static final RegistryObject<PlacementModifierType<QuicksandPitPlacement>> QUICKSAND_PIT_PLACEMENT = 
            PLACEMENT_MODIFIERS.register("quicksand_pit_placement", 
                    () -> () -> QuicksandPitPlacement.CODEC);
    
    /**
     * Register placement modifiers with the Forge event bus
     */
    public static void register(IEventBus eventBus) {
        PLACEMENT_MODIFIERS.register(eventBus);
    }
}