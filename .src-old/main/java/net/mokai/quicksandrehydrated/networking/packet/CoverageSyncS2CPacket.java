package net.mokai.quicksandrehydrated.networking.packet;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.mokai.quicksandrehydrated.entity.coverage.CoverageSerializer;
import net.mokai.quicksandrehydrated.entity.coverage.PlayerCoverage;
import net.mokai.quicksandrehydrated.networking.ClientPacketHandler;

import java.util.function.Supplier;

public class CoverageSyncS2CPacket {

    private final int id;
    private final PlayerCoverage coverage;

    public CoverageSyncS2CPacket(int id, PlayerCoverage cover) {
        this.id = id;
        this.coverage = cover;
    }

    public CoverageSyncS2CPacket(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        CompoundTag nbt = buf.readNbt();
        PlayerCoverage coverage = CoverageSerializer.deserializeCoverage(nbt);
        this.coverage = coverage;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        CompoundTag nbt = CoverageSerializer.serializeCoverage(this.coverage);
        buf.writeNbt(nbt);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientPacketHandler.handleCoverageSync(this.coverage, this.id);
        });
        return true;
    }
}
