package net.mokai.quicksandrehydrated.networking.packet;
/*
import net.mokai.quicksandrehydrated.block.entity.MixerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;

import java.util.ArrayList;
import java.util.List;

public record ItemStackSyncS2CPacket(ItemStackHandler itemStackHandler, BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ItemStackSyncS2CPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "item_stack_sync_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackSyncS2CPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.collection(ArrayList::new, ItemStack.STREAM_CODEC),
                    packet -> {
                        List<ItemStack> list = new ArrayList<>();
                        for (int i = 0; i < packet.itemStackHandler().getSlots(); i++) {
                            list.add(packet.itemStackHandler().getStackInSlot(i));
                        }
                        return list;
                    },
                    BlockPos.STREAM_CODEC,
                    ItemStackSyncS2CPacket::pos,
                    (list, blockPos) -> {
                        ItemStackHandler handler = new ItemStackHandler(list.size());
                        for (int i = 0; i < list.size(); i++) {
                            handler.insertItem(i, list.get(i), false);
                        }
                        return new ItemStackSyncS2CPacket(handler, blockPos);
                    }
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ItemStackSyncS2CPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(payload.pos()) instanceof MixerBlockEntity blockEntity) {
                blockEntity.setHandler(payload.itemStackHandler());
            }
        });
    }
}

 */
