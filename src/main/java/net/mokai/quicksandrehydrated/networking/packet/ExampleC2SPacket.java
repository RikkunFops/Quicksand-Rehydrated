package net.mokai.quicksandrehydrated.networking.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;

public record ExampleC2SPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ExampleC2SPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "example_c2s"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExampleC2SPacket> STREAM_CODEC =
            StreamCodec.unit(new ExampleC2SPacket());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ExampleC2SPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            ServerLevel level = player.serverLevel();

            // HERE WE ARE ON THE SERVER!
            // EntityType.COW.spawn(level, null, null, player.blockPosition(),
            //         MobSpawnType.COMMAND, true, false);
        });
    }
}
