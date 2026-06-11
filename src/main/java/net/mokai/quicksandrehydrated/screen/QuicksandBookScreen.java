package net.mokai.quicksandrehydrated.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.mokai.quicksandrehydrated.QuicksandRehydrated;
import net.mokai.quicksandrehydrated.screen.UIElements.TexturedButton;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * A simple book screen for displaying quicksand information.
 * This is a basic implementation - you can expand it with multiple pages, images, etc.
 */
@OnlyIn(Dist.CLIENT)
public class QuicksandBookScreen extends Screen {
    private static final int BOOK_WIDTH = 272;
    private static final int BOOK_HEIGHT = 180;
    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 256;
    private static final int ATLAS_WIDTH = 512;
    private static final int ATLAS_HEIGHT = 512;

    private static final ResourceLocation BOOK_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(QuicksandRehydrated.MOD_ID, "textures/gui/mess_book.png");

    private int currentPage = 0;
    private int totalPages = PAGE_CONTENT.length; // Adjust based on your content

    // Book content for each page
    private static final String[] PAGE_TITLES = {
            "Quicksand Types",
            "Behavior",
            "Drowning",
            "Tips"
    };

    private static final String[][] PAGE_CONTENT = {
            {
                    "This is a test for the quicksand book"
            },
            {
                    "All quicksand behaves",
                    "similarly - you will sink",
                    "gradually and begin to",
                    "drown if you cannot escape.",
                    "",
                    "Movement is slowed",
                    "significantly in quicksand."
            },
            {
                    "If your head is submerged",
                    "in quicksand, you will start",
                    "drowning. Your air meter",
                    "will deplete rapidly.",
                    ""
            },
            {
                    "Pro Tips:",
                    "- Use reeds to escape",
                    "- Wear light armor to move",
                    "  faster in quicksand",
                    "- Avoid deep quicksand",
                    "- Always carry a reed!",
                    "- Stay calm and escape!"
            }
    };

    public QuicksandBookScreen() {
        super(Component.literal("Quicksand Encyclopedia"));
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();

        int centerX = (this.width - BOOK_WIDTH) / 2;
        int centerY = (this.height - BOOK_HEIGHT) / 2;

        /*      Removed until I find a good close button texture
        // Close button at bottom right
        this.addRenderableWidget(new TexturedButton(
                centerX + BOOK_WIDTH - 40, centerY + BOOK_HEIGHT - 25, 15, 10,
                0, 0, 0, 20,  // TODO: Update UV coordinates for close button
                ATLAS_WIDTH, ATLAS_HEIGHT,
                BOOK_TEXTURE,
                button -> this.onClose()
        ));
        */
        // Previous page button
        if (currentPage > 0) {
            this.addRenderableWidget(new TexturedButton(
                    centerX + 25, centerY + BOOK_HEIGHT - 25, 18, 10,
                    99, 195, 99, 221,  // TODO: Update UV coordinates for prev button
                    ATLAS_WIDTH, ATLAS_HEIGHT,
                    BOOK_TEXTURE,
                    button -> {
                        if (currentPage > 0) {
                            currentPage--;
                            this.init();
                        }
                    }
            ));
        }

        // Next page button
        if (currentPage < totalPages - 1) {
            this.addRenderableWidget(new TexturedButton(
                    centerX + BOOK_WIDTH - 45, centerY + BOOK_HEIGHT - 25, 18, 10,
                    99, 182, 99, 208,  // TODO: Update UV coordinates for next button
                    ATLAS_WIDTH, ATLAS_HEIGHT,
                    BOOK_TEXTURE,
                    button -> {
                        if (currentPage < totalPages - 1) {
                            currentPage++;
                            this.init();
                        }
                    }
            ));
        }
    }


    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        // Draw book background (light brown/paper color)
        int centerX = (this.width - BOOK_WIDTH) / 2;
        int centerY = (this.height - BOOK_HEIGHT) / 2;
        guiGraphics.blit(BOOK_TEXTURE, centerX, centerY, 0, 0, BOOK_WIDTH, BOOK_HEIGHT, ATLAS_WIDTH, ATLAS_HEIGHT);

        // Draw title
        String title = PAGE_TITLES[currentPage];
        int titleWidth = this.font.width(title);
        this.font.drawInBatch(title, centerX + (BOOK_WIDTH - titleWidth) / 2, centerY + 15, 0x000000, false,
                guiGraphics.pose().last().pose(), guiGraphics.bufferSource(),
                net.minecraft.client.gui.Font.DisplayMode.NORMAL, 0, 15728880);

        // Draw page content
        String[] pageLines = PAGE_CONTENT[currentPage];
        int lineY = centerY + 40;
        int lineHeight = 12;

        for (String line : pageLines) {
            if (lineY > centerY + BOOK_HEIGHT - 40) break;
            this.font.drawInBatch(line, centerX + 20, lineY, 0x000000, false,
                    guiGraphics.pose().last().pose(), guiGraphics.bufferSource(),
                    net.minecraft.client.gui.Font.DisplayMode.NORMAL, 0, 15728880);
            lineY += lineHeight;
        }

        // Draw page number
        String pageNum = "Page " + (currentPage + 1) + "/" + totalPages;
        int pageNumWidth = this.font.width(pageNum);
        this.font.drawInBatch(pageNum, centerX + (BOOK_WIDTH - pageNumWidth) / 2, centerY + BOOK_HEIGHT - 15, 0x666666, false,
                guiGraphics.pose().last().pose(), guiGraphics.bufferSource(),
                net.minecraft.client.gui.Font.DisplayMode.NORMAL, 0, 15728880);


        for (var widget : this.renderables) {
            widget.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        }
    }


    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(null);
    }
}
