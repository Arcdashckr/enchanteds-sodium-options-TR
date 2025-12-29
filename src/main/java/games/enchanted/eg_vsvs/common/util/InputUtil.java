package games.enchanted.eg_vsvs.common.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;

public class InputUtil {
    public static InputConstants.Key getKey(int key) {
        return getKey(key, 0);
    }

    public static InputConstants.Key getKey(int key, int scancode) {
        return InputConstants.getKey(new KeyEvent(key, scancode, 0));
    }

    public static boolean shouldShowDebugWidgetBound() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_RSHIFT);
    }
}
