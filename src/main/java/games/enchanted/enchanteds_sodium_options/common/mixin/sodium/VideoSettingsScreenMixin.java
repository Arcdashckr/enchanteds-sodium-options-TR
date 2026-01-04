package games.enchanted.enchanteds_sodium_options.common.mixin.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import games.enchanted.enchanteds_sodium_options.common.gui.VideoOptionsScreen;
import net.caffeinemc.mods.sodium.client.gui.SodiumOptions;
import net.caffeinemc.mods.sodium.client.gui.VideoSettingsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = VideoSettingsScreen.class, priority = 950)
public class VideoSettingsScreenMixin extends Screen {
    protected VideoSettingsScreenMixin(Component title) {
        super(title);
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/gui/SodiumOptions;isReadOnly()Z"),
        method = "createScreen"
    )
    private static boolean wrapSodiumVideoScreenCtor(SodiumOptions instance, Operation<Boolean> original, Screen currentScreen, @Cancellable CallbackInfoReturnable<Screen> cir) {
        boolean configReadOnly = original.call(instance);
        if(!configReadOnly) {
            cir.setReturnValue(new VideoOptionsScreen(currentScreen));
            return false;
        }
        return true;
    }
}
