package games.enchanted.eg_vsvs.common.gui.widget.scroll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class VideoOptionsList extends VerticalScrollContainerWidget<VideoOptionsList.Entry> {
    public static final Identifier LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/menu_list_background.png");
    public static final Identifier INWORLD_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");
    public static final int LIST_BACKGROUND_TEXTURE_SIZE = 32;

    public static final int DEFAULT_CHILD_HEIGHT = 25;
    public static final int ROW_WIDTH = 310;

    public VideoOptionsList(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void visitChildren(Consumer<AbstractWidget> visitor) {
        this.children().forEach(child -> child.widgetChildren().forEach(visitor));
    }

    public void addOption(AbstractWidget child) {
        this.addChild(new OptionEntry(child));
    }

    @Override
    public int getRowWidth() {
        return ROW_WIDTH;
    }

    @Override
    public int getRowLeft() {
        return this.getMiddleX() - (ROW_WIDTH / 2);
    }

    @Override
    public int getRowRight() {
        return this.getMiddleX() + (ROW_WIDTH / 2);
    }

    private int getMiddleX() {
        return this.getX() + (this.getWidth() / 2);
    }

    @Override
    protected int scrollBarX() {
        return getRowRight();
    }

    @Override
    protected void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean notInWorld = Minecraft.getInstance().level == null;

        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
            notInWorld ? LIST_BACKGROUND : INWORLD_LIST_BACKGROUND,
            this.getX(),
            this.getY(),
            this.getRight(),
            this.getBottom() + (int)this.scrollAmount(),
            this.getWidth(),
            this.getHeight(),
            LIST_BACKGROUND_TEXTURE_SIZE,
            LIST_BACKGROUND_TEXTURE_SIZE
        );

        int separatorTextureWidth = 32;
        int separatorTextureHeight = 2;
        int separatorHeight = 2;

        Identifier headerSeparator = notInWorld ? Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR;
        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
            headerSeparator,
            this.getX(),
            this.getY() - 2,
            0.0F,
            0.0F,
            this.getWidth(),
            separatorTextureHeight,
            separatorTextureWidth,
            separatorHeight
        );
        Identifier footerSeparator = notInWorld ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
            footerSeparator,
            this.getX(),
            this.getBottom(),
            0.0F,
            0.0F,
            this.getWidth(),
            separatorTextureHeight,
            separatorTextureWidth,
            separatorHeight
        );
    }

    static abstract class Entry extends Child {
        @Override
        protected int height() {
            return DEFAULT_CHILD_HEIGHT;
        }
    }

    static class OptionEntry extends Entry {
        final AbstractWidget child;

        OptionEntry(AbstractWidget widget) {
            setMargins(new Margin(0, 0));
            this.child = widget;
        }

        @Override
        public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
            this.child.setX(this.getContentX());
            this.child.setY(this.getContentY());
            this.child.render(graphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends AbstractWidget> widgetChildren() {
            return List.of(child);
        }

        @Override
        public List<? extends NarratableEntry> narratableChildren() {
            return List.of(child);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(child);
        }
    }
}
