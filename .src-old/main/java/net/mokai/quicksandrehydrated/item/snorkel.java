package net.mokai.quicksandrehydrated.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


import javax.annotation.Nullable;

public class snorkel extends ArmorItem {

    public snorkel(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
    }

    /*   // Borrowed from Prehistoric Fauna. It had this bit of code which has nothing similar in our mod.
    	@Override
	public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
		consumer.accept((net.minecraftforge.client.extensions.common.IClientItemExtensions) PrehistoricFauna.PROXY.getArmorRenderProperties());
    }
     */

    @Override
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "prehistoricfauna:textures/models/armor/eggshell_helmet.png";
    }
}
