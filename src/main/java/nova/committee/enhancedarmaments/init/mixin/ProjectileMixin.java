package nova.committee.enhancedarmaments.init.mixin;

import nova.committee.enhancedarmaments.init.callback.ProjectileImpactCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/28 0:27
 * Version: 1.0
 */
@Mixin(Projectile.class)
public abstract class ProjectileMixin {
    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    private void port_lib$onProjectileHit(HitResult result, CallbackInfo ci) {
        if (ProjectileImpactCallback.EVENT.invoker().onImpact((Projectile) (Object) this, result)) {
            ci.cancel();
        }
    }
}
