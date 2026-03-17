package net.mokai.quicksandrehydrated.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.block.quicksands.Quicksand;
import net.mokai.quicksandrehydrated.block.quicksands.core.QuicksandBehavior;
import net.mokai.quicksandrehydrated.block.quicksands.core.QuicksandBase;
import net.mokai.quicksandrehydrated.entity.data.QuicksandWobbleMEffect;
import net.mokai.quicksandrehydrated.entity.data.QuicksandWobblePEffect;
import net.mokai.quicksandrehydrated.util.BodyDepthThreshold;
import net.mokai.quicksandrehydrated.util.DepthCurve;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static net.mokai.quicksandrehydrated.util.DepthCurve.Vec2;

public class QuicksandRegistry {

    // ------------------ BLOCK REGISTER --------------------
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(QuicksandRehydrated.MOD_ID);

    // ------------------ BLOCK PROPERTIES -----------------
    private static final BlockBehaviour.Properties baseBlockBehavior =
            BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).noCollission().forceSolidOn().isViewBlocking((a,b,c)->true);
    private static final BlockBehaviour.Properties muddyBlockBehavior =
            BlockBehaviour.Properties.ofFullCopy(Blocks.MUD).requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties baseFlowingBlockBehavior =
            BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties slimeBlockBehavior =
            BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).friction(1.0F).strength(2.5F);
    private static final BlockBehaviour.Properties woolBlockBehavior =
            BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).friction(1.0F).strength(2.5F);

    // ------------------ QUICKSAND BEHAVIORS -----------------
    static QuicksandBehavior BaseQuicksandBehavior = new QuicksandBehavior()
            .setVertSpeed(0.4)
            .setWalkSpeed(new DepthCurve(new double[]{0.9,0.55,0.15,0.1,0.0}))
            .setSinkSpeed(0.2)
            .setBuoyancyPoint(BodyDepthThreshold.KNEE.depth)
            .setCoverageTexture("quicksand_coverage")
            .setResurfingForce(0.025);

    // ------------------ BLOCK REGISTRATION -----------------
    public static final DeferredBlock<Block> QUICKSAND =
            registerBlock("quicksand",
                    () -> new Quicksand(
                            baseBlockBehavior.randomTicks(),
                            BaseQuicksandBehavior
                    ));
            /*() -> new Quicksand(
                    baseBlockBehavior.randomTicks(),
                    new QuicksandBehavior()
                            .setCoverageTexture("quicksand_coverage")
                            .setSinkSpeed(0.0005d)
                            .setVertSpeed(0.1d)
                            .setWalkSpeed(new DepthCurve(0.9, 0.1))
                            .setBuoyancyPoint(BodyDepthThreshold.KNEE.depth)
                            .setResurfingForce(0.03)
            */

    // ------------------ CREATIVE LIST ---------------------------

    public static Collection<ItemStack> setupCreativeGroup() {
        CREATIVELIST = new ArrayList<>();
        addItem(QUICKSAND);

        return CREATIVELIST;
    }

    public static Collection<ItemStack> CREATIVELIST;

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