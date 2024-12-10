package millenniumambiguity.nomoresingleplayerlatency;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class Knockback {

    /**
     * Precook knockback.
     *
     * @param entity   LivingEntity target
     * @param strength knockback strength
     * @param x
     * @param z
     */
    public static void livingKnockback(LivingEntity entity, double strength, double x, double z) {

        knockback(entity, strength, x, z);

        // For players, we precook 3 ticks.
        if (entity.getLastDamageSource() != null && entity.getLastDamageSource().getEntity() instanceof Player) {
            entity.travel(Vec3.ZERO);
            entity.travel(Vec3.ZERO);
        }

        // precook 1 tick for all knockback
        entity.travel(Vec3.ZERO);
    }

    /**
     * calculate and apply knockback simulacra to Player.attack(entity) but without any damage applied.
     * net.minecraft.world.entity.player.Player.attack
     *
     * @param entity attacked target
     * @param player attacker
     */
    public static void estimateKnockback(LivingEntity entity, Player player) {
        float f = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1 = EnchantmentHelper.getDamageBonus(player.getMainHandItem(), entity.getType());
        float f2 = player.getAttackStrengthScale(0.5F);
        f *= 0.2F + f2 * f2 * 0.8F;
        f1 *= f2;
        if (f > 0.0F || f1 > 0.0F) {
            boolean flag = f2 > 0.9F;
            float i = (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
            i += (float) EnchantmentHelper.getKnockbackBonus(player);
            if (player.isSprinting() && flag) {
                ++i;
            }

            if (!entity.isInvulnerableTo(player.damageSources().playerAttack(player))) {
                if (i > 0.0F) {
                    knockback(entity, i * 0.5F,
                            Mth.sin(player.getYRot() * 0.017453292F),
                            -Mth.cos(player.getYRot() * 0.017453292F));
                }
            }
        }
    }

    /**
     * Vanilla knockback without modloader hooks.
     * net.minecraft.world.entity.LivingEntity.knockback
     *
     * @param entity   LivingEntity target
     * @param strength knockback strength
     * @param x
     * @param z
     */
    private static void knockback(LivingEntity entity, double strength, double x, double z) {
        strength *= 1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        if (!(strength <= 0.0)) {
            entity.hasImpulse = true;
            Vec3 vec3 = entity.getDeltaMovement();
            Vec3 vec31 = (new Vec3(x, 0.0, z)).normalize().scale(strength);
            entity.setDeltaMovement(vec3.x / 2.0 - vec31.x, entity.onGround() ? Math.min(0.4, vec3.y / 2.0 + strength) : vec3.y, vec3.z / 2.0 - vec31.z);
        }
    }
}
