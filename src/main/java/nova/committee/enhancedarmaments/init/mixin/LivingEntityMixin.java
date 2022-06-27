package nova.committee.enhancedarmaments.init.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nova.committee.enhancedarmaments.init.callback.EntityEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/27 22:49
 * Version: 1.0
 */
@Mixin(value = LivingEntity.class, priority = 1001)
public abstract class LivingEntityMixin {

    @Shadow
    protected ItemStack useItem;

    @Shadow public abstract InteractionHand getUsedItemHand();

    @Inject(method = "tick()V", at = @At(value = "HEAD"))
    public void LivingEntity_tick(CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        Level world = entity.getCommandSenderWorld();

        EntityEvents.LIVING_TICK.invoker().onTick(world, entity);
    }

    @Inject(method = "die(Lnet/minecraft/world/damagesource/DamageSource;)V", at = @At(value = "HEAD"))
    public void LivingEntity_die(DamageSource damageSource, CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        Level world = entity.getCommandSenderWorld();

        EntityEvents.LIVING_ENTITY_DEATH.invoker().onDeath(world, entity, damageSource);
    }

    @Inject(method = "calculateFallDamage(FF)I", at = @At(value = "RETURN"), cancellable = true)
    protected void LivingEntity_calculateFallDamage(float f, float g, CallbackInfoReturnable<Integer> ci) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        Level world = livingEntity.getCommandSenderWorld();

        if (EntityEvents.ON_FALL_DAMAGE_CALC.invoker().onFallDamageCalc(world, livingEntity, f, g) == 0) {
            ci.setReturnValue(0);
            return;
        }

        MobEffectInstance mobEffectInstance = livingEntity.getEffect(MobEffects.JUMP);
        float h = mobEffectInstance == null ? 0.0F : (float)(mobEffectInstance.getAmplifier() + 1);
        ci.setReturnValue(Mth.ceil((f - 3.0F - h) * g));
    }

    @ModifyVariable(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value= "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(FF)F", ordinal = 0), ordinal = 0, argsOnly = true)
    private float LivingEntity_actuallyHurt(float f, DamageSource damageSource, float damage) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        Level world = livingEntity.getCommandSenderWorld();

        float newDamage = EntityEvents.ON_LIVING_DAMAGE_CALC.invoker().onLivingDamageCalc(world, livingEntity, damageSource, f);
        if (newDamage != -1 && newDamage != f) {
            return newDamage;
        }

        return f;
    }

    @Inject(method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "HEAD"), cancellable = true)
    public void LivingEntity_hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> ci) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        Level world = livingEntity.getCommandSenderWorld();

        if (!EntityEvents.ON_LIVING_ATTACK.invoker().onLivingAttack(world, livingEntity, damageSource, f)) {
            ci.setReturnValue(false);
        }
    }

    @Inject(method = "dropAllDeathLoot(Lnet/minecraft/world/damagesource/DamageSource;)V", at = @At(value = "TAIL"))
    protected void dropAllDeathLoot(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        Level world = livingEntity.getCommandSenderWorld();

        EntityEvents.ON_ENTITY_IS_DROPPING_LOOT.invoker().onDroppingLoot(world, livingEntity, damageSource);
    }

    @Inject(method = "jumpFromGround", at = @At(value = "TAIL"))
    protected void LivingEntity_jumpFromGround(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        Level world = livingEntity.getCommandSenderWorld();

        EntityEvents.ON_ENTITY_IS_JUMPING.invoker().onLivingJump(world, livingEntity);
    }
}
