package net.mokai.quicksandrehydrated.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.mokai.quicksandrehydrated.util.CameraBoxDimensions;
import org.apache.commons.lang3.tuple.Pair;


@OnlyIn(Dist.CLIENT)
@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {

    @Inject(method = "renderScreenEffect", at = @At("HEAD"))
    private static void renderScreenEffectHook(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
        Player player = minecraft.player;
        if (player != null && !player.noPhysics) {
            // Can hook in here for additional screen overlay hijinks.
            // Maybe a partial screen coverage after the player has resurfaced?
        }
    }

    @SuppressWarnings("unused")
    @Nullable
    private static Pair<BlockState, BlockPos> getOverlayBlock(Player player) {
        // Unaltered hitbox for suffocation. It's larger so we check this one first
        // Dimensions + condition check taken from Entity.java:isInWall
        var suffocatingIn = getOverlayBlockFor(player, player.getBbWidth() * 0.8d, 1.0e-6d, (blockState, blockPos, aabb) ->
            !blockState.isAir()
            && blockState.isSuffocating(player.level(), blockPos)
            && (
                Shapes.joinIsNotEmpty(
                    blockState.getCollisionShape(player.level(), blockPos).move(
                        (double)blockPos.getX(),
                        (double)blockPos.getY(),
                        (double)blockPos.getZ()
                    ),
                    Shapes.create(aabb),
                    BooleanOp.AND
                )
            )
        );
        if (suffocatingIn != null) {
            return suffocatingIn;
        }
        // Smaller, customised hitbox for drowning, check it second
        var drowningIn = getOverlayBlockFor(player, CameraBoxDimensions.FULL_WIDTH, CameraBoxDimensions.FULL_HEIGHT, (blockState, blockPos, aabb) ->
            blockState.getRenderShape() != RenderShape.INVISIBLE
            && blockState.isViewBlocking(player.level(), blockPos)
            // Need to handle view blocking blocks which aren't a full block (vanilla doesn't have any of these)
            // Currently only works if they're some form of cube or rectangular prism
            && blockState.getShape(player.level(), blockPos).bounds().move(blockPos).intersects(aabb)
        );
        if (drowningIn != null) {
            return drowningIn;
        }
        return null;
    }

    @Nullable
    private static Pair<BlockState, BlockPos> getOverlayBlockFor(Player player, double w, double h, Predicate condition) {
        AABB aabb = AABB.ofSize(player.getEyePosition(), w, h, w);
        return BlockPos
            .betweenClosedStream(aabb)
            .map(blockPos -> Pair.of(player.level().getBlockState(blockPos), blockPos))
            .filter(pair -> condition.test(pair.getLeft(), pair.getRight(), aabb))
            .findFirst()
            .orElse(null);
    }

    static interface Predicate {
        boolean test(BlockState blockState, BlockPos blockPos, AABB aabb);
    }
}
