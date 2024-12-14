package millenniumambiguity.nomoresingleplayerlatency.mixin;

import millenniumambiguity.nomoresingleplayerlatency.Knockback;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "knockback", at = @At("TAIL"))
    public void knockback0(double strength, double x, double z, CallbackInfo ci) {
        Knockback.livingKnockbackPost((LivingEntity)(Object)this);
    }
}
