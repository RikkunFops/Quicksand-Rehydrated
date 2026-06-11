package net.mokai.quicksandrehydrated.client.render;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

import static net.mokai.quicksandrehydrated.QuicksandRehydrated.MOD_ID;

/**
 * Central place to declare/bake model layer locations.
 * Keep the names in sync with your ModEntitySetup registrations.
 */
public final class ModModelLayers {

    // Existing ones in your project
    public static final ModelLayerLocation HUNNIBEE_LAYER   = layer("hunnibee_layer");
    public static final ModelLayerLocation TAR_GOLEM_LAYER  = layer("tar_golem_layer");

    // Cave Blob layers (inner = solid, outer = translucent shell)
    public static final ModelLayerLocation CAVE_BLOB_SOLID_LAYER = layer("cave_blob_solid_layer");
    public static final ModelLayerLocation CAVE_BLOB_CLEAR_LAYER = layer("cave_blob_clear_layer");

    // (Optional) If you like having Bubble here too:
    // public static final ModelLayerLocation BUBBLE = layer("bubble");

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(new ResourceLocation(MOD_ID, name), "main");
    }

    private ModModelLayers() {}
}
