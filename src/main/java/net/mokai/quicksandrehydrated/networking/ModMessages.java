package net.mokai.quicksandrehydrated.networking;

import net.minecraft.world.entity.Entity;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.networking.packet.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = QuicksandRehydrated.MOD_ID)
public class ModMessages {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        // C2S Packets
        registrar.playToServer(
                ExampleC2SPacket.TYPE,
                ExampleC2SPacket.STREAM_CODEC,
                ExampleC2SPacket::handle
        );

        registrar.playToServer(
                StruggleAttemptC2SPacket.TYPE,
                StruggleAttemptC2SPacket.STREAM_CODEC,
                StruggleAttemptC2SPacket::handle
        );

        registrar.playToServer(
                StruggleDownC2SPacket.TYPE,
                StruggleDownC2SPacket.STREAM_CODEC,
                StruggleDownC2SPacket::handle
        );

        registrar.playToServer(
                StruggleReleaseC2SPacket.TYPE,
                StruggleReleaseC2SPacket.STREAM_CODEC,
                StruggleReleaseC2SPacket::handle
        );
/*
        // S2C Packets
        registrar.playToClient(
                FluidSyncS2CPacket.TYPE,
                FluidSyncS2CPacket.STREAM_CODEC,
                FluidSyncS2CPacket::handle
        );

        registrar.playToClient(
                ItemStackSyncS2CPacket.TYPE,
                ItemStackSyncS2CPacket.STREAM_CODEC,
                ItemStackSyncS2CPacket::handle
        );
*/
        registrar.playToClient(
                CoverageSyncS2CPacket.TYPE,
                CoverageSyncS2CPacket.STREAM_CODEC,
                CoverageSyncS2CPacket::handle
        );

        registrar.playToClient(
                StruggleResultS2CPacket.TYPE,
                StruggleResultS2CPacket.STREAM_CODEC,
                StruggleResultS2CPacket::handle
        );


    }

    public static <MSG extends CustomPacketPayload> void sendToServer(MSG message) {
        PacketDistributor.sendToServer(message);
    }

    public static <MSG extends CustomPacketPayload> void sendToPlayer(MSG message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

    public static <MSG extends CustomPacketPayload> void sendToClients(MSG message) {
        PacketDistributor.sendToAllPlayers(message);
    }
    public static <MSG extends  CustomPacketPayload> void sendToClientTrackingAndSelf(MSG message, Entity entity) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
    }
}
