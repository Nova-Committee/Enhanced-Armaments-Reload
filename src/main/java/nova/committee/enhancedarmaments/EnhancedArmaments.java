package nova.committee.enhancedarmaments;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nova.committee.enhancedarmaments.common.config.EAConfig;
import nova.committee.enhancedarmaments.common.network.PacketHandler;
import nova.committee.enhancedarmaments.init.handler.RarityHandler;

@Mod(Static.MODID)
public class EnhancedArmaments {


    public EnhancedArmaments() {
        PacketHandler.registerMessage();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::config);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EAConfig.SPEC);
        RarityHandler.getInstance().loadRarities();
    }

    private void config(ModConfigEvent event) {
        if (event.getConfig().getSpec() == EAConfig.SPEC)
            EAConfig.load();
    }
}
