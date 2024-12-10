package millenniumambiguity.nomoresingleplayerlatency;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import static millenniumambiguity.nomoresingleplayerlatency.Knockback.estimateKnockback;

public class MouseButtonLeftPress {

    public static boolean mouseButtonLeftPress() {

        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null) return false;
        if (minecraft.level == null || minecraft.player == null) return false;

        HitResult hitResult = minecraft.hitResult;
        if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {

            final var hitTarget = ((EntityHitResult) hitResult).getEntity();
            if (!(hitTarget instanceof LivingEntity localTarget)) return false;
            if (!localTarget.isAttackable()) return false;
            final var localPlayer = minecraft.player;
            if (localTarget.skipAttackInteraction(localPlayer)) return false;
            if (!localPlayer.canAttack(localTarget)) return false;

            // when playing on remote server
            if (!minecraft.hasSingleplayerServer()) {
                // estimate and apply knockback
                estimateKnockback(localTarget, localPlayer);
                // sometimes the y knockback is negative when on ground. Should not be the case. Hench this fix:
                if (localTarget.getDeltaMovement().y < 0 && localTarget.onGround())
                    localTarget.setDeltaMovement(localTarget.getDeltaMovement().multiply(1, -1, 1));

                final var localPos1 = localTarget.position().add(localTarget.getDeltaMovement().multiply(.5, 1, .5));
                localTarget.setPos(localPos1);

                // frame2 knockback.
                final var localPos2 = localPos1.add(localTarget.getDeltaMovement().multiply(.5, 1, .5));
                localTarget.xo = localPos2.x;
                localTarget.yo = localPos2.y;
                localTarget.zo = localPos2.z;

                localTarget.hasImpulse = false;
                return false;
            }

            // Clientside attack animations.
            final var localDamageType = minecraft.level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC);
            final var localDamageSource = new DamageSource(localDamageType, localPlayer);
            localTarget.handleDamageEvent(localDamageSource);
            localPlayer.swing(InteractionHand.MAIN_HAND);
            localPlayer.resetAttackStrengthTicker();

            // Get server objects.
            final var serverLevel = minecraft.getSingleplayerServer().getLevel(localPlayer.level().dimension());
            final var serverPlayer = serverLevel.getPlayerByUUID(localPlayer.getUUID());
            final var serverTarget = (LivingEntity) serverLevel.getEntity(localTarget.getUUID());

            // Serverside attack.
            serverPlayer.attack(serverTarget);
            serverTarget.hasImpulse = false;

            // Append velocity to local target.
            localTarget.setDeltaMovement(serverTarget.getDeltaMovement());

            // sometimes the y knockback is negative when on ground. Should not be the case. Hench this fix:
            if (localTarget.getDeltaMovement().y < 0 && localTarget.onGround())
                localTarget.setDeltaMovement(localTarget.getDeltaMovement().multiply(1, -1, 1));

            // Instant knockback.
            localTarget.setOldPosAndRot();
            final var pos1 = localTarget.position().add(localTarget.getDeltaMovement().multiply(.5, 1, .5));
            localTarget.setPos(pos1);

            // frame2 knockback.
            final var pos2 = pos1.add(localTarget.getDeltaMovement());
            localTarget.xo = pos2.x;
            localTarget.yo = pos2.y;
            localTarget.zo = pos2.z;

            //localTarget.moveTo(localTarget.position().add(localTarget.getDeltaMovement().multiply(.5, 1, .5)));

            // Copy server data to client.
            localTarget.setLastHurtByPlayer(localPlayer);
            localTarget.setRemainingFireTicks(serverTarget.getRemainingFireTicks());
            localTarget.deathTime = serverTarget.deathTime;
            localTarget.hurtMarked = serverTarget.hurtMarked;
            localTarget.hasImpulse = false;

            return true;
        }

        return false;
    }

}
