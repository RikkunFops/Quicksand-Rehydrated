package net.mokai.quicksandrehydrated.networking.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.mokai.quicksandrehydrated.networking.ClientPacketHandler;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;

public record StruggleResultS2CPacket(double amount) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StruggleResultS2CPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "struggle_result_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, StruggleResultS2CPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.DOUBLE,
                    StruggleResultS2CPacket::amount,
                    StruggleResultS2CPacket::new
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(StruggleResultS2CPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientPacketHandler.handleStruggleResult(payload.amount());
        });
    }
}
