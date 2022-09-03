package nova.committee.enhancedarmaments;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nova.committee.enhancedarmaments.common.config.Config;
import nova.committee.enhancedarmaments.init.handler.PacketHandler;
import nova.committee.enhancedarmaments.init.ClientProxy;
import nova.committee.enhancedarmaments.init.IProxy;
import nova.committee.enhancedarmaments.init.ServerProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EnhancedArmaments.MODID)
public class EnhancedArmaments {

    public static final String MODID = "enhancedarmaments";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);


    public EnhancedArmaments() {
        PacketHandler.registerMessage();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::config);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void setup(FMLCommonSetupEvent event) {

    }


    private void clientInit(FMLClientSetupEvent event) {
        proxy.init();
    }

    private void config(ModConfig.ModConfigEvent event) {
        if (event.getConfig().getSpec() == Config.SPEC)
            Config.load();
    }


}
