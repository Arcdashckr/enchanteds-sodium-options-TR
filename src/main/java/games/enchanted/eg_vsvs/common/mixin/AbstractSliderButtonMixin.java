package games.enchanted.eg_vsvs.common.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import games.enchanted.eg_vsvs.common.gui.widget.extension.AbstractSliderButtonExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractSliderButton.class)
public abstract class AbstractSliderButtonMixin extends AbstractWidget {
    public AbstractSliderButtonMixin(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/AbstractSliderButton;isHovered()Z"),
        method = "renderWidget"
    )
    public boolean wrapHoverCheck(AbstractSliderButton instance, Operation<Boolean> original, GuiGraphics guiGraphics) {
        if(this instanceof AbstractSliderButtonExtension) {
            if(original.call(instance) && !this.isActive()) {
                guiGraphics.requestCursor(CursorTypes.NOT_ALLOWED);
                return false;
            }
        }
        return original.call(instance);
    }

    @WrapMethod(
        method = "getHandleSprite"
    )
    public Identifier wrapSpriteGetter(Operation<Identifier> original) {
        if(this instanceof AbstractSliderButtonExtension extension && !this.isActive()) {
            return extension.eg_vsvs$getDisabledHandleSprite();
        }
        return original.call();
    }
}
