package net.mokai.quicksandrehydrated.item;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mokai.quicksandrehydrated.screen.QuicksandBookScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class QuicksandBook extends Item {
    public QuicksandBook(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand hand) {
        ItemStack itemStack = pPlayer.getItemInHand(hand);
        if (pLevel.isClientSide) {
            this.openBookScreen();
        }
        return InteractionResultHolder.success(itemStack);
    }

    @OnlyIn(Dist.CLIENT)
    private void openBookScreen() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.setScreen(new QuicksandBookScreen());
    }
}
