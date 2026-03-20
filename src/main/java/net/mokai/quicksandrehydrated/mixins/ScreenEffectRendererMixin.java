package net.mokai.quicksandrehydrated.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
import net.mokai.quicksandrehydrated.util.CameraBoxDimensions;
import org.apache.commons.lang3.tuple.Pair;
import java.util.stream.Stream;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {

    @Inject(method = "renderScreenEffect", at = @At("HEAD"))
    private static void renderScreenEffectHook(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
        Player player = minecraft.player;
        if (player != null && !player.noPhysics) {
            // Additional screen overlay logic here
        }
    }

    @Unique
    @Nullable
    private static Pair<BlockState, BlockPos> getOverlayBlock(Player player) {
        // Check suffocating
        var suffocatingIn = getOverlayBlockFor(player, player.getBbWidth() * 0.8d, 1.0e-6d, (blockState, blockPos, aabb) ->
                !blockState.isAir()
                        && blockState.isSuffocating(player.level(), blockPos)
                        && Shapes.joinIsNotEmpty(
                        blockState.getCollisionShape(player.level(), blockPos).move(blockPos.getX(), blockPos.getY(), blockPos.getZ()),
                        Shapes.create(aabb),
                        BooleanOp.AND
                )
        );
        if (suffocatingIn != null) return suffocatingIn;

        // Check drowning
        var drowningIn = getOverlayBlockFor(player, CameraBoxDimensions.FULL_WIDTH, CameraBoxDimensions.FULL_HEIGHT, (blockState, blockPos, aabb) ->
                blockState.getRenderShape() != RenderShape.INVISIBLE
                        && blockState.isViewBlocking(player.level(), blockPos)
                        && blockState.getShape(player.level(), blockPos).bounds().move(blockPos.getX(), blockPos.getY(), blockPos.getZ()).intersects(aabb)
        );
        return drowningIn;
    }

    @Unique
    @Nullable
    private static Pair<BlockState, BlockPos> getOverlayBlockFor(Player player, double w, double h, TriPredicate condition) {
        AABB aabb = AABB.ofSize(player.getEyePosition(), w, h, w);
        return BlockPos.betweenClosedStream(aabb)
                .map(blockPos -> Pair.of(player.level().getBlockState(blockPos), blockPos))
                .filter(pair -> condition.test(pair.getLeft(), pair.getRight(), aabb))
                .findFirst()
                .orElse(null);
    }



}

interface TriPredicate {
    boolean test(BlockState blockState, BlockPos blockPos, AABB aabb);
}