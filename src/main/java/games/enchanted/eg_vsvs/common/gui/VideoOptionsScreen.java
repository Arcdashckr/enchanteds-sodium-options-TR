package games.enchanted.eg_vsvs.common.gui;

import games.enchanted.eg_vsvs.common.gui.widget.scroll.VideoOptionsList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class VideoOptionsScreen extends Screen {
    final Screen parent;
    VideoOptionsList optionsList;

    public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    public VideoOptionsScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(this.title, this.font);
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(Button.BIG_WIDTH).build());
        this.layout.visitWidgets(this::addRenderableWidget);

        int headerHeight = this.layout.getHeaderHeight();
        this.optionsList = new VideoOptionsList(0, headerHeight, this.width, this.height - headerHeight - this.layout.getFooterHeight());
        this.addRenderableWidget(this.optionsList);
        for (int i = 0; i < 119; i++) {
            this.optionsList.addOption(Button.builder(Component.empty(), button -> {}).build());
        }

        this.repositionElements();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        if(optionsList != null) {
            int headerHeight = this.layout.getHeaderHeight();
            this.optionsList.setRectangle(this.width, this.height - headerHeight - this.layout.getFooterHeight(), 0, headerHeight);
            this.optionsList.repositionElements();
        }
    }
}
