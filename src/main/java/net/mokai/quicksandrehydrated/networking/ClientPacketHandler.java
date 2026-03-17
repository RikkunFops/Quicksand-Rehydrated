package net.mokai.quicksandrehydrated.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.mokai.quicksandrehydrated.entity.playerStruggling;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.mokai.quicksandrehydrated.entity.coverage.PlayerCoverage;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandler {

    public static void handleStruggleResult(double amount) {
        Player thisPlayer = Minecraft.getInstance().player;
        if (thisPlayer != null) {
            thisPlayer.addDeltaMovement(new Vec3(0.0, amount, 0.0));
        }
    }

    public static void handleCoverageSync(PlayerCoverage coverage, int entityId) {
        ClientLevel level = Minecraft.getInstance().level;

        if (level != null) {
            Entity entity = level.getEntity(entityId);

            if (entity instanceof Player) {
                Player player = (Player) entity;
                playerStruggling pS = (playerStruggling) player;
                pS.replaceCoverage(coverage);
            }
        }
    }

}
