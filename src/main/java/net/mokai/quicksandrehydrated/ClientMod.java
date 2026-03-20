package net.mokai.quicksandrehydrated;

import net.mokai.quicksandrehydrated.util.Keybinding;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class ClientMod {

    @SubscribeEvent
    public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(Keybinding.STRUGGLE_KEY);
    }
}