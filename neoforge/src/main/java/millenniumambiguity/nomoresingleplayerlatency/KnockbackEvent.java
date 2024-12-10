package millenniumambiguity.nomoresingleplayerlatency;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;

import static millenniumambiguity.nomoresingleplayerlatency.Knockback.livingKnockback;

public class KnockbackEvent {

    @SubscribeEvent
    public static void onLivingKnockback(LivingKnockBackEvent event) {

        // Call common code.
        livingKnockback(event.getEntity(), event.getStrength(), event.getRatioX(), event.getRatioZ());

        // Skip default knockback.
        event.setCanceled(true);
    }
}
