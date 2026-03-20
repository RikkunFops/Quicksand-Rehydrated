package net.mokai.quicksandrehydrated.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(QuicksandRehydrated.MOD_ID);

    //public static final RegistryObject<Block> SOFT_COVER = registerBlock("loose_moss", () -> new GoundCover(BlockBehaviour.Properties.copy(Blocks.MOSS_CARPET)));
    //Plant and stuff


    public static final DeferredBlock<Block> DUCKWEED = registerBlock("duckweed", () -> new net.mokai.quicksandrehydrated.block.plants.Duckweed(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_CARPET).noCollission().replaceable().sound(SoundType.GRASS).instabreak()));
    public static final DeferredBlock<Block> DUCKWEED_FLOWERS = registerBlock("duckweed_flowers", () -> new net.mokai.quicksandrehydrated.block.plants.Duckweed(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_CARPET).noCollission().replaceable().sound(SoundType.GRASS).instabreak()));
    public static final DeferredBlock<Block> PEAT_BOG_BUSH = registerBlock("peat_bog_bush", () -> new net.mokai.quicksandrehydrated.block.plants.PeatBogBush(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)));
    public static final DeferredBlock<Block> FERN_BUSH = registerBlock("fern_bush", () -> new net.mokai.quicksandrehydrated.block.plants.FernBush(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)));
    public static final DeferredBlock<Block> CATTAIL_REEDS = registerBlock("cattails", () -> new net.mokai.quicksandrehydrated.block.plants.Cattails(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)));
    public static final DeferredBlock<Block> BRANCH = registerBlock("muddy_branch", () -> new net.mokai.quicksandrehydrated.block.plants.MuddyBranch(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noCollission().noOcclusion().instabreak()));
    //public static final DeferredBlock<Block> CRANBERRY_BUSH = BLOCKS.register("cranberry_bush", () -> new net.mokai.quicksandrehydrated.block.plants.CranBerryBush(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).noOcclusion()));



    public static Collection<ItemStack> setupCreativeGroups() {
        CREATIVELIST = new ArrayList<>();

        //Plant, crops and flowers//

        addItem(PEAT_BOG_BUSH);
        addItem(DUCKWEED);
        addItem(DUCKWEED_FLOWERS);
        addItem(FERN_BUSH);
        addItem(BRANCH);
        addItem(CATTAIL_REEDS);
        System.out.println("NATURAL CREATIVE TAB");
        return CREATIVELIST;
    }





    // ----------------------------------- Done! -----------------------------


    private static Collection<ItemStack> CREATIVELIST;

    public static void addItem(DeferredBlock<?> block) {
        CREATIVELIST.add(new ItemStack(block.get()));
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    // ------------------ BLOCK ITEM REGISTRATION -----------------
    private static <T extends Block> DeferredHolder<Item, Item> registerBlockItem(String name, Supplier<T> blockHolder) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(blockHolder.get(), new Item.Properties()));
    }

    // ------------------ REGISTER TO EVENT BUS -----------------
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }


}
