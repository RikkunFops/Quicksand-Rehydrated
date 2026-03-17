package net.mokai.quicksandrehydrated.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;

import net.mokai.quicksandrehydrated.entity.EntityBubble;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.mokai.quicksandrehydrated.QuicksandRehydrated;

public class ModEntityTypes {

	public static final DeferredRegister<EntityType<?>> ENTITIES =
			DeferredRegister.create(Registries.ENTITY_TYPE, QuicksandRehydrated.MOD_ID);

	public static final DeferredRegister<Item> SPAWN_EGGS =
			DeferredRegister.create(Registries.ITEM, QuicksandRehydrated.MOD_ID);



	public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
			DeferredRegister.create(Registries.VILLAGER_PROFESSION, QuicksandRehydrated.MOD_ID);


	public static final DeferredHolder<EntityType<?>, EntityType<EntityBubble>> BUBBLE =
			ENTITIES.register("bubble", () -> EntityType.Builder.<EntityBubble>of(EntityBubble::new, MobCategory.MISC)
							.sized(1f, 1f)
							.fireImmune()
							.noSave()
							.build("bubble")
			);



	public static void register(IEventBus eventBus) {
		ENTITIES.register(eventBus);
		SPAWN_EGGS.register(eventBus);
		VILLAGER_PROFESSIONS.register(eventBus);
	}
}