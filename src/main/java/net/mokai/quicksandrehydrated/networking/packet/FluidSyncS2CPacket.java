
/*
package net.mokai.quicksandrehydrated.networking.packet;

import net.mokai.quicksandrehydrated.block.entity.MixerBlockEntity;
import net.mokai.quicksandrehydrated.screen.MixerMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;

public record FluidSyncS2CPacket(FluidStack fluidStack, BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<FluidSyncS2CPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "fluid_sync_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidSyncS2CPacket> STREAM_CODEC =
            StreamCodec.composite(
                    FluidStack.STREAM_CODEC,
                    FluidSyncS2CPacket::fluidStack,
                    BlockPos.STREAM_CODEC,
                    FluidSyncS2CPacket::pos,
                    FluidSyncS2CPacket::new
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(FluidSyncS2CPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(payload.pos()) instanceof MixerBlockEntity blockEntity) {
                blockEntity.setInFluid(payload.fluidStack());

                if (Minecraft.getInstance().player.containerMenu instanceof MixerMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(payload.pos())) {
                    menu.setFluid(payload.fluidStack());
                }
            }
        });
    }
}
*/