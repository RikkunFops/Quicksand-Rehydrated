package net.mokai.quicksandrehydrated.networking.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.mokai.quicksandrehydrated.entity.playerStruggling;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;

public record StruggleDownC2SPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StruggleDownC2SPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "struggle_down_c2s"));

    public static final StreamCodec<RegistryFriendlyByteBuf, StruggleDownC2SPacket> STREAM_CODEC =
            StreamCodec.unit(new StruggleDownC2SPacket());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(StruggleDownC2SPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();

            if (!(player instanceof playerStruggling strugglingPlayer)) {
                System.err.println("[QuicksandRehydrated] StruggleDownC2SPacket received for player without playerStruggling mixin: " + player.getGameProfile().getName());
                return;
            }

            strugglingPlayer.setHoldingStruggle(true);
        });
    }
}
