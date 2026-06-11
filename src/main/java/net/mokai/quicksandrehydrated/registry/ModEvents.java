package net.mokai.quicksandrehydrated.registry;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.entity.playerStruggling;
import net.mokai.quicksandrehydrated.networking.ModMessages;
import net.mokai.quicksandrehydrated.networking.packet.CoverageSyncS2CPacket;

@EventBusSubscriber(modid = QuicksandRehydrated.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void playerLogIn(PlayerEvent.PlayerLoggedInEvent e) {
        playerStruggling pS = (playerStruggling) e.getEntity();
        pS.syncCoverage();
    }

    @SubscribeEvent
    public static void playerStartTracking(PlayerEvent.StartTracking e) {
        Entity target = e.getTarget();
        if (target instanceof Player) {
            if (!target.level().isClientSide) {
                Player targetPlayer = (Player) target;
                playerStruggling pS = (playerStruggling) target;
                ServerPlayer observer = (ServerPlayer) e.getEntity();

                ModMessages.sendToPlayer(
                        new CoverageSyncS2CPacket(targetPlayer.getId(), pS.getCoverage()),
                        observer
                );
            }
        }
    }

}