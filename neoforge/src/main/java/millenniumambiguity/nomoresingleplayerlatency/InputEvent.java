package millenniumambiguity.nomoresingleplayerlatency;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import static millenniumambiguity.nomoresingleplayerlatency.MouseButtonLeftPress.mouseButtonLeftPress;

public class InputEvent {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onMouseClick(net.neoforged.neoforge.client.event.InputEvent.MouseButton.Pre event) {
        if (event.getAction() == GLFW.GLFW_PRESS && event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (mouseButtonLeftPress()) {
                // Cancel the mouse event.
                event.setCanceled(true);
            }
        }
    }
}
