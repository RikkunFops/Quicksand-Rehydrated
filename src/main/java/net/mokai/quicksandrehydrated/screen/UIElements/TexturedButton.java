package net.mokai.quicksandrehydrated.screen.UIElements;


import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TexturedButton extends Button {
    private final ResourceLocation texture;
    private final int textureU;
    private final int textureV;
    private final int textureUHovered;
    private final int textureVHovered;
    private final int textureWidth;
    private final int textureHeight;

    public TexturedButton(int x, int y, int width, int height,
                          int textureU, int textureV,
                          int textureUHovered, int textureVHovered,
                          int textureWidth, int textureHeight,
                          ResourceLocation texture,
                          Button.OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.texture = texture;
        this.textureU = textureU;
        this.textureV = textureV;
        this.textureUHovered = textureUHovered;
        this.textureVHovered = textureVHovered;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    // Button width / height = 15x10

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Use hovered coordinates if mouse is over button
        int u = this.isHoveredOrFocused() ? textureUHovered : textureU;
        int v = this.isHoveredOrFocused() ? textureVHovered : textureV;

        guiGraphics.blit(texture, this.getX(), this.getY(), u, v, this.width, this.height, textureWidth, textureHeight);
    }
}

