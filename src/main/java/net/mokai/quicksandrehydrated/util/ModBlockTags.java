package net.mokai.quicksandrehydrated.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;

public class ModBlockTags {
    
    // Custom qsrehydrated namespace tags
    public static final TagKey<Block> QUICKSAND_DROWNABLE = 
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "quicksand_drownable"));
    
    public static final TagKey<Block> DUCKWEED = 
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "duckweed"));
    
    public static final TagKey<Block> QUICKSAND_AO_OVERRIDE = 
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "quicksand_ao_override"));
    
    public static final TagKey<Block> PEAT_BOG_BUSH = 
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "peat_bog_bush"));
    
    public static final TagKey<Block> FERN_BUSH = 
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "fern_bush"));
}
