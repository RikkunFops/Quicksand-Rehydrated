package net.mokai.quicksandrehydrated.networking.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.mokai.quicksandrehydrated.block.quicksands.core.QuicksandBase;
import net.mokai.quicksandrehydrated.networking.ModMessages;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;

import java.util.Random;
import java.util.UUID;

public record StruggleAttemptC2SPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StruggleAttemptC2SPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "struggle_attempt_c2s"));

    public static final StreamCodec<RegistryFriendlyByteBuf, StruggleAttemptC2SPacket> STREAM_CODEC =
            StreamCodec.unit(new StruggleAttemptC2SPacket());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(StruggleAttemptC2SPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            ServerLevel level = player.serverLevel();

            Block playerBlock = player.level().getBlockState(player.getOnPos()).getBlock();

            if (playerBlock instanceof QuicksandBase) {
                Random rand = new Random();
                double amount = rand.nextDouble(0.0, 1.0);

                player.addDeltaMovement(new Vec3(0.0, amount, 0.0));

                UUID playerId = player.getUUID();
                ModMessages.sendToPlayer(new StruggleResultS2CPacket(amount), player);
            }
        });
    }
}
