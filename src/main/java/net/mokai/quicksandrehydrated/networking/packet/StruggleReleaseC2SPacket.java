package net.mokai.quicksandrehydrated.networking.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.mokai.quicksandrehydrated.entity.playerStruggling;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;

public record StruggleReleaseC2SPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StruggleReleaseC2SPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "struggle_release_c2s"));

    public static final StreamCodec<RegistryFriendlyByteBuf, StruggleReleaseC2SPacket> STREAM_CODEC =
            StreamCodec.unit(new StruggleReleaseC2SPacket());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(StruggleReleaseC2SPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();

            if (!(player instanceof playerStruggling strugglingPlayer)) {
                System.err.println("[QuicksandRehydrated] StruggleReleaseC2SPacket received for player without playerStruggling mixin: " + player.getGameProfile().getName());
                return;
            }

            strugglingPlayer.BeginStruggle();
            strugglingPlayer.setHoldingStruggle(false);
        });
    }
}
