package nova.committee.enhancedarmaments.init.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/28 0:26
 * Version: 1.0
 */
public interface ProjectileImpactCallback {
    Event<ProjectileImpactCallback> EVENT = EventFactory.createArrayBacked(ProjectileImpactCallback.class, callbacks -> (proj, hit) -> {
        for (ProjectileImpactCallback callback : callbacks) {
            if (callback.onImpact(proj, hit)) return true;
        }
        return false;
    });

    boolean onImpact(Projectile projectile, HitResult hitResult);
}
