package nova.committee.enhancedarmaments;

import net.fabricmc.api.ModInitializer;
import nova.committee.enhancedarmaments.init.handler.*;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/27 21:57
 * Version: 1.0
 */
public class Enhancedarmaments implements ModInitializer {
    @Override
    public void onInitialize() {

        Static.config = ConfigHandler.load();
        CommandEventHandler.init();
        ItemTooltipEventHandler.addInformation();
        LivingDeathEventHandler.init();
        LivingHurtEventHandler.onHurt();
        LivingHurtEventHandler.onArrowShoot();
        LivingHurtEventHandler.onArrowHit();
        LivingUpdateEventHandler.onUpdate();
    }
}
