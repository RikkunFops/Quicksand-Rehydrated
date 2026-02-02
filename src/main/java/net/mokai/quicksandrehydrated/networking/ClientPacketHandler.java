package net.mokai.quicksandrehydrated.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandler {

    public static void handleStruggleResult(double amount) {
        Player thisPlayer = Minecraft.getInstance().player;
        thisPlayer.addDeltaMovement(new Vec3(0.0, amount, 0.0));
    }

}
