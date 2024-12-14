package millenniumambiguity.nomoresingleplayerlatency.mixin;

import net.minecraft.client.MouseHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static millenniumambiguity.nomoresingleplayerlatency.MouseButtonLeftPress.mouseButtonLeftPressFromMixin;

@Mixin(net.minecraft.client.MouseHandler.class)
public class MouseMixin {

    @Shadow private boolean isLeftPressed;

    @Redirect(method = "onPress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHandler;isLeftPressed:Z", opcode = Opcodes.PUTFIELD))
    public void Press(MouseHandler instance, boolean value){
        if (value) {
            isLeftPressed = !mouseButtonLeftPressFromMixin();
        } else {
            isLeftPressed = false;
        }
    }
}
