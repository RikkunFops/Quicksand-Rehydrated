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
        // Original implementation uses a MutableBlockPos, possibly for performance reasons?
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        double w = CameraBoxDimensions.HALF_WIDTH;
        double h = CameraBoxDimensions.HALF_HEIGHT;
        // Imagine a small box where the player's eye is.
        // We check each corner of this box to see if it's inside a block. If it is, we obscure the screen.
        for (int i = 0; i < 8; ++i) {
            // Originally, the x and z axis were using player.getBbWidth() * 0.8 which is way larger than we wanted lol
            double x = ((i & 1) == 0 ? -w : w) + player.getX();
            double y = ((i & 2) == 0 ? -h : h) + player.getEyeY();
            double z = ((i & 4) == 0 ? -w : w) + player.getZ();
            mutableBlockPos.set(x, y, z);
            BlockState blockState = player.level().getBlockState(mutableBlockPos);
            if (blockState.getRenderShape() != RenderShape.INVISIBLE && blockState.isViewBlocking(player.level(), mutableBlockPos)) {
                // Need to handle view blocking blocks which aren't a full block (vanilla doesn't have any of these)
                // Currently only works if they're some form of cube or rectangular prism
                if (blockState.getShape(player.level(), mutableBlockPos).bounds().move(mutableBlockPos).contains(x, y, z)) {
                    return Pair.of(blockState, mutableBlockPos.immutable());
                }
            }
        }
        return null;
    }
}
