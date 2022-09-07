package nova.committee.enhancedarmaments.init.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/27 22:46
 * Version: 1.0
 */
public class EntityEvents {

    private EntityEvents() { }

    public static final Event<Living_Tick> LIVING_TICK = EventFactory.createArrayBacked(Living_Tick.class, callbacks -> (world, entity) -> {
        for (Living_Tick callback : callbacks) {
            callback.onTick(world, entity);
        }
    });

    public static final Event<Living_Entity_Death> LIVING_ENTITY_DEATH = EventFactory.createArrayBacked(Living_Entity_Death.class, callbacks -> (world, entity, source) -> {
        for (Living_Entity_Death callback : callbacks) {
            callback.onDeath(world, entity, source);
        }
    });


    public static final Event<Pre_Entity_Join_World> PRE_ENTITY_JOIN_WORLD = EventFactory.createArrayBacked(Pre_Entity_Join_World.class, callbacks -> (world, entity) -> {
        for (Pre_Entity_Join_World callback : callbacks) {
            if (!callback.onPreSpawn(world, entity)) {
                return false;
            }
        }
        return true;
    });

    public static final Event<Entity_Fall_Damage_Calc> ON_FALL_DAMAGE_CALC = EventFactory.createArrayBacked(Entity_Fall_Damage_Calc.class, callbacks -> (world, entity, f, g) -> {
        for (Entity_Fall_Damage_Calc callback : callbacks) {
            if (callback.onFallDamageCalc(world, entity, f, g) == 0) {
                return 0;
            }
        }
        return 1;
    });

    public static final Event<Entity_Living_Damage> ON_LIVING_DAMAGE_CALC = EventFactory.createArrayBacked(Entity_Living_Damage.class, callbacks -> (world, entity, damageSource, damageAmount) -> {
        for (Entity_Living_Damage callback : callbacks) {
            float newDamage = callback.onLivingDamageCalc(world, entity, damageSource, damageAmount);
            if (newDamage != damageAmount) {
                return newDamage;
            }
        }

        return -1;
    });

    public static final Event<Entity_Living_Attack> ON_LIVING_ATTACK = EventFactory.createArrayBacked(Entity_Living_Attack.class, callbacks -> (world, entity, damageSource, damageAmount) -> {
        for (Entity_Living_Attack callback : callbacks) {
            if (!callback.onLivingAttack(world, entity, damageSource, damageAmount)) {
                return false;
            }
        }

        return true;
    });

    public static final Event<Entity_Is_Dropping_Loot> ON_ENTITY_IS_DROPPING_LOOT = EventFactory.createArrayBacked(Entity_Is_Dropping_Loot.class, callbacks -> (world, entity, damageSource) -> {
        for (Entity_Is_Dropping_Loot callback : callbacks) {
            callback.onDroppingLoot(world, entity, damageSource);
        }
    });

    public static final Event<Entity_Is_Jumping> ON_ENTITY_IS_JUMPING = EventFactory.createArrayBacked(Entity_Is_Jumping.class, callbacks -> (world, entity) -> {
        for (Entity_Is_Jumping callback : callbacks) {
            callback.onLivingJump(world, entity);
        }
    });

    @FunctionalInterface
    public interface Living_Tick {
        void onTick(Level world, Entity entity);
    }

    @FunctionalInterface
    public interface Living_Entity_Death {
        void onDeath(Level world, Entity entity, DamageSource source);
    }

    @FunctionalInterface
    public interface Pre_Entity_Join_World {
        boolean onPreSpawn(Level world, Entity entity);
    }

    @FunctionalInterface
    public interface Entity_Fall_Damage_Calc {
        int onFallDamageCalc(Level world, Entity entity, float f, float g);
    }

    @FunctionalInterface
    public interface Entity_Living_Damage {
        float onLivingDamageCalc(Level world, Entity entity, DamageSource damageSource, float damageAmount);
    }

    @FunctionalInterface
    public interface Entity_Living_Attack {
        boolean onLivingAttack(Level world, Entity entity, DamageSource damageSource, float damageAmount);
    }

    @FunctionalInterface
    public interface Entity_Is_Dropping_Loot {
        void onDroppingLoot(Level world, Entity entity, DamageSource damageSource);
    }

    @FunctionalInterface
    public interface Entity_Is_Jumping {
        void onLivingJump(Level world, Entity entity);
    }
}
