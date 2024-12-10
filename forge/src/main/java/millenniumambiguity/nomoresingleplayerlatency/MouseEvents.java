package millenniumambiguity.nomoresingleplayerlatency;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MouseEvents {

    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseButton.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.player != null) {
            if (event.getAction() == GLFW.GLFW_PRESS && event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                // The player has initiated a left-click attack on the client
                System.out.println("Client attack started!");

                // You can perform your custom logic here
                // Example: Check what the player is looking at
                HitResult hitResult = minecraft.hitResult;
                if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                    System.out.println("Attacking entity: " + ((EntityHitResult) hitResult).getEntity());
                    //var damageType = minecraft.level.registryAccess()
                    //        .registryOrThrow(Registries.DAMAGE_TYPE)
                    //        .getHolderOrThrow(DamageTypes.GENERIC);
                    //var d = new DamageSource(damageType, minecraft.player, minecraft.player, minecraft.player.position());
                    //((EntityHitResult) hitResult).getEntity().handleDamageEvent(d);
                    event.setCanceled(true);
                }
            }
        }
    }

}
