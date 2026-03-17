package net.mokai.quicksandrehydrated.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class Keybinding {

    public static final String KEY_CATEGORY_QUICKSAND = "key.category.quicksandrehydrated.quicksand";
    public static final String KEY_STRUGGLE = "key.quicksandrehydrated.struggle";

    // NeoForge 1.21.1 keybinding: no KeyConflictContext
    public static final KeyMapping STRUGGLE_KEY = new KeyMapping(
            KEY_STRUGGLE,                // translation key
            InputConstants.Type.KEYSYM,  // key type
            GLFW.GLFW_KEY_SPACE,         // default key
            KEY_CATEGORY_QUICKSAND       // category
    );
}