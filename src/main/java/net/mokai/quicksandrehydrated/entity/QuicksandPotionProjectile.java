package net.mokai.quicksandrehydrated.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mokai.quicksandrehydrated.recipe.SinkingConversionInput;
import net.mokai.quicksandrehydrated.recipe.SinkingPotionConversionRecipe;
import net.mokai.quicksandrehydrated.registry.ModRecipes;

import java.util.Optional;


public class QuicksandPotionProjectile extends ThrownPotion {

    public QuicksandPotionProjectile(Level pLevel, LivingEntity pShooter) {
        super(pLevel, pShooter);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (!this.level().isClientSide) {
            BlockPos center = BlockPos.containing(pResult.getLocation());
            int radius = 4;

            for (int x = center.getX() - radius; x <= center.getX() + radius; x++) {
                for (int z = center.getZ() - radius; z <= center.getZ() + radius; z++) {
                    double dx = x - center.getX();
                    double dz = z - center.getZ();
                    if (dx * dx + dz * dz < 7) {
                        for (int y = center.getY() - 4; y <= center.getY() + 2; y++) {
                            affectBlock(new BlockPos(x, y, z));
                        }
                    }
                }
            }

            this.level().levelEvent(2007, this.blockPosition(),
                    PotionContents.getColor(Potions.TURTLE_MASTER));
            this.discard();
        }
    }

    public InteractionResult affectBlock(BlockPos pPos) {
        Level level = this.level();
        BlockState blockstate = level.getBlockState(pPos);

        Item blockRawItem = blockstate.getBlock().asItem();
        if (blockRawItem == Items.AIR) return InteractionResult.PASS;

        ItemStack blockItemStack = blockRawItem.getDefaultInstance();
        RecipeManager recipes = level.getRecipeManager();

        SinkingConversionInput input = new SinkingConversionInput(blockItemStack);
        Optional<RecipeHolder<SinkingPotionConversionRecipe>> optional = recipes.getRecipeFor(
                ModRecipes.SINKING_CONVERSION.get(),
                input,
                level
        );

        if (optional.isPresent()) {
            ItemStack resultStack = optional.get().value().result();
            Item resultItem = resultStack.getItem();

            if (resultItem instanceof BlockItem blockItem) {
                level.setBlockAndUpdate(pPos, blockItem.getBlock().defaultBlockState());
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
