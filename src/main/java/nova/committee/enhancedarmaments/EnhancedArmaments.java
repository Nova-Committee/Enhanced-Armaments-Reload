package nova.committee.enhancedarmaments;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nova.committee.enhancedarmaments.common.config.Config;
import nova.committee.enhancedarmaments.common.network.PacketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EnhancedArmaments.MODID)
public class EnhancedArmaments {

    public static final String MODID = "enhancedarmaments";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public EnhancedArmaments() {
        PacketHandler.registerMessage();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::config);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void config(ModConfigEvent event) {
        if (event.getConfig().getSpec() == Config.SPEC)
            Config.load();
    }
}
