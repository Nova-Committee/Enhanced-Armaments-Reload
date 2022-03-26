package nova.committee.enhancedarmaments;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import nova.committee.enhancedarmaments.common.config.Config;
import nova.committee.enhancedarmaments.common.network.GuiAbilityPacket;
import nova.committee.enhancedarmaments.init.ClientProxy;
import nova.committee.enhancedarmaments.init.ISidedProxy;
import nova.committee.enhancedarmaments.init.handler.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("enhancedarmaments")
public class EnhancedArmaments {

    public static final String MODID = "enhancedarmaments";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final ISidedProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, null);
    private static final String PROTOCOL_VERSION = "1.0";
    public static SimpleChannel network = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(MODID, "networking"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public EnhancedArmaments() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::config);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void setup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ItemTooltipEventHandler());
        MinecraftForge.EVENT_BUS.register(new LivingUpdateEventHandler());
        MinecraftForge.EVENT_BUS.register(new InputEventHandler());
        MinecraftForge.EVENT_BUS.register(new LivingHurtEventHandler());
        MinecraftForge.EVENT_BUS.register(new LivingDeathEventHandler());

        network.registerMessage(0, GuiAbilityPacket.class, GuiAbilityPacket::encode, GuiAbilityPacket::decode, GuiAbilityPacket::handle);
    }


    private void clientInit(FMLClientSetupEvent event) {
        proxy.init();
    }

    private void config(ModConfigEvent event) {
        if (event.getConfig().getSpec() == Config.SPEC)
            Config.load();
    }


}
