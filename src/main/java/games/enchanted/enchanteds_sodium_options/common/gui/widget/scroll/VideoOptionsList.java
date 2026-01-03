package games.enchanted.enchanteds_sodium_options.common.gui.widget.scroll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class VideoOptionsList extends VerticalScrollContainerWidget<VideoOptionsList.Entry> {
    public static final Identifier LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/menu_list_background.png");
    public static final Identifier INWORLD_LIST_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");
    public static final int LIST_BACKGROUND_TEXTURE_SIZE = 32;

    public static final int DEFAULT_CHILD_HEIGHT = 25;
    public static final int DEFAULT_CHILD_WIDTH = 150;
    public static final int ROW_WIDTH = 310;

    @Nullable private Entry lastEntry = null;

    public VideoOptionsList(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void visitChildren(Consumer<AbstractWidget> visitor) {
        this.children().forEach(child -> child.widgetChildren().forEach(visitor));
    }


    public WidgetPosition addOption(AbstractWidget child) {
        if(this.lastEntry != null && this.lastEntry instanceof OptionEntry optionEntry) {
            optionEntry.setSecondChild(child);
            this.lastEntry = null;
            return new WidgetPosition(this.children().size() - 1, true);
        }
        child.setWidth(DEFAULT_CHILD_WIDTH);
        Entry entry = new OptionEntry(child);
        this.lastEntry = entry;
        this.addChild(entry);
        return new WidgetPosition(this.children().size() - 1, false);
    }

    public WidgetPosition addBigOption(AbstractWidget child) {
        this.lastEntry = null;
        child.setWidth(ROW_WIDTH);
        this.addChild(new OptionEntry(child));
        return new WidgetPosition(this.children().size() - 1, false);
    }

    public void addHeader(Component header) {
        this.lastEntry = null;
        this.addChild(new HeaderEntry(header));
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
        return getRowRight() + 8;
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
        @Nullable AbstractWidget secondChild;

        OptionEntry(AbstractWidget widget) {
            setMargins(new Margin(0, 0));
            this.child = widget;
        }

        void setSecondChild(AbstractWidget child) {
            this.secondChild = child;
        }

        @Override
        public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
            this.child.setX(this.getContentX());
            this.child.setY(this.getContentYMiddle() - this.child.getHeight() / 2);
            this.child.render(graphics, mouseX, mouseY, partialTick);

            if(this.secondChild == null) return;
            this.secondChild.setX(this.getContentRight() - this.secondChild.getWidth());
            this.secondChild.setY(this.getContentYMiddle() - this.secondChild.getHeight() / 2);
            this.secondChild.render(graphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends AbstractWidget> widgetChildren() {
            if(secondChild != null) return List.of(child, secondChild);
            return List.of(child);
        }

        @Override
        public List<? extends NarratableEntry> narratableChildren() {
            if(secondChild != null) return List.of(child, secondChild);
            return List.of(child);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            if(secondChild != null) return List.of(child, secondChild);
            return List.of(child);
        }
    }

    static class HeaderEntry extends Entry {
        final Component header;
        final Font font = Minecraft.getInstance().font;

        HeaderEntry(Component header) {
            setMargins(new Margin(8, 0, 0, 0));
            this.header = header;
        }

        @Override
        protected int height() {
            return this.font.lineHeight;
        }

        @Override
        public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
            graphics.drawString(this.font, this.header, this.getContentX(), this.getContentY(), -1);
        }

        @Override
        public List<? extends AbstractWidget> widgetChildren() {
            return List.of();
        }

        @Override
        public List<? extends NarratableEntry> narratableChildren() {
            return List.of();
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of();
        }
    }

    public record WidgetPosition(int entryIndex, boolean secondary) {
    }
}
