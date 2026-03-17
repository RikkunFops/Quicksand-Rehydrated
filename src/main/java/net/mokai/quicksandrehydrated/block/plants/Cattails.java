package net.mokai.quicksandrehydrated.block.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.mokai.quicksandrehydrated.util.ModTags;
import org.jetbrains.annotations.NotNull;

public abstract class Cattails extends BushBlock implements BonemealableBlock {

    public static final IntegerProperty LAYER = IntegerProperty.create("layer", 0, 2);

    public Cattails(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LAYER, 0));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        if (state.getValue(LAYER) == 0) {
            return super.canSurvive(state, worldIn, pos);
        } else {
            BlockState blockstate = worldIn.getBlockState(pos.below());
            if (state.getBlock() != this) return super.canSurvive(state, worldIn, pos);
            return blockstate.getBlock() == this;
        }

    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        return blockpos.getY() < context.getLevel().getMaxBuildHeight() - 1 && context.getLevel().getBlockState(blockpos.above()).canBeReplaced(context) && context.getLevel().getBlockState(blockpos.above(2)).canBeReplaced(context) ? super.getStateForPlacement(context) : null;
    }

    public void placeAt(LevelAccessor worldIn, BlockPos pos, int flags) {
        worldIn.setBlock(pos, this.defaultBlockState().setValue(LAYER, 0), flags);
        worldIn.setBlock(pos.above(), this.defaultBlockState().setValue(LAYER, 1), flags);
        worldIn.setBlock(pos.above(2), this.defaultBlockState().setValue(LAYER, 2), flags);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        worldIn.setBlock(pos, this.defaultBlockState().setValue(LAYER, 0), 2);
        worldIn.setBlock(pos.above(), this.defaultBlockState().setValue(LAYER, 1), 2);
        worldIn.setBlock(pos.above(2), this.defaultBlockState().setValue(LAYER, 2), 2);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        int layer = state.getValue(LAYER);
        BlockPos base = pos.below(layer);

        boolean drop = !player.isCreative();

        level.destroyBlock(base, drop);
        level.destroyBlock(base.above(), false);
        level.destroyBlock(base.above(2), false);

        return super.playerWillDestroy(level, pos, state, player);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYER);
    }

    public boolean isValidBonemealTarget(LevelReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    public boolean isBonemealSuccess(Level worldIn, RandomSource rand, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel worldIn, RandomSource rand, BlockPos pos, BlockState state) {
        popResource(worldIn, pos, new ItemStack(this));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        return state.is(ModTags.Blocks.PEAT_BOG_BUSH) || super.mayPlaceOn(state, getter, pos);
    }
}