package games.enchanted.eg_vsvs.common.gui.widget.option;

import net.caffeinemc.mods.sodium.client.config.structure.Option;

public interface OptionWidget<T extends Option> {
    T getOption();
}
