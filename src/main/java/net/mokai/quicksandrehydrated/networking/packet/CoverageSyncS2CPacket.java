package net.mokai.quicksandrehydrated.networking.packet;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.entity.coverage.CoverageSerializer;
import net.mokai.quicksandrehydrated.entity.coverage.PlayerCoverage;
import net.mokai.quicksandrehydrated.networking.ClientPacketHandler;

public record CoverageSyncS2CPacket(int id, PlayerCoverage coverage) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CoverageSyncS2CPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "coverage_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CoverageSyncS2CPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> {
                        buf.writeInt(packet.id);
                        CompoundTag nbt = CoverageSerializer.serializeCoverage(packet.coverage);
                        buf.writeNbt(nbt);
                    },
                    (buf) -> {
                        int id = buf.readInt();
                        CompoundTag nbt = buf.readNbt();
                        PlayerCoverage coverage = CoverageSerializer.deserializeCoverage(nbt);
                        return new CoverageSyncS2CPacket(id, coverage);
                    }
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CoverageSyncS2CPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientPacketHandler.handleCoverageSync(payload.coverage, payload.id);
        });
    }
}
