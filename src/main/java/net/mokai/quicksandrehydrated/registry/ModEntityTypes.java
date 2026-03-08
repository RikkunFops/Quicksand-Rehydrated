package net.mokai.quicksandrehydrated.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.entity.*;
import java.util.function.Supplier;

public class ModEntityTypes {
	public static final DeferredRegister<EntityType<?>> ENTITIES =
	        DeferredRegister.create(Registries.ENTITY_TYPE, QuicksandRehydrated.MOD_ID);

	public static final DeferredRegister<Item> SPAWN_EGGS =
	        DeferredRegister.create(Registries.ITEM, QuicksandRehydrated.MOD_ID);

	public static final DeferredRegister<PoiType> POI_TYPES =
	        DeferredRegister.create(Registries.POI_TYPE, QuicksandRehydrated.MOD_ID);

	public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
	        DeferredRegister.create(Registries.VILLAGER_PROFESSION, QuicksandRehydrated.MOD_ID);
}
