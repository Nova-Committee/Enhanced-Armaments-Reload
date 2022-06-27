package nova.committee.enhancedarmaments;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import nova.committee.enhancedarmaments.common.config.Config;
import nova.committee.enhancedarmaments.common.network.GuiAbilityPacket;
import nova.committee.enhancedarmaments.common.network.PacketHandler;
import nova.committee.enhancedarmaments.init.ClientProxy;
import nova.committee.enhancedarmaments.init.IProxy;
import nova.committee.enhancedarmaments.init.ServerProxy;
import nova.committee.enhancedarmaments.init.handler.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("enhancedarmaments")
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
